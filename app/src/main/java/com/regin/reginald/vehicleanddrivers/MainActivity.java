package com.regin.reginald.vehicleanddrivers;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
//import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OrderTypes;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.Routes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.model.WareHouses;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.CustomClickListener;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.CustomLongClickListener;
import com.regin.reginald.vehicleanddrivers.Aariyan.Maps.RoutePlanActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Maps.TestMaps;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.NetworkingFeedback;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.PostNetworking;
import com.regin.reginald.vehicleanddrivers.PrinterControl.BixolonPrinter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        CustomClickListener, CustomLongClickListener {


    @Override
    public void longClick(int position, String lat, String lng, String sequence, String customerName) {
        Intent i = new Intent(MainActivity.this, MyMapActivity.class);
        i.putExtra("Lat", lat);
        i.putExtra("Lon", lng);
        i.putExtra("seq", sequence);
        i.putExtra("custName", customerName);
        startActivity(i);
    }

    @Override
    public void normalClick(String invoiceNo, String cash, String threshold, String storeName) {
        Intent b;
        if (threshold.equals("0")) {
            b = new Intent(MainActivity.this, InvoiceDetails.class);
            UpdateDeliverySeq();
            b.putExtra("deldate", deliverdate.getText().toString());
            b.putExtra("routes", routename.getText().toString());
            b.putExtra("ordertype", ordertype.getText().toString());
            b.putExtra("invoiceno", invoiceNo);
            b.putExtra("cash", cash);
        } else {
            b = new Intent(MainActivity.this, CratesActivity.class);
            b.putExtra("invoiceno", invoiceNo);
            b.putExtra("threshold", threshold);
            b.putExtra("storename", storeName);
            b.putExtra("deldate", deliverdate.getText().toString());
            b.putExtra("routes", routename.getText().toString());
            b.putExtra("ordertype", ordertype.getText().toString());
        }
        startActivity(b);
    }

    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;
        String ItemString7;
        String ItemString8;
        String ItemString9;


        Item(String t, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;
            ItemString7 = t7;
            ItemString8 = t8;
            ItemString9 = t9;
        }
    }

    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        CheckBox text6;

        public float lastTouchedX;
        public float lastTouchedY;

    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                //rowView = inflater.inflate(R.layout.orders_rows, null);
                rowView = inflater.inflate(R.layout.single_order_rows, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = rowView.findViewById(R.id.storename);
                viewHolder.text2 = rowView.findViewById(R.id.address);
                viewHolder.text3 = rowView.findViewById(R.id.orderid);
                viewHolder.text4 = rowView.findViewById(R.id.del);
                viewHolder.text6 = rowView.findViewById(R.id.offload);
                viewHolder.text4.setBackgroundColor(Color.WHITE);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);
            holder.text4.setText(list.get(position).ItemString4);


            if ((list.get(position).ItemString4).equals("1")) {
                holder.text6.setChecked(true);
                holder.text4.setBackgroundColor(Color.WHITE);

                // holder.text6.setBackgroundColor(Color.rgb(74, 144, 224));
            } else {
                holder.text6.setChecked(false);
                holder.text4.setText("");
            }

            holder.text6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    // States _state = (States) cb.getTag();
//                    Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
                    checkAndUncheck(list.get(position).ItemString6, cb.isChecked(), list.get(position).ItemString8);
                    //_state.setSelected(cb.isChecked());
                }
            });

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
       /* @Override
        public boolean onTouch(View v, MotionEvent event) {
            ViewHolder vh = (ViewHolder) v.getTag();

            vh.lastTouchedX = event.getX();
            vh.lastTouchedY = event.getY();

            return false;
        }*/
    }

    List<Item> items1, lineinfo;
    List<Data> listdata;
    ItemsListAdapter myItemsListAdapter;

    //Button get, closelines, btndoneoffloading, donelineinfo, close_line_info, btn_submit_ackn, continue_without, starttrip_dialog;
    TextView get, closelines, btndoneoffloading, donelineinfo, close_line_info, btn_submit_ackn, continue_without, starttrip_dialog;
    Spinner route, ordertypes;
    EditText dte_from, qtyordered, notecomment, kmstart_dilog, timestart;
    CustomListView _orderdlist;
    ListView _orderdlistlines;
    CheckBox accept, offload;
    private SignaturePad mSignaturePad;
    private static BixolonPrinter bxlPrinter = null;
    TextView product_name, pastelcode, priceline, commentline, deliverdate, ordertype, routename, calcr_plan, coord, passwd, demail, not_uploade;
    private SignaturePad ack_sign;

    private TextView start_trip, endtrip, acknowledge_stock;
    private ImageView sort_order;

    private int year;
    private int month;
    private int day;
    private String currentDate;
    private static final int DATE_DIALOG_ID = 1;
    String tomorrowDate, updatedCoordinates;
    //FirebaseFirestore db;
    //DocumentReference docRef;
    //So-ca = http://169.255.77.160:8181/DriverGas/
    int len = 0;
    //String customerOrders, SERVERIP = "http://linxsystems3.dedicated.co.za:8881/driver/",ordertypeidreturned,routeidreturned;= "http://192.168.0.18:8181/driver/"
    // http://so-ca.ddns.net:8179/driver/      169.255.77.160:8181
    String customerOrders, SERVERIP, DriverEmail, DriverPassword, kmstart, kmend;
    private int ordertypeidreturned, routeidreturned;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    final OrderNotUploadedActivity postHeaders = new OrderNotUploadedActivity();
    ProgressDialog progressDoalog;
    //private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    double mass;
    double lat = -33.966145;
    double lon = 22.466218, custlat, custlon;
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

    Handler handler = new Handler();
    Runnable runnableUpload;
    int delayUpload = 10000;
    boolean hasCratesModule = false;

    DatabaseAdapter databaseAdapter;

    private RecyclerView recyclerView;

    private String freezeTemp = "3.0";

    private TextView routePlan;

    private int orderId, routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_new);
        AndroidNetworking.initialize(getApplicationContext());


        dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderLines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, OrderID INTEGER , ProductId INTEGER, PastelCode TEXT,PastelDescription TEXT,Qty DOUBLE,Price Double,OrderDetailId INTEGER,Comment TEXT,offLoadComment TEXT,returnQty DOUBLE,Uploaded boolean DEFAULT 1,blnoffloaded INTEGER,strCustomerReason TEXT,blnTruckchecked INTEGER,WareHouseName TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderHeaders (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,StoreName TEXT, OrderID INTEGER ,CustomerPastelCode TEXT,DeliveryDate TEXT, InvoiceNo TEXT, DeliveryAddress TEXT,Latitude DOUBLE,Longitude DOUBLE,OrderValueExc Double ,OrderValueInc Double ,User TEXT,OrderMass DOUBLE,offloaded boolean DEFAULT 0,Uploaded boolean DEFAULT 1,imagestring TEXT,CashPaid DOUBLE,strNotesDrivers TEXT,StartTripTime TEXT,EndTripTime TEXT,DeliverySeq INTEGER,strCoordinateStart TEXT,DriverName TEXT,DriverEmail TEXT,DriverPassword TEXT,strCustomerSignedBy TEXT,PaymentType TEXT,Loyalty Text)");

       /* Intent intent = new Intent(MainActivity.this, OrderService.class);
        startService(intent);*/
        //mDatabaseHelper = DatabaseHelper.getHelper(this);
        //   final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/";
        //db = openOrCreateDatabase("LinxDriversOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db = openOrCreateDatabase("drivers_app.db", Context.MODE_PRIVATE, null);
        ArrayList<SettingsModel> settIP = dbH.getSettings();

        for (SettingsModel orderAttributes : settIP) {
            SERVERIP = orderAttributes.getstrServerIp();
        }

