package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import static com.regin.reginald.vehicleanddrivers.MainActivity.round;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.Routes;
import com.regin.reginald.vehicleanddrivers.Aariyan.Abstraction.BaseActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.OrdersAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.Resource;
import com.regin.reginald.vehicleanddrivers.Aariyan.ViewModel.OrdersViewModel;
import com.regin.reginald.vehicleanddrivers.Data;
import com.regin.reginald.vehicleanddrivers.GPSTracker;
import com.regin.reginald.vehicleanddrivers.MainActivity;
import com.regin.reginald.vehicleanddrivers.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdersActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private IpModel serverModel;
    private String deliveryDate;
    private int routeId, orderId;
    private DatabaseAdapter databaseAdapter;
    private OrdersViewModel orderViewModel;
    private RecyclerView recyclerView;

    private TextView routeNames, orderTypes, dDate, endTripBtn, tripInfoBtn, uploadedCount, calcr_plan, coord;

    private ImageView backBtn;
    private TextView acknowledgeStockBtn;

    private ProgressBar progressBar;

    String SERVERIP;

    Handler handler = new Handler();
    Runnable runnableUpload;
    int delayUpload = 10000;
    double lat = -33.966145;
    double lon = 22.466218, custlat, custlon;

    /**
     * Copied from his Code
     */
    double prevlat = -33.966145;
    double prevlon = 22.466218;
    static double PI_RAD = Math.PI / 180.0;
    GPSTracker gps;
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyC5vAgb-nawregIa5gRRG34wnabasN3blk")
            .build();
    private static final String TAG = "InvoiceActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    Dialog dialog;
    private FirebaseAuth mAuth;

    boolean hasCratesModule = false;
    private String DriverEmail;
    double mass;
    private String DriverPassword;

    private static List<OrderModel> listOfOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        databaseAdapter = new DatabaseAdapter(this);
        List<IpModel> list = databaseAdapter.getServerIpModel();
        if (list.size() > 0) {
            SERVERIP = list.get(0).getServerIp();
        } else {
            SERVERIP = "";
        }
        initUi();
    }

    private void initUi() {
        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeNames = findViewById(R.id.routeName);
        orderTypes = findViewById(R.id.orderType);
        dDate = findViewById(R.id.deliverDate);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDeliverySeq();
                finish();
            }
        });
        acknowledgeStockBtn = findViewById(R.id.acknowledgeBtn);
        acknowledgeStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acknowledgeStockFunc();
            }
        });
        progressBar = findViewById(R.id.progressbar);

        endTripBtn = findViewById(R.id.endTripBtn);
        uploadedCount = findViewById(R.id.uploadedCount);

        tripInfoBtn = findViewById(R.id.tripInfoBtn);
        tripInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTripInfo();
            }
        });

        calcr_plan = findViewById(R.id.calcr_plan);
        coord = findViewById(R.id.coord);

        //instantiation:
        instantiate();

        //counting the Upload & Non-Upload
        uploadedNNonUploaded();
    }

    public void UpdateDeliverySeq() {

        if (listOfOrders.size() != 0 ){
            for (int i = 0; i < listOfOrders.size(); i++) {

                OrderModel model = listOfOrders.get(i);

                int newSeq = i + 1;
                databaseAdapter.updateOrdersHeaderDeliverySequenceByInvoice(newSeq,model.getInvoiceNo());
                //dbH.updateDeals("Update OrderHeaders set DeliverySeq=" + newSeq + " Where InvoiceNo='" + model.getInvoiceNo() + "'");
            }
        }

//        for (int i = 0; i < _orderdlist.getCount(); i++) {
//
//            //yList.getAdapter().getView(i, null, null);
//            View v = _orderdlist.getAdapter().getView(i, null, null);
//
//            TextView et = (TextView) v.findViewById(R.id.orderid);
//
//            int newSeq = i + 1;
//            dbH.updateDeals("Update OrderHeaders set DeliverySeq=" + newSeq + " Where InvoiceNo='" + et.getText().toString() + "'");
//        }


    }

    private void instantiate() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mAuth = FirebaseAuth.getInstance();


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    checkLocation();

                } catch (Exception var3) {
                    Log.e("**********", "Crashed " + var3);


                }

            }
        };
        (new Thread(runnable)).start();


        if (databaseAdapter.checkIfLinesUploaded() > 0) {
            //
            if (isInternetAvailable()) {
                Toast.makeText(this, "You Are Connected ", Toast.LENGTH_SHORT).show();
                OrderHeaderPost();
            }
        }

        //has_DATA();

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

