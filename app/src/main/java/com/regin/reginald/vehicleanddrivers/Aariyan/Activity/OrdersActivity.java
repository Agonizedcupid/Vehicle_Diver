package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import static com.regin.reginald.vehicleanddrivers.MainActivity.round;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.regin.reginald.model.Routes;
import com.regin.reginald.vehicleanddrivers.Aariyan.Abstraction.BaseActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.OrdersAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.Resource;
import com.regin.reginald.vehicleanddrivers.Aariyan.ViewModel.OrdersViewModel;
import com.regin.reginald.vehicleanddrivers.Data;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdersActivity extends BaseActivity {

    private IpModel serverModel;
    private String deliveryDate;
    private int routeId, orderId;
    private DatabaseAdapter databaseAdapter;
    private OrdersViewModel orderViewModel;
    private RecyclerView recyclerView;

    private TextView routeNames, orderTypes, dDate, endTripBtn, tripInfoBtn, uploadedCount;

    private ImageView backBtn;
    private TextView acknowledgeStockBtn;

    private ProgressBar progressBar;

    String SERVERIP;

    Handler handler = new Handler();
    Runnable runnableUpload;
    int delayUpload = 10000;

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

        //counting the Upload & Non-Upload
        uploadedNNonUploaded();
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
                dbH.updateDeals("UPDATE OrderHeaders set StartTripTime='" + tomorrowDate + "', strCoordinateStart='" + coordinates + "'");
            }
        });
        builder.setView(view);

        builder.show();
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

    /**
     * Background task:
     */

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
}