//        databaseAdapter = new DatabaseAdapter(this);
//        List<IpModel> list = databaseAdapter.getServerIpModel();
//        if (list.size() > 0) {
//            SERVERIP = list.get(0).getServerIp();
//        } else {
//            SERVERIP = "";
//        }

        initUI();

    }

    @Override
    protected void onResume() {
        if (dbH.checkiflinesuploaded() > 0) {
            //
            if (isInternetAvailable()) {
                //Toast.makeText(this, "You Are Connected ", Toast.LENGTH_SHORT).show();
                //OrderHeaderPost(); //TODO : REPLACED:
                new PostNetworking(SERVERIP).orderHeaderPost(dbH);
            } else {
                Toast.makeText(MainActivity.this, "Please turn on the internet!", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(MainActivity.this, "Nothing to post!", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    private void initUI() {
        start_trip = findViewById(R.id.tripInfoBtn);
        sort_order = findViewById(R.id.backBtn);
        endtrip = findViewById(R.id.endTripBtn);
        deliverdate = findViewById(R.id.deliverDate);
        ordertype = findViewById(R.id.orderType);
        routename = findViewById(R.id.routeName);
        calcr_plan = findViewById(R.id.calcr_plan);
        coord = findViewById(R.id.coord);
        //demail =  findViewById(R.id.demail);
        //passwd = (TextView) findViewById(R.id.passwd);
        not_uploade = findViewById(R.id.uploadedCount);
        // dte_from = (EditText) findViewById(R.id.datetime);
        _orderdlist = findViewById(R.id._orderdlistlines);
        acknowledge_stock = findViewById(R.id.acknowledgeBtn);

        routePlan = findViewById(R.id.routePlan);
        routePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RoutePlanActivity.class));
                //startActivity(new Intent(MainActivity.this, TestMaps.class));
            }
        });

        /**
         *
         */
//        recyclerView = findViewById(R.id.orderRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /**
         *
         */

//        if (dbH.checkiflinesuploaded() > 0) {
//            //
//            if (isInternetAvailable()) {
//                //Toast.makeText(this, "You Are Connected ", Toast.LENGTH_SHORT).show();
//                OrderHeaderPost();
//            } else {
//                Toast.makeText(MainActivity.this, "Please turn on the internet!", Toast.LENGTH_SHORT).show();
//            }
//        }

        calcr_plan.setVisibility(View.INVISIBLE);
        Intent returndata = getIntent();

//        if (getIntent() != null) {
//            Toast.makeText(MainActivity.this, "HHH: "+getIntent().getStringExtra("deldate"), Toast.LENGTH_SHORT).show();
//        }

        deliverdate.setText(returndata.getStringExtra("deldate"));
        ordertype.setText(returndata.getStringExtra("ordertype"));
        routename.setText(returndata.getStringExtra("routes"));
        orderId = returndata.getIntExtra("orderId", 0);
        routeId = returndata.getIntExtra("routeId", 0);
        if (getIntent().hasExtra("temp")) {
            freezeTemp = getIntent().getStringExtra("temp");
        }

        ordertypeidreturned = orderId;
        routeidreturned = routeId;
        kmstart = returndata.getStringExtra("edtkm_start");
        kmend = returndata.getStringExtra("edt_km_end");


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

        sort_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDeliverySeq();
                Intent i = new Intent(MainActivity.this, LandingPage.class);
                startActivity(i);
            }
        });
        Log.e("hasdata", "***********************" + dbH.hasData());

        // TODO: it's not done yet.
        if (dbH.hasData()) {
            List<Orders> oH = dbH.returnOrderHeaders();
            items1 = new ArrayList<Item>();
            listdata = new ArrayList<Data>();

            for (Orders orderAttributes : oH) {
               /* Log.e("itemsval","***"+orderAttributes.getStoreName()+"****"+orderAttributes.getDeliveryAddress()+"*******"+orderAttributes.getInvoiceNo()+"-------"+orderAttributes.getOrderMass());
                Item item = new Item(orderAttributes.getStoreName(), orderAttributes.getDeliveryAddress(),orderAttributes.getInvoiceNo(),
                        orderAttributes.getoffloaded(),"1","Header",orderAttributes.getCashPaid(),orderAttributes.getoffloaded());
                items1.add(item);*/


                Data listItem = new Data((orderAttributes.getStoreName()).trim(), (orderAttributes.getDeliveryAddress()).trim(), (orderAttributes.getInvoiceNo()).trim(),
                        orderAttributes.getoffloaded(), "1", "Header", orderAttributes.getCashPaid(), orderAttributes.getoffloaded(), orderAttributes.getLatitude(), orderAttributes.getLongitude(), Integer.toString(orderAttributes.getDeliverySeq()),
                        orderAttributes.getThreshold());
                listdata.add(listItem);
                mass = mass + Double.parseDouble(orderAttributes.getOrderMass());
                acknowledge_stock.setText(orderAttributes.getDriverName() + " Please acknowledge the stock");
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
                                    myRefstops.setValue(dbH.countSigned());
                                    myRefroute.setValue(routename.getText());
                                    myRefdeldate.setValue(deliverdate.getText());
                                    myRefordertype.setValue(ordertype.getText());
                                    //LatLng latLng = new LatLng(lat, lon);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                }

                                // ...
                            }
                        });
                //orderAttributes.getPrice()
                //orderAttributes.getPrice()
            }
            //myItemsListAdapter = new ItemsListAdapter(MainActivity.this, items1);
            // _orderdlist.setAdapter(myItemsListAdapter);

            Adapter adapter = new Adapter(this, listdata, oH, new Adapter.Listener() {
                @Override
                public void onGrab(int position, TableLayout row) {
//                    _orderdlist.onGrab(position, row);
                }
            }, this, this);
            _orderdlist.setAdapter(adapter);
            _orderdlist.setListener(new CustomListView.Listener() {
                @Override
                public void swapElements(int indexOne, int indexTwo) {
                    Data temp = listdata.get(indexOne);
                    listdata.set(indexOne, listdata.get(indexTwo));
                    listdata.set(indexTwo, temp);
                }
            });
        } else {
            not_uploade.setText("");
            startProgress("Syncing");
//            ArrayList<Routes> routesdata = dbH.multiId("Select * from Routes where RouteName ='" + routename.getText().toString() + "'");
//            ArrayList<Routes> ordertyp = dbH.multiId("Select * from OrderTypes where OrderType ='" + ordertype.getText().toString() + "'");
//            for (Routes orderAttributes4 : routesdata) {
//                routeidreturned = orderAttributes4.getRouteName();
//            }
//
//            for (Routes orderAttributes4 : ordertyp) {
//                ordertypeidreturned = orderAttributes4.getRouteName();
//            }

//            new getOrderLines().execute(SERVERIP + "OrderLines.php?OrderType="
//                    + orderId + "&Route="
//                    + routeId + "&DeliveryDate=" + deliverdate.getText().toString());

            Log.e("HEADERS_POST_TEST", "" + SERVERIP + "OrderHeaders.php?OrderType=" + ordertypeidreturned + "&Route=" + routeidreturned + "&DeliveryDate=" + deliverdate.getText().toString());
            //new getOrderHeaders().execute(SERVERIP + "OrderHeaders.php?OrderType=" + ordertypeidreturned + "&Route=" + routeidreturned + "&DeliveryDate=" + deliverdate.getText().toString());
            new getOrderHeaders().execute(SERVERIP + "OrderHeaders.php?OrderType="
                    + orderId
                    + "&Route=" + routeId + "&DeliveryDate=" + deliverdate.getText().toString());


        }


        start_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start_trip.setBackgroundColor(Color.BLUE);
                UpdateDeliverySeq();
                String[] finaldistanceAndTime = distanceAndTime();

                double t = DriversHours(mass, 0, 1, Double.parseDouble(finaldistanceAndTime[0]));
                BigDecimal result;
                result = round((float) t, 3);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view;
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.trip_info_dialog, null);
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
                        Toast.makeText(MainActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
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
                        dbH.updateDeals("UPDATE OrderHeaders set StartTripTime='" + tomorrowDate + "', strCoordinateStart='" + coordinates + "'");
                    }
                });
                builder.setView(view);

                builder.show();
            }
        });
        endtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDeliverySeq();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle("FINISH TRIP ")
                        .setMessage(" Are you sure? NB This will discard the saved filters.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbH.updateDeals("delete from Filters");
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_YEAR, 0);
                                Date tomorrow = calendar.getTime();

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String tomorrowDate = dateFormat.format(tomorrow);
                                dbH.updateDeals("UPDATE OrderHeaders set Uploaded=0 , EndTripTime='" + tomorrowDate + "'");
                                Intent b = new Intent(MainActivity.this, LandingPage.class);
                                startActivity(b);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        /*
        On Click here:
         */

        _orderdlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////
        try {
            _orderdlist.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Data selectedItem = (Data) (parent.getItemAtPosition(position));

               /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle("Are you checking in?")
                        .setMessage("")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("CHECK-IN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }})

                        .setNegativeButton("VIEW INVOICE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }})  .show();*/
                    String threshold = selectedItem.ItemString12;
                    Log.e("threshold", "++++++" + threshold);

                    if (threshold.equals("0")) {
                        Intent b = new Intent(MainActivity.this, InvoiceDetails.class);
                        UpdateDeliverySeq();
                        b.putExtra("deldate", deliverdate.getText().toString());
                        b.putExtra("routes", routename.getText().toString());
                        b.putExtra("ordertype", ordertype.getText().toString());
                        b.putExtra("invoiceno", selectedItem.ItemString3);
                        b.putExtra("cash", selectedItem.ItemString7);
                        startActivity(b);
                    } else {
                        Intent b = new Intent(MainActivity.this, CratesActivity.class);
                        b.putExtra("invoiceno", selectedItem.ItemString3);
                        b.putExtra("threshold", selectedItem.ItemString12);
                        b.putExtra("storename", selectedItem.ItemString);
                        b.putExtra("deldate", deliverdate.getText().toString());
                        b.putExtra("routes", routename.getText().toString());
                        b.putExtra("ordertype", ordertype.getText().toString());
                        startActivity(b);
                    }


                }
            });
            _orderdlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Data selectedItem = (Data) (parent.getItemAtPosition(position));
                    //String lat = selectedItem.ItemString9;
                    //String lon = selectedItem.ItemString10;
                    //Toast.makeText(MainActivity.this, "Called", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, MyMapActivity.class);
                    i.putExtra("Lat", selectedItem.ItemString9);
                    i.putExtra("Lon", selectedItem.ItemString10);
                    i.putExtra("seq", selectedItem.ItemString11);