//    private void has_DATA() {
//        // TODO: it's not done yet.
//        if (databaseAdapter.hasData()) {
//            List<OrderModel> oH = databaseAdapter.returnOrderHeaders();
//            //items1 = new ArrayList<MainActivity.Item>();
//            //listdata = new ArrayList<Data>();
//
//            for (OrderModel orderAttributes : oH) {
//               /* Log.e("itemsval","***"+orderAttributes.getStoreName()+"****"+orderAttributes.getDeliveryAddress()+"*******"+orderAttributes.getInvoiceNo()+"-------"+orderAttributes.getOrderMass());
//                Item item = new Item(orderAttributes.getStoreName(), orderAttributes.getDeliveryAddress(),orderAttributes.getInvoiceNo(),
//                        orderAttributes.getoffloaded(),"1","Header",orderAttributes.getCashPaid(),orderAttributes.getoffloaded());
//                items1.add(item);*/
//
//
//                Data listItem = new Data((orderAttributes.getStoreName()).trim(), (orderAttributes.getDeliveryAddress()).trim(), (orderAttributes.getInvoiceNo()).trim(),
//                        ""+orderAttributes.getOffloaded(), "1", "Header", orderAttributes.getCashPaid(), ""+orderAttributes.getOffloaded(), ""+orderAttributes.getLatitude(), ""+orderAttributes.getLongitude(), Integer.toString(orderAttributes.getDeliverySeq()),
//                        orderAttributes.getThreshold());
//                //listdata.add(listItem);
//                mass = mass + Double.parseDouble(orderAttributes.getOrderMass());
//                acknowledgeStockBtn.setText(orderAttributes.getDriverName() + " Please acknowledge the stock");
//                DriverEmail = orderAttributes.getDriverEmail();
//                DriverPassword = orderAttributes.getDriverPassword();
//
//                mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
//                                    String user = mAuth.getCurrentUser().getUid();
//                                    String email = mAuth.getCurrentUser().getEmail();
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//                                    DatabaseReference myRef = database.getReference().child("Driver").child("users").child(user).child("lat");
//                                    DatabaseReference myRef2 = database.getReference().child("Driver").child("users").child(user).child("lon");
//                                    DatabaseReference myRefEmail = database.getReference().child("Driver").child("users").child(user).child("email");
//                                    DatabaseReference myRefstops = database.getReference().child("Driver").child("users").child(user).child("stops");
//                                    DatabaseReference myRefroute = database.getReference().child("Driver").child("users").child(user).child("route");
//                                    DatabaseReference myRefdeldate = database.getReference().child("Driver").child("users").child(user).child("deldate");
//                                    DatabaseReference myRefordertype = database.getReference().child("Driver").child("users").child(user).child("ordertype");
//
//                                    myRef.setValue(Double.toString(lat));
//                                    myRef2.setValue(Double.toString(lon));
//                                    myRefEmail.setValue(email);
//                                    myRefstops.setValue(databaseAdapter.countSigned());
//                                    myRefroute.setValue(routeNames.getText().toString());
//                                    myRefdeldate.setValue(dDate.getText().toString());
//                                    myRefordertype.setValue(orderTypes.getText().toString());
//                                    //LatLng latLng = new LatLng(lat, lon);
//                                    //updateUI(user);
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//
//                                }
//
//                                // ...
//                            }
//                        });
//                //orderAttributes.getPrice()
//                //orderAttributes.getPrice()
//            }
//            //myItemsListAdapter = new ItemsListAdapter(MainActivity.this, items1);
//            // _orderdlist.setAdapter(myItemsListAdapter);
//
////            Adapter adapter = new Adapter(this, listdata, new Adapter.Listener() {
////                @Override
////                public void onGrab(int position, TableLayout row) {
//////                    _orderdlist.onGrab(position, row);
////                }
////            });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////            _orderdlist.setAdapter(adapter);
////            _orderdlist.setListener(new CustomListView.Listener() {
////                @Override
////                public void swapElements(int indexOne, int indexTwo) {
////                    Data temp = listdata.get(indexOne);
////                    listdata.set(indexOne, listdata.get(indexTwo));
////                    listdata.set(indexTwo, temp);
////                }
////            });
//        } else {
//            uploadedCount.setText("");
//            startProgress("Syncing");
////            ArrayList<Routes> routesdata = dbH.multiId("Select * from Routes where RouteName ='" + routename.getText().toString() + "'");
////            ArrayList<Routes> ordertyp = dbH.multiId("Select * from OrderTypes where OrderType ='" + ordertype.getText().toString() + "'");
//
//            List<RouteModel> routesdata = dbH.multiId("Select * from Routes where RouteName ='" + routename.getText().toString() + "'");
//            //ArrayList<RouteModel> ordertyp = dbH.multiId("Select * from OrderTypes where OrderType ='" + ordertype.getText().toString() + "'");
//
//            for (Routes orderAttributes4 : routesdata) {
//                routeidreturned = orderAttributes4.getRouteName();
//            }
//
//            for (Routes orderAttributes4 : ordertyp) {
//                ordertypeidreturned = orderAttributes4.getRouteName();
//            }
//            Log.e("try", "******" + SERVERIP + "OrderHeaders.php?OrderType=" + ordertypeidreturned + "&Route=" + routeidreturned + "&DeliveryDate=" + deliverdate.getText().toString());
//            new MainActivity.getOrderHeaders().execute(SERVERIP + "OrderHeaders.php?OrderType=" + ordertypeidreturned + "&Route=" + routeidreturned + "&DeliveryDate=" + deliverdate.getText().toString());
//
//        }
//    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void startTripInfo() {
        tripInfoBtn.setTextColor(Color.BLUE);
        UpdateDeliverySeq();
        String[] finaldistanceAndTime = distanceAndTime();

        double t = DriversHours(mass, 0, 1, Double.parseDouble(finaldistanceAndTime[0]));
        BigDecimal result;
        result = round((float) t, 3);

        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
        View view;
        view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.trip_info_dialog, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView delv_time = (TextView) view.findViewById(R.id.delv_time);
        TextView massll = (TextView) view.findViewById(R.id.mass);
        TextView distancetocover = (TextView) view.findViewById(R.id.distancetocover);
        //progressDoalog.dismiss();

        double x = result.doubleValue() - Math.floor(result.doubleValue());
        Log.e("x****", "****************************************************************" + x);
        int tdh = result.intValue() / 1;
        double minutes = x * 60;
        title.setText("Trip info");
        delv_time.setText("" + tdh + ":" + (int) minutes + "");
        massll.setText("" + mass);
        distancetocover.setText("" + finaldistanceAndTime[0] + "km And " + finaldistanceAndTime[1] + " minutes");

        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(OrdersActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("START TRIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkLocation();

                String coordinates = lat + "," + lon;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date tomorrow = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tomorrowDate = dateFormat.format(tomorrow);
                //TODO:
                //dbH.updateDeals("UPDATE OrderHeaders set StartTripTime='" + tomorrowDate + "', strCoordinateStart='" + coordinates + "'");
            }
        });
        builder.setView(view);

        builder.show();
    }

    public double DriversHours(double tonnage, double travelTimeInMinutes, int numberOfStops, double sumdistance) {
        //100 is hardcoded for testing purpose

        // return sumdistance +(numberOfStops * 5 )+((tonnage/2)/5);
        int speedIs1KmMinute = 80;
        double estimatedDriveTimeInMinutes = sumdistance / speedIs1KmMinute;


        //return ((tonnage*0.15) + estimatedDriveTimeInMinutes)/60.00;

        return ((tonnage / 6) + sumdistance) / 60;
    }

    public String[] distanceAndTime() {
        List<Orders> orderheader = databaseAdapter.returnOrderHeaders();
        //order headers
        String[] results = new String[2];
        double totdistance = 0;
        double totduration = 0;
        ArrayList<String> stringArrayList = new ArrayList<String>();

        stringArrayList.add(lat + "," + lon);
        for (Orders orderAttributes : orderheader) {
            stringArrayList.add(orderAttributes.getLatitude() + "," + orderAttributes.getLongitude());
        }
        String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
        //start_trip.setBackgroundColor(Color.BLUE);
        String coordinate1 = "";
        String coordinate2 = "";
        int k = 0;

        for (int i = 0; i < stringArray.length; i++) {


            if (i != (stringArray.length - 1)) {
                coordinate1 = stringArray[k];
                coordinate2 = stringArray[i + 1];
                String[] res = reurndistancetime(coordinate1, coordinate2);

                if (res[0] == null || res[0].equals("NULL")) {
                    totdistance = totdistance + Double.parseDouble("0");
                    totduration = totduration + Double.parseDouble("0");
                } else {
                    totdistance = totdistance + Double.parseDouble(res[0]);
                    totduration = totduration + Double.parseDouble(res[1]);
                }

                //Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
                k = i + 1;
            }

        }
        results[0] = String.format("%.0f", totdistance);
        results[1] = String.format("%.0f", totduration);
        //Do something with result here


        return results;
    }

    public String[] reurndistancetime(String coordinate1, String coordinate2) {

        String[] ret = new String[2];
        try {


            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);

            DistanceMatrix trix = req.origins(coordinate1)
                    .destinations(coordinate2)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();


            String inMeters = (trix.rows[0].elements[0].distance.inMeters) + "";
            ret[0] = Double.toString(Double.parseDouble(inMeters) / 1000);

            String inSec = trix.rows[0].elements[0].duration.inSeconds + "";
            //ret[0] = ((trix.rows[0].elements[0].distance).toString()).replace("km","") ;
            ret[1] = Double.toString(Double.parseDouble(inSec) / 60);

            //Log.e("tttttt","*********************************"+(trix.rows[0].elements[0].distance).toString());
            //Do something with result here

            Log.e("minutes", "*********************************" + ret[1]);
            Log.e("kmeters", "*********************************" + ret[0]);
            // ....
        } catch (ApiException e) {
            // output += this.printError(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ret;
    }

    private void uploadedNNonUploaded() {
        uploadedCount.setText(databaseAdapter.OrdersNotPostedToTheOffice());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(OrdersActivity.this, "You Are Connected ", Toast.LENGTH_SHORT).show();
                uploadedCount.setText(databaseAdapter.OrdersNotPostedToTheOffice());
            }
        }, delayUpload);
    }

    private void acknowledgeStockFunc() {
        Dialog dialog = new Dialog(OrdersActivity.this, android.R.style.Theme_Light_NoTitleBar);
        dialog.setContentView(R.layout.stock_acknowledge);

      /*  TextView textView = new TextView(context);
        textView.setText();*/
        dialog.setTitle("Please Type in the Cash");
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView btn_submit_ackn = dialog.findViewById(R.id.btn_submit_ackn);
        ImageView undoSignature = dialog.findViewById(R.id.undoSignature);
        undoSignature.setVisibility(View.GONE);
        SignaturePad ack_sign = (SignaturePad) dialog.findViewById(R.id.ack_sign);
        ack_sign.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                undoSignature.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSigned() {
                undoSignature.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                undoSignature.setVisibility(View.GONE);
            }
        });

        undoSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ack_sign.clear();
            }
        });

        btn_submit_ackn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = ack_sign.getSignatureBitmap();
                //new UploadImage(signatureBitmap,IDs).execute();
                //new MainActivity.UploadImage(signatureBitmap,selectedItem.ItemString3,lat,lon, tomorrowDate).execute();
                if (addSignatureJpg(signatureBitmap, "image")) {

                    Toast.makeText(OrdersActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrdersActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private boolean addSignatureJpg(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("Drivers_App"), String.format("Drivers_App_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, invoiceNo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo, String InvoiceNo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //  OutputStream stream = new FileOutputStream(photo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteImage = outputStream.toByteArray();
        String s = Base64.encodeToString(byteImage, Base64.DEFAULT);

//UploadNewOrderLinesDetails
        //dbH.updateDeals("Update OrderHeaders set imagestring='"+s+"' where InvoiceNo ='"+InvoiceNo+"'");
        startProgress("Syncing");
//        ArrayList<Routes> routesdata = dbH.multiId("Select * from Routes where RouteName ='" + routename.getText().toString() + "'");
//        ArrayList<Routes> ordertyp = dbH.multiId("Select * from OrderTypes where OrderType ='" + ordertype.getText().toString() + "'");
//        for (Routes orderAttributes4 : routesdata) {
//            routeidreturned = orderAttributes4.getRouteName();
//        }
//
//        for (Routes orderAttributes4 : ordertyp) {
//            ordertypeidreturned = orderAttributes4.getRouteName();
//        }
        //new MainActivity.UploadNewOrderLinesDetails(deliverdate.getText().toString(), ordertypeidreturned, routeidreturned, s).execute();
        new UploadNewOrderLinesDetails(dDate.getText().toString(), orderTypes.getText().toString(), routeNames.getText().toString(), s).execute();

        // Log.e("********","***************"+s);
        //Log.e("********","***************InvoiceNo----"+InvoiceNo);
        //stream.close();
    }

    ProgressDialog progressdialog;

    public void startProgress(String msg) {
        progressdialog = new ProgressDialog(OrdersActivity.this);
        progressdialog.setMax(100);
        progressdialog.setMessage("Please Wait...." + msg);
        progressdialog.setTitle("Caution");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            file.mkdir();
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    @Override
    protected void onResume() {

        //Checking is the server is exist:
        if (isServerExist(this)) {
            serverModel = getServerModel(this);
        }

        //getting the intent value:
        if (getIntent() != null) {
            progressBar.setVisibility(View.VISIBLE);
            deliveryDate = getIntent().getStringExtra("deldate");
            Constant.DELIVERY_DATE = deliveryDate;
            routeId = getIntent().getIntExtra("routes", 0);
            orderId = getIntent().getIntExtra("ordertype", 0);
            Constant.ORDER_TYPE = orderId;
            String delivery = getIntent().getStringExtra("delivery");
            String routeName = getIntent().getStringExtra("routeName");
            Constant.ROUTES_NAME = routeName;

            dDate.setText(deliveryDate);
            routeNames.setText(routeName);
            orderTypes.setText(delivery);
            loadOrders(serverModel, deliveryDate, routeId, orderId);
        }

        super.onResume();
    }

    private void loadOrders(IpModel serverModel, String deliveryDate, int routeId, int orderId) {
        orderViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);
        orderViewModel.init(databaseAdapter, serverModel.getServerIp(), deliveryDate, routeId, orderId);
        orderViewModel.listOfOrders().observe(this, new Observer<Resource<List<OrderModel>>>() {
            @Override
            public void onChanged(Resource<List<OrderModel>> listResource) {
                switch (listResource.status) {
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, "Error: " + listResource.errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        OrdersAdapter adapter = new OrdersAdapter(OrdersActivity.this, listResource.response);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        createFirebaseAuthentications(listResource.response);
                        listOfOrders.addAll(listResource.response);
                        Toast.makeText(OrdersActivity.this, "Size: " + listResource.response.size(), Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, "Loading: " + listResource.status, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //For now it's creating each time the user:
    private void createFirebaseAuthentications(List<OrderModel> response) {
        for (OrderModel orderAttributes : response) {
            mass = mass + Double.parseDouble(orderAttributes.getOrderMass());
            acknowledgeStockBtn.setText(orderAttributes.getDriverName() + " Please acknowledge the stock");
            DriverEmail = orderAttributes.getDriverEmail();
            DriverPassword = orderAttributes.getDriverPassword();

            mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                String user = mAuth.getCurrentUser().getUid();
                                String email = mAuth.getCurrentUser().getEmail();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference myRef = database.getReference().child("Driver").child("users").child(user).child("lat");
                                DatabaseReference myRef2 = database.getReference().child("Driver").child("users").child(user).child("lon");
                                DatabaseReference myRefEmail = database.getReference().child("Driver").child("users").child(user).child("email");
                                DatabaseReference myRefstops = database.getReference().child("Driver").child("users").child(user).child("stops");
                                DatabaseReference myRefroute = database.getReference().child("Driver").child("users").child(user).child("route");
                                DatabaseReference myRefdeldate = database.getReference().child("Driver").child("users").child(user).child("deldate");
                                DatabaseReference myRefordertype = database.getReference().child("Driver").child("users").child(user).child("ordertype");

                                myRef.setValue(Double.toString(lat));
                                myRef2.setValue(Double.toString(lon));
                                myRefEmail.setValue(email);
                                myRefstops.setValue(databaseAdapter.countSigned());
                                myRefroute.setValue(routeNames.getText().toString());
                                myRefdeldate.setValue(dDate.getText().toString());
                                myRefordertype.setValue(orderTypes.getText().toString());
                                //LatLng latLng = new LatLng(lat, lon);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            }

                            // ...
                        }
                    });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();

        coord.setText(Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude()));
        Log.d(TAG, "Location changed customer*******************************************************************************");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            Log.d(TAG, "Existing customer uid*******************************************************************************" + uid);
            //String email = user.getEmail();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference().child("Driver").child("users").child(uid).child("lat");
            DatabaseReference myRef2 = database.getReference().child("Driver").child("users").child(uid).child("lon");
            DatabaseReference myRefEmail = database.getReference().child("Driver").child("users").child(uid).child("email");
            DatabaseReference myRefstops = database.getReference().child("Driver").child("users").child(uid).child("stops");
            DatabaseReference myRefroute = database.getReference().child("Driver").child("users").child(uid).child("route");
            DatabaseReference myRefdeldate = database.getReference().child("Driver").child("users").child(uid).child("deldate");
            DatabaseReference myRefordertype = database.getReference().child("Driver").child("users").child(uid).child("ordertype");

            myRef.setValue(Double.toString(lat));
            myRef2.setValue(Double.toString(lon));
            myRefEmail.setValue(email);
            // myRefstops.setValue(dbH.countSigned());
            myRefstops.setValue(databaseAdapter.countSigned());
            myRefroute.setValue(routeNames.getText().toString());
            myRefdeldate.setValue(dDate.getText().toString());
            myRefordertype.setValue(orderTypes.getText().toString());
        } else {


            Log.e("DriverEmail**", "--------------------------------" + DriverEmail);
            Log.e("DriverPassword**", "--------------------------------" + DriverPassword);
            try {
                mAuth.signInWithEmailAndPassword(DriverEmail, DriverPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    String user = mAuth.getCurrentUser().getUid();
                                    String email = mAuth.getCurrentUser().getEmail();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    DatabaseReference myRef = database.getReference().child("Driver").child("users").child(user).child("lat");
                                    DatabaseReference myRef2 = database.getReference().child("Driver").child("users").child(user).child("lon");
                                    DatabaseReference myRefEmail = database.getReference().child("Driver").child("users").child(user).child("email");
                                    DatabaseReference myRefstops = database.getReference().child("Driver").child("users").child(user).child("stops");
                                    DatabaseReference myRefroute = database.getReference().child("Driver").child("users").child(user).child("route");
                                    DatabaseReference myRefdeldate = database.getReference().child("Driver").child("users").child(user).child("deldate");
                                    DatabaseReference myRefordertype = database.getReference().child("Driver").child("users").child(user).child("ordertype");

                                    myRef.setValue(Double.toString(lat));
                                    myRef2.setValue(Double.toString(lon));
                                    myRefEmail.setValue(email);
                                    //myRefstops.setValue(dbH.countSigned());
                                    myRefstops.setValue(databaseAdapter.countSigned());
                                    myRefroute.setValue(routeNames.getText().toString());
                                    myRefdeldate.setValue(dDate.getText().toString());
                                    myRefordertype.setValue(orderTypes.getText().toString());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                }

                                // ...
                            }
                        });
            } catch (Exception e) {

            }

        }


    }

    /**
     * Background task:
     */

    public void OrderHeaderPost() {

        //ArrayList<Orders> dealLineToUpload = dbH.getOrderHeadersNotUploaded();
        ArrayList<Orders> dealLineToUpload = new ArrayList<>();
        for (Orders orderAttributes : dealLineToUpload) {

            String strNotesDrivers = "NULL";
            String strEmailAddress = "NULL";
            String strCashSig = "NULL";
            String strStartTime = "NULL";
            String strEndTime = "NULL";
            String strTheImage = "NoImage";
            String signedBy = "NULL";
            if (orderAttributes.getstrNotesDrivers() != null && !orderAttributes.getstrNotesDrivers().isEmpty()) {
                strNotesDrivers = orderAttributes.getstrNotesDrivers();
            }
            if (orderAttributes.getstrEmailCustomer() != null && !orderAttributes.getstrEmailCustomer().isEmpty()) {
                strEmailAddress = orderAttributes.getstrEmailCustomer();
            }
            if (orderAttributes.getstrCashsignature() != null && !orderAttributes.getstrCashsignature().isEmpty()) {
                strCashSig = orderAttributes.getstrCashsignature();
            }

            if (orderAttributes.getStartTripTime() != null && !orderAttributes.getStartTripTime().isEmpty()) {
                strStartTime = orderAttributes.getStartTripTime();
            }
            if (orderAttributes.getEndTripTime() != null && !orderAttributes.getEndTripTime().isEmpty()) {
                strEndTime = orderAttributes.getEndTripTime();
            }
            if (orderAttributes.getimagestring() != null && !orderAttributes.getimagestring().isEmpty()) {
                strTheImage = orderAttributes.getimagestring();
            }
            if (orderAttributes.getstrCustomerSignedBy() != null && !orderAttributes.getstrCustomerSignedBy().isEmpty()) {
                signedBy = orderAttributes.getstrCustomerSignedBy();
            }
            Log.e("*****", "********************************note " + strEmailAddress);
            new UploadNewOrderLines(orderAttributes.getInvoiceNo(), orderAttributes.getLatitude(), orderAttributes.getLongitude(),
                    strTheImage, orderAttributes.getCashPaid(), strNotesDrivers, orderAttributes.getoffloaded(), strEmailAddress, strCashSig, strStartTime, strEndTime, orderAttributes.getDeliverySequence(), orderAttributes.getstrCoordinateStart(), signedBy, orderAttributes.getLoyalty()).execute();
        }

    }

    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {

        String orderdate;
        String ordertype;
        String route;
        String signature;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressdialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
            builder
                    .setTitle("Signature Saved ")
                    .setMessage("Thank you")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }

        public UploadNewOrderLinesDetails(String orderdate, String ordertype, String route, String signature) {
            this.orderdate = orderdate;
            this.ordertype = ordertype;
            this.route = route;
            this.signature = signature;

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            HttpPost httppost = new HttpPost(SERVERIP + "PostAcknowledgement.php");
            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("orderdate", orderdate);
                json.put("ordertype", ordertype);
                json.put("route", route);
                json.put("signature", signature);

                Log.d("JSON", json.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String results;


                }

            } catch (ClientProtocolException e) {
                Log.e("JSON", e.getMessage());
            } catch (IOException e) {
                Log.e("JSON", e.getMessage());
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
            }
            //db.close();
            return null;
        }
    }


    public class UploadNewOrderLines extends AsyncTask<Void, Void, Void> {

        String invoice;
        String lat;
        String lon;
        String image;
        String cash;
        String note;
        String offload;
        String strEmailAddress;
        String strCashSig;
        String strStartTime;
        String strEndTime;
        String delseq;
        String strCoord;
        String signedBy;
        String Loyalty;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadNewOrderLines(String invoice, String lat, String lon, String image, String cash, String note, String offload, String email, String strCashSig, String strStartTime,
                                   String strEndTime, String delseq, String strCoord, String signedBy, String Loyalty) {
            this.invoice = invoice;
            this.lat = lat;
            this.lon = lon;
            this.image = image;
            this.cash = cash;
            this.note = note;
            this.offload = offload;
            this.strEmailAddress = email;
            this.strCashSig = strCashSig;
            this.strStartTime = strStartTime;
            this.strEndTime = strEndTime;
            this.delseq = delseq;
            this.strCoord = strCoord;
            this.signedBy = signedBy;
            this.Loyalty = Loyalty;


        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            Log.e("postIP", "++++++++++++++++++++++++++++++++" + SERVERIP + "PostHeaders");
            Log.e("postIP", "++++++++++++++++++++++++++++++++ signedBy" + signedBy);
            Log.e("Loyalty", "++++++++++++++++++++++++++++++++ Loyalty" + Loyalty);
            HttpPost httppost = new HttpPost(SERVERIP + "PostHeaders");
            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("invoice", invoice);
                json.put("lat", lat);
                json.put("lon", lon);
                json.put("image", image);
                json.put("cash", cash);
                json.put("note", note);
                json.put("offload", offload);
                json.put("strEmailAddress", strEmailAddress);
                json.put("strCashSig", strCashSig);
                json.put("strEndTime", strEndTime);
                json.put("strStartTime", strStartTime);
                json.put("delseq", delseq);
                json.put("strCoord", strCoord);
                json.put("signedBy", signedBy);
                json.put("Loyalty", Loyalty);


                Log.e("Loyalty", "++++++++++++++++++++++++++++++++JSON Loyalty " + Loyalty);
                Log.d("JSON", json.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is HEADERS**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID, strPartNumber;
                    ID = BoardDetails.getString("InvoiceNo");

                    //dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + ID + "'");
                    databaseAdapter.updateOrdersHeaderByInvoice(ID);
                }

            } catch (ClientProtocolException e) {
                Log.e("JSON", e.getMessage());
            } catch (IOException e) {
                Log.e("JSON", e.getMessage());
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
            }
            // db.close();
            return null;
        }
    }
}