//                    i.putExtra("currentLat", lat);
//                    i.putExtra("currentLon", lon);
                    i.putExtra("custName", selectedItem.ItemString);
                    startActivity(i);

                    return true;
                }
            });
        } catch (Exception e) {

        }

        acknowledge_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Light_NoTitleBar);
                dialog.setContentView(R.layout.stock_acknowledge);

      /*  TextView textView = new TextView(context);
        textView.setText();*/
                dialog.setTitle("Please Type in the Cash");
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                btn_submit_ackn = dialog.findViewById(R.id.btn_submit_ackn);
                ack_sign = dialog.findViewById(R.id.ack_sign);
                ImageView undoSignature = dialog.findViewById(R.id.undoSignature);
                undoSignature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                            Toast.makeText(MainActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        not_uploade.setText(dbH.OrdersNotPostedToTheOffice());
        handler.postDelayed(runnableUpload = new Runnable() {
            public void run() {
                //Toast.makeText(MainActivity.this, "You Are Connected ", Toast.LENGTH_SHORT).show();
                not_uploade.setText(dbH.OrdersNotPostedToTheOffice());
            }
        }, delayUpload);
        (new Thread(runnable)).start();


    }

    public boolean addSignatureJpg(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, invoiceNo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public void UpdateDeliverySeq() {

        for (int i = 0; i < _orderdlist.getCount(); i++) {

            //yList.getAdapter().getView(i, null, null);
            View v = _orderdlist.getAdapter().getView(i, null, null);

            TextView et = (TextView) v.findViewById(R.id.orderid);

            int newSeq = i + 1;
            dbH.updateDeals("Update OrderHeaders set DeliverySeq=" + newSeq + " Where InvoiceNo='" + et.getText().toString() + "'");
        }


    }

    /**
     * The Distance is in KM
     * Time is in Minutes
     *
     * @return
     */

    public String[] distanceAndTime() {
        List<Orders> orderheader = dbH.returnOrderHeaders();
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
        start_trip.setBackgroundColor(Color.BLUE);
        String coordinate1 = "";
        String coordinate2 = "";
        int k = 0;

        for (int i = 0; i < stringArray.length; i++) {


            if (i != (stringArray.length - 1)) {
                coordinate1 = stringArray[k];
                coordinate2 = stringArray[i + 1];
                Log.e("*****", "******************************** location" + coordinate1 + "***" + coordinate2);
                String[] res = reurndistancetime(coordinate1, coordinate2);

                if (res[0] == null || res[0].equals("NULL")) {
                    // Log.e("*****","******************************** cooooooooooooooooooooord"+res[0]);

                    totdistance = totdistance + Double.parseDouble("0");
                    totduration = totduration + Double.parseDouble("0");
                } else {
                    totdistance = totdistance + Double.parseDouble(res[0]);
                    totduration = totduration + Double.parseDouble(res[1]);
                }

                //Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
                k = i + 1;
                Log.e("*****", "******************************** cooooooooooooooooooooord" + totdistance);

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

    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("Caution");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {

            year = i;
            month = j;
            day = k;
            updateDisplay();
            dte_from.setText(currentDate);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, myDateSetListener, year, month,
                        day);

        }
        return null;
    }

    private void updateDisplay() {
        currentDate = new StringBuilder().append(year).append("-")
                .append(month + 1).append("-").append(day).toString();

        Log.i("DATE", currentDate);
    }

    public double DriversHours(double tonnage, double travelTimeInMinutes, int numberOfStops, double sumdistance) {
        //100 is hardcoded for testing purpose

        // return sumdistance +(numberOfStops * 5 )+((tonnage/2)/5);
        int speedIs1KmMinute = 80;
        double estimatedDriveTimeInMinutes = sumdistance / speedIs1KmMinute;


        //return ((tonnage*0.15) + estimatedDriveTimeInMinutes)/60.00;

        return ((tonnage / 6) + sumdistance) / 60;
    }

    public static String GET(String urlp) {

        String movieJsonStr = "";
        String result = "";
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        URL url;

        try {
            url = new URL(urlp);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Initialize a new string buffer object
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            // Loop through the lines
            while ((line = bufferedReader.readLine()) != null) {
                // Append the current line to string buffer
                stringBuffer.append(line);
            }

            movieJsonStr = stringBuffer.toString();

        } catch (Throwable e) {
            Log.e("backgroundtask", "EXCEPTION", e);
        } finally {
            connection.disconnect();
            try {


                if (bufferedReader != null)
                    bufferedReader.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.e("READER.CLOSE()", e.toString());
            }
        }

        try {
            result = movieJsonStr;
        } catch (Throwable e) {
            Log.e("BACKGROUNDTASK", "EXCEPTION FROM jsonParse()", e);
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class getOrderTypes extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                try {
                    dbH.updateDeals("DROP TABLE IF EXISTS OrderTypes");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderTypes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, OrderTypeId INTEGER , OrderType TEXT)");

                    List<OrderTypes> products = new Gson().fromJson(customerOrders, new TypeToken<List<OrderTypes>>() {
                    }.getType());
                    int i = 1;
                    for (OrderTypes product : products) {
                        product.setId(i);
                        i++;
                    }

                    String productListString = new Gson().toJson(
                            products,
                            new TypeToken<ArrayList<OrderTypes>>() {
                            }.getType());

                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(OrderTypes.class, product_json);
                    //readDatabaseProducts();
                    //No need
                    Log.e("**ql*", "done sync");
                    ArrayList<OrderTypes> ordertype = dbH.getOrderType();

                    List<String> labels = new ArrayList<String>();
                    for (OrderTypes orderAttributes4 : ordertype) {
                        labels.add(orderAttributes4.getOrderType());
                    }
                    ArrayAdapter<String> ordertypeA =
                            new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, labels);
                    ordertypeA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    ordertypes.setAdapter(ordertypeA);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getRoutes extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                try {

                    dbH.updateDeals("DROP TABLE IF EXISTS Routes");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS Routes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, RouteId INTEGER , RouteName TEXT)");

                    List<Routes> products = new Gson().fromJson(customerOrders, new TypeToken<List<Routes>>() {
                    }.getType());
                    int i = 1;
                    for (Routes product : products) {
                        product.setId(i);
                        i++;
                    }

                    String productListString = new Gson().toJson(
                            products,
                            new TypeToken<ArrayList<Routes>>() {
                            }.getType());

                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(Routes.class, product_json);
                    //readDatabaseProducts();
                    //No Need
                    Log.e("**ql*", "done sync");

                    List<RouteModel> routesdata = dbH.getRoutes();

                    List<String> labels = new ArrayList<String>();
                    for (RouteModel orderAttributes4 : routesdata) {
                        labels.add(orderAttributes4.getRouteName());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, labels);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    route.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Working here.
     */
    ///////////////////////////////////////////////////////  Here It's crashing now /////////////////////////////////////////////////////////////////////////
    private class getOrderHeaders extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                try {
                    dbH.updateDeals("DROP TABLE IF EXISTS OrderHeaders");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderHeaders (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,StoreName TEXT, OrderID INTEGER ,CustomerPastelCode TEXT,DeliveryDate TEXT, InvoiceNo TEXT, DeliveryAddress TEXT,Latitude DOUBLE,Longitude DOUBLE,OrderValueExc Double ,OrderValueInc Double ,User TEXT,OrderMass DOUBLE,offloaded boolean DEFAULT 0,Uploaded boolean DEFAULT 1,imagestring TEXT,CashPaid DOUBLE,strNotesDrivers TEXT,strEmailCustomer TEXT,strCashsignature TEXT,InvTotIncl TEXT,StartTripTime TEXT,EndTripTime TEXT,DeliverySeq INTEGER,strCoordinateStart TEXT,DriverName TEXT,DriverEmail TEXT,DriverPassword TEXT,strCustomerSignedBy TEXT,PaymentType TEXT,Loyalty Text,Threshold TEXT)");

                    List<Orders> products = new Gson().fromJson(customerOrders, new TypeToken<List<Orders>>() {
                    }.getType());
//                    int i = 1;
//                    for (Orders product : products) {
//                        product.setId(i);
//                        i++;
//                    }
//
//                    Log.d("ORDERS_HEADERS_VALUE", ""+customerOrders);
//
//                    String productListString = new Gson().toJson(
//                            products,
//                            new TypeToken<ArrayList<Orders>>() {
//                            }.getType());
//
//                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(Orders.class, product_json);
                    //readDatabaseProducts();
                    new NetworkingFeedback(dbH).insertOrderHeadersIntoSQLiteDatabase(products);
                    Log.e("**ql*", "done sync");

                    progressDoalog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please carry on ,DATA found")
                            .setCancelable(false)
                            .setPositiveButton("Get Details", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startProgress("Syncing Items");
                                    List<Orders> oH = dbH.returnOrderHeaders();
                                    //Toast.makeText(MainActivity.this, "SIZE: " + oH.size(), Toast.LENGTH_SHORT).show();
                                    listdata = new ArrayList<Data>();

                                    for (Orders orderAttributes : oH) {
                                        Data listItem = new Data(orderAttributes.getStoreName(), orderAttributes.getDeliveryAddress(), orderAttributes.getInvoiceNo(),
                                                orderAttributes.getoffloaded(), "1", "Header", orderAttributes.getCashPaid(), orderAttributes.getoffloaded(),
                                                orderAttributes.getLatitude(), orderAttributes.getLongitude(),
                                                Integer.toString(orderAttributes.getDeliverySeq())
                                                , orderAttributes.getThreshold());
                                        listdata.add(listItem);

                                        mass = mass + Double.parseDouble(orderAttributes.getOrderMass());
                                        acknowledge_stock.setText(orderAttributes.getDriverName() + " Please acknowledge the stock");
                                        DriverEmail = orderAttributes.getDriverEmail();
                                        DriverPassword = orderAttributes.getDriverPassword();
                                        try {


                                            mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword)
                                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                // Sign in success, update UI with the signed-in user's information
                                                                Log.d(TAG, "createUserWithEmail:success");
                                                                try {
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
                                                                    myRefstops.setValue(dbH.countSigned());
                                                                    myRefroute.setValue(routename.getText());
                                                                    myRefdeldate.setValue(deliverdate.getText());
                                                                    myRefordertype.setValue(ordertype.getText());
                                                                } catch (Exception e) {
                                                                }
                                                                //LatLng latLng = new LatLng(lat, lon);
                                                                //updateUI(user);
                                                            } else {
                                                                // If sign in fails, display a message to the user.
                                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                                            }

                                                            // ...
                                                        }
                                                    });

                                        } catch (Exception e) {

                                        }
                                        //orderAttributes.getPrice()
                                    }
                                    Adapter adapter = new Adapter(MainActivity.this, listdata, oH, new Adapter.Listener() {
                                        @Override
                                        public void onGrab(int position, TableLayout row) {
                                            _orderdlist.onGrab(position, row);
                                        }
                                    }, MainActivity.this, MainActivity.this);

                                    _orderdlist.setAdapter(adapter);
                                    _orderdlist.setListener(new CustomListView.Listener() {
                                        @Override
                                        public void swapElements(int indexOne, int indexTwo) {
                                            Data temp = listdata.get(indexOne);
                                            listdata.set(indexOne, listdata.get(indexTwo));
                                            listdata.set(indexTwo, temp);
                                        }
                                    });

                                    /**
                                     * Removed from there
                                     */
                                    //new getOrderLines().execute(SERVERIP + "OrderLines.php?OrderType=" + ordertypeidreturned + "&Route=" + routeidreturned + "&DeliveryDate=" + deliverdate.getText().toString());
                                    //dbH.updateDeals("Update TimeSync set lastTimeSync='"+timeSync()+"' where TableName='PriceLists')");

                                    new getOrderLines().execute(SERVERIP + "OrderLines.php?OrderType="
                                            + orderId + "&Route="
                                            + routeId + "&DeliveryDate=" + deliverdate.getText().toString());
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getOrderLines extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Order Lines Inserting", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                try {
                    dbH.updateDeals("DROP TABLE IF EXISTS OrderLines");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderLines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, OrderID INTEGER , ProductId INTEGER, PastelCode TEXT,PastelDescription TEXT,Qty DOUBLE,Price Double,OrderDetailId INTEGER,Comment TEXT,offLoadComment TEXT,returnQty DOUBLE,Uploaded boolean DEFAULT 1,blnoffloaded INTEGER,strCustomerReason TEXT,blnTruckchecked INTEGER,WareHouseName TEXT)");

                    List<OrderLines> products = new Gson().fromJson(customerOrders, new TypeToken<List<OrderLines>>() {
                    }.getType());
//                    int i = 1;
//                    for (OrderLines product : products) {
//                        product.setId(i);
//                        i++;
//                    }
//
//                    String productListString = new Gson().toJson(
//                            products,
//                            new TypeToken<ArrayList<OrderLines>>() {
//                            }.getType());
//
//                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(OrderLines.class, product_json);
                    //readDatabaseProducts();
                    Log.e("**ql*", "done sync");

                    new NetworkingFeedback(dbH).insertNewlyOrderLinesIntoSQLiteDatabase(products);
                    progressDoalog.dismiss();

                    //  final AlertDialog dialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
                    final Dialog dialogBuilder = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                    dialogBuilder.setContentView(R.layout.startdialog);
                    dialogBuilder.setTitle("Order History");
                    dialogBuilder.setCanceledOnTouchOutside(false);
                    dialogBuilder.setCancelable(false);
                    kmstart_dilog = (EditText) dialogBuilder.findViewById(R.id.kmstart);
                    timestart = (EditText) dialogBuilder.findViewById(R.id.timestart);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
                    String tm = sdf.format(c.getTime());
                    timestart.setText(tm);

                    starttrip_dialog = dialogBuilder.findViewById(R.id.starttrip_dialog);
                    continue_without = dialogBuilder.findViewById(R.id.continue_without);

                    starttrip_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String kmout = kmstart_dilog.getText().toString();
                            if (kmout.length() < 1) {
                                Toast.makeText(getApplicationContext(), "KM Out is empty", Toast.LENGTH_LONG).show();
                            } else {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                new getWareHouses().execute(SERVERIP + "WareHousesNew.php");
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
                                String tm = sdf.format(c.getTime());

                                timestart.setText(tm);

                                dbH.updateDeals("DROP TABLE IF EXISTS Filters");
                                dbH.updateDeals("CREATE TABLE IF NOT EXISTS Filters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,RouteName TEXT, OrderType TEXT ,DelDate TEXT)");
                                dbH.updateDeals("Insert into Filters(RouteName,OrderType,DelDate) values ('" + routename.getText().toString() + "','" + ordertype.getText().toString() + "','" + deliverdate.getText().toString() + "')");
                                dbH.updateDeals("Insert into TripHeader(RouteName,OrderType,DelDate,dtmCreated,KmStart) values ('" + routename.getText().toString() + "','" + ordertype.getText().toString() + "','" + deliverdate.getText().toString() + "','" + tm + "','" + kmout + "')");

                                dialogBuilder.dismiss();
                            }
                        }
                    });
                    continue_without.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // DO SOMETHINGS
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Your trip info has not been created, you can GET again to redo this step.")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //
                                            Log.e("test", "***" + SERVERIP + "WareHousesNew.php");
                                            new getWareHouses().execute(SERVERIP + "WareHousesNew.php");
                                            dbH.updateDeals("DROP TABLE IF EXISTS Filters");
                                            dbH.updateDeals("CREATE TABLE IF NOT EXISTS Filters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,RouteName TEXT, OrderType TEXT ,DelDate TEXT)");
                                            dbH.updateDeals("Insert into Filters(RouteName,OrderType,DelDate) values ('" + routename.getText().toString() + "','" + ordertype.getText().toString() + "','" + deliverdate.getText().toString() + "')");
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            dialogBuilder.dismiss();
                        }
                    });

                    // dialogBuilder.setView(dialogView);
                    dialogBuilder.show();


                   /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Would you like to start this trip?")
                            .setCancelable(false)
                            .setPositiveButton("Finished", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //
                                    Log.e("test","***"+SERVERIP + "WareHouses.php");
                                    new getWareHouses().execute(SERVERIP + "WareHouses.php");
                                    dbH.updateDeals("DROP TABLE IF EXISTS Filters");
                                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS Filters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,RouteName TEXT, OrderType TEXT ,DelDate TEXT)");
                                    dbH.updateDeals("Insert into Filters(RouteName,OrderType,DelDate) values ('"+routename.getText().toString()+"','"+ordertype.getText().toString()+"','"+deliverdate.getText().toString()+"')" );
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getWareHouses extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();

            if (len > 0) {
                try {
                    JSONObject data = new JSONObject(result);
                    JSONArray ExtraProducts = data.getJSONArray("Products");
                    JSONArray customerOrders = data.getJSONArray("warehouse");
                    Log.e("ExtraProducts***t", "ExtraProducts**************" + result);

                    dbH.updateDeals("DROP TABLE IF EXISTS WareHouses");
                    dbH.updateDeals("DROP TABLE IF EXISTS ExtraProducts");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS WareHouses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,WareHouse TEXT, WareHouseId Integer)");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS ExtraProducts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ProductId INTEGER,ProductCode TEXT,ProductName TEXT);");

                    for (int j = 0; j < ExtraProducts.length(); ++j) {

                        JSONObject BoardDetails = ExtraProducts.getJSONObject(j);

                        ContentValues values = new ContentValues();

                        values.put("ProductId", BoardDetails.getInt("intProductId"));
                        //values.put("ProductId", BoardDetails.getString("intProductId"));
                        values.put("ProductCode", BoardDetails.getString("strProductCode"));
                        values.put("ProductName", BoardDetails.getString("strProductName"));


                        db.insert("ExtraProducts", null, values);
                    }

                    List<WareHouses> products = new Gson().fromJson(customerOrders.toString(), new TypeToken<List<WareHouses>>() {
                    }.getType());
//                    int i = 1;
//                    for (WareHouses product : products) {
//                        product.setId(i);
//                        i++;
//                    }
//
//                    String productListString = new Gson().toJson(
//                            products,
//                            new TypeToken<ArrayList<WareHouses>>() {
//                            }.getType());
//
//                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(WareHouses.class, product_json);
                    new NetworkingFeedback(dbH).insertWareHousesIntoSQLiteDatabase(products);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        prevlat = lat1;
        prevlon = long1;
        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
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
        dbH.updateDeals("Update OrderHeaders set imagestring='" + s + "' where InvoiceNo ='" + InvoiceNo + "'");
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

        new UploadNewOrderLinesDetails(deliverdate.getText().toString(), "" + ordertypeidreturned, "" + routeidreturned, s).execute();

        // Log.e("********","***************"+s);
        //Log.e("********","***************InvoiceNo----"+InvoiceNo);
        //stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, invoiceNo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BixolonPrinter getPrinterInstance() {
        return bxlPrinter;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        MainActivity.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String name;
        double lati;
        double longi;
        String dateTab;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadImage(Bitmap image, String name, double lati, double longi, String dateTab) {
            this.image = image;
            this.name = name;
            this.lati = lati;
            this.longi = longi;
            this.dateTab = dateTab;
        }


        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVERIP + "savepicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {

            }
            return null;

        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);

            return httpRequestParams;
        }
    }

    public void checkAndUncheck(String tableName, boolean status, String detaild) {
        if (tableName.equals("Lines")) {
            Log.e("sql", "checkbox*******" + "Update OrderLines set blnoffloaded=" + status + " Where OrderDetailId=" + detaild);

            if (status) {
                dbH.updateDeals("Update OrderLines set blnoffloaded=1 Where OrderDetailId=" + detaild);
            } else {
                dbH.updateDeals("Update OrderLines set blnoffloaded=0 Where OrderDetailId=" + detaild);
            }

        }

    }

    @Override
    public void onConnected(Bundle bundle) {
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

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean removeItemToList(List<Item> l, Item it) {
        boolean result = l.remove(it);
        return result;
    }

    private boolean addItemToList(List<Item> l, Item it) {
        boolean result = l.add(it);
        return result;
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
    public void onLocationChanged(android.location.Location location) {

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
            myRefstops.setValue(dbH.countSigned());
            myRefroute.setValue(routename.getText());
            myRefdeldate.setValue(deliverdate.getText());
            myRefordertype.setValue(ordertype.getText());
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
                                    myRefstops.setValue(dbH.countSigned());
                                    myRefroute.setValue(routename.getText());
                                    myRefdeldate.setValue(deliverdate.getText());
                                    myRefordertype.setValue(ordertype.getText());
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

    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {

        String orderdate;
        String ordertype;
        String route;
        String signature;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder
                    .setTitle("Signature Saved ")
                    .setMessage("Thank you")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialognew, int which) {
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

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

//    public void OrderHeaderPost() {
//
//        ArrayList<Orders> dealLineToUpload = dbH.getOrderHeadersNotUploaded();
//        for (Orders orderAttributes : dealLineToUpload) {
//
//            String strNotesDrivers = "NULL";
//            String strEmailAddress = "NULL";
//            String strCashSig = "NULL";
//            String strStartTime = "NULL";
//            String strEndTime = "NULL";
//            String strTheImage = "NoImage";
//            String signedBy = "NULL";
//            if (orderAttributes.getstrNotesDrivers() != null && !orderAttributes.getstrNotesDrivers().isEmpty()) {
//                strNotesDrivers = orderAttributes.getstrNotesDrivers();
//            }
//            if (orderAttributes.getstrEmailCustomer() != null && !orderAttributes.getstrEmailCustomer().isEmpty()) {
//                strEmailAddress = orderAttributes.getstrEmailCustomer();
//            }
//            if (orderAttributes.getstrCashsignature() != null && !orderAttributes.getstrCashsignature().isEmpty()) {
//                strCashSig = orderAttributes.getstrCashsignature();
//            }
//
//            if (orderAttributes.getStartTripTime() != null && !orderAttributes.getStartTripTime().isEmpty()) {
//                strStartTime = orderAttributes.getStartTripTime();
//            }
//            if (orderAttributes.getEndTripTime() != null && !orderAttributes.getEndTripTime().isEmpty()) {
//                strEndTime = orderAttributes.getEndTripTime();
//            }
//            if (orderAttributes.getimagestring() != null && !orderAttributes.getimagestring().isEmpty()) {
//                strTheImage = orderAttributes.getimagestring();
//            }
//            if (orderAttributes.getstrCustomerSignedBy() != null && !orderAttributes.getstrCustomerSignedBy().isEmpty()) {
//                signedBy = orderAttributes.getstrCustomerSignedBy();
//            }
//            Log.e("*****", "********************************note " + strEmailAddress);
//            new UploadNewOrderLines(orderAttributes.getInvoiceNo(), orderAttributes.getLatitude(), orderAttributes.getLongitude(),
//                    strTheImage, orderAttributes.getCashPaid(), strNotesDrivers, orderAttributes.getoffloaded(), strEmailAddress, strCashSig, strStartTime, strEndTime, orderAttributes.getDeliverySequence(), orderAttributes.getstrCoordinateStart(), signedBy, orderAttributes.getLoyalty()).execute();
//        }
//
//    }
//
//    public class UploadNewOrderLines extends AsyncTask<Void, Void, Void> {
//
//        String invoice;
//        String lat;
//        String lon;
//        String image;
//        String cash;
//        String note;
//        String offload;
//        String strEmailAddress;
//        String strCashSig;
//        String strStartTime;
//        String strEndTime;
//        String delseq;
//        String strCoord;
//        String signedBy;
//        String Loyalty;
//
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//        }
//
//        public UploadNewOrderLines(String invoice, String lat, String lon, String image, String cash, String note, String offload, String email, String strCashSig, String strStartTime,
//                                   String strEndTime, String delseq, String strCoord, String signedBy, String Loyalty) {
//            this.invoice = invoice;
//            this.lat = lat;
//            this.lon = lon;
//            this.image = image;
//            this.cash = cash;
//            this.note = note;
//            this.offload = offload;
//            this.strEmailAddress = email;
//            this.strCashSig = strCashSig;
//            this.strStartTime = strStartTime;
//            this.strEndTime = strEndTime;
//            this.delseq = delseq;
//            this.strCoord = strCoord;
//            this.signedBy = signedBy;
//            this.Loyalty = Loyalty;
//
//
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            HttpClient httpclient = new DefaultHttpClient();
//
//            //dbCreation();
//            //}
//            Log.e("postIP", "++++++++++++++++++++++++++++++++" + SERVERIP + "PostHeaders");
//            Log.e("postIP", "++++++++++++++++++++++++++++++++ signedBy" + signedBy);
//            Log.e("Loyalty", "++++++++++++++++++++++++++++++++ Loyalty" + Loyalty);
//            HttpPost httppost = new HttpPost(SERVERIP + "PostHeaders");
//            try {
//                // Add your data
//                JSONObject json = new JSONObject();
//                json.put("invoice", invoice);
//                json.put("lat", lat);
//                json.put("lon", lon);
//                json.put("image", image);
//                json.put("cash", cash);
//                json.put("note", note);
//                json.put("offload", offload);
//                json.put("strEmailAddress", strEmailAddress);
//                json.put("strCashSig", strCashSig);
//                json.put("strEndTime", strEndTime);
//                json.put("strStartTime", strStartTime);
//                json.put("delseq", delseq);
//                json.put("strCoord", strCoord);
//                json.put("signedBy", signedBy);
//                json.put("Loyalty", Loyalty);
//                json.put("fridgetemp", freezeTemp);
//
//                Log.d("TEST_HEADER_RESPONSE", "TEMP: "+freezeTemp);
//
//                Log.e("Loyalty", "++++++++++++++++++++++++++++++++JSON Loyalty " + Loyalty);
//                Log.d("JSON", json.toString());
//                List nameValuePairs = new ArrayList(1);
//                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));
//
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                // Execute HTTP Post Request
//                org.apache.http.HttpResponse response = httpclient.execute(httppost);
//                String responseBody = EntityUtils.toString(response.getEntity());
//                Log.e("JSON-*", "RESPONSE is HEADERS**: " + responseBody);
//                JSONArray BoardInfo = new JSONArray(responseBody);
//                Log.d("TEST_HEADER_RESPONSE", "doInBackground: "+BoardInfo);
//
//                for (int j = 0; j < BoardInfo.length(); ++j) {
//
//                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
//                    String ID, strPartNumber;
//                    ID = BoardDetails.getString("InvoiceNo");
//
//                    Log.e("JSON-*", "RESPONSE IS HEADERS**: " + ID);
//                    dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + ID + "'");
//                }
//
//            } catch (Exception e) {
//                Log.e("JSON_TESTING", e.getMessage());
//            }
//            // db.close();
//            return null;
//        }
//    }
}
