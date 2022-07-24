package com.regin.reginald.vehicleanddrivers;

import android.Manifest;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
//import android.support.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.OrderTypes;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.Routes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.NetworkingFeedback;
import com.regin.reginald.vehicleanddrivers.PrinterControl.BixolonPrinter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import nl.elastique.poetry.json.JsonPersister;


public class LandingPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GPSTracker gps;
    //Button get, start_trip, closelines, btndoneoffloading, donelineinfo, close_line_info, refresh, printers, saveddata, endtrip, btndeliverynotes, btncreditrequest;
    Button start_trip, closelines, btndoneoffloading, donelineinfo, close_line_info;

    private TextView get, saveddata, btncreditrequest, endtrip, printers;
    private FloatingActionButton refresh, btndeliverynotes;

    Spinner route, ordertypes;
    EditText qtyordered, notecomment;
    Intent mServiceIntent;
    private OrderService mSensorService;

    Context ctx;
    private int selectedRoute;
    private int selectedOrderTypes;

    public Context getCtx() {
        return ctx;
    }

    int len = 0;
    private int year;
    private int month;
    private int day;
    private String currentDate;
    private static final int DATE_DIALOG_ID = 1;
    //Grocery Express = http://169.255.77.160:8181/DriverGas/
    //So-Ca = http://so-ca.ddns.net:8179/driver/
    //String customerOrders, SERVERIP = "http://linxsystems3.dedicated.co.za:8881/driver/",ordertypeidreturned,routeidreturned,dbDateFrom,dbRoute,dbLateOrder,filterTable="";"http://192.168.0.18:8181/driver/"
    String customerOrders, SERVERIP, LINX = "http://102.37.0.48/driversapp/", ordertypeidreturned, routeidreturned, dbDateFrom, dbRoute, dbLateOrder, filterTable = "";
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    ProgressDialog progressDoalog;
    //private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static BixolonPrinter bxlPrinter = null;
    double lat = -33.966145;
    double lon = 22.466218;
    private static final String TAG = "InvoiceActivity";
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    private FirebaseAuth mAuth;
    private boolean serviceRunning = false;

    private CardView deliveryDateCard;
    private TextView dateTextView;

    private ConstraintLayout snackBarLayout;

    private ProgressBar progressBar;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.landing_act);
        setContentView(R.layout.activity_landing_page);
        FirebaseApp.initializeApp(LandingPage.this);
        AndroidNetworking.initialize(getApplicationContext());
        DriverPermission();
        if (bxlPrinter == null) {
            bxlPrinter = new BixolonPrinter(this);
        }


        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
       /* if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasLocationPermissions != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }*/
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        int hasLocationPermissions = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermissions != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

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

        //mDatabaseHelper = DatabaseHelper.getHelper(this);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/";
        db = this.openOrCreateDatabase("LinxDriversOrders.db", Context.MODE_PRIVATE, null);

        final String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS PrinterInfo (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ProtoType TEXT, LogicalName TEXT ,Address TEXT)");

        dbH.updateDeals("CREATE TABLE IF NOT EXISTS Filters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,RouteName TEXT, OrderType TEXT ,DelDate TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS TripHeader (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,RouteName TEXT, OrderType TEXT ,DelDate TEXT," +
                "dtmCreated TEXT,KmStart TEXT,KmEnd TEXT,SealNumber TEXT,DriverName TEXT,DriverCompleteSignature TEXT,Completed boolean DEFAULT 0,Uploaded boolean DEFAULT 0)");

        dbH.updateDeals("CREATE TABLE IF NOT EXISTS WareHouses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,WareHouse TEXT, WareHouseId Integer)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS ManagementConsole (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Messages TEXT, DocID TEXT ,datetimes TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderLines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, OrderID INTEGER , ProductId INTEGER, PastelCode TEXT,PastelDescription TEXT,Qty DOUBLE,Price Double,OrderDetailId INTEGER,Comment TEXT,offLoadComment TEXT,returnQty DOUBLE,Uploaded boolean DEFAULT 1,blnoffloaded INTEGER,blnTruckchecked INTEGER,WareHouseName TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS OrderHeaders (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,StoreName TEXT, OrderID INTEGER ,CustomerPastelCode TEXT,DeliveryDate TEXT, InvoiceNo TEXT, DeliveryAddress TEXT,Latitude DOUBLE,Longitude DOUBLE,OrderValueExc Double ,OrderValueInc Double ,User TEXT,OrderMass DOUBLE,offloaded boolean DEFAULT 0,Uploaded boolean DEFAULT 1,imagestring TEXT,CashPaid DOUBLE,strNotesDrivers TEXT,StartTripTime TEXT,EndTripTime TEXT,DeliverySeq INTEGER,strCoordinateStart TEXT,DriverName TEXT,DriverEmail TEXT,DriverPassword TEXT,strCustomerSignedBy TEXT,dteCheckIn TEXT,dteCheckOut TEXT,PaymentType TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblSettings(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, strServerIp VARCHAR,regKey TEXT,Company VARCHAR,DeviceID TEXT,Email TEXT);");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS Notifications(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,TabletId TEXT, Messages TEXT,Uploaded boolean DEFAULT 0);");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS CheckLists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,checkListId INTEGER,checkListMessage TEXT);");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblDeliveryNotesLines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,CustomerName TEXT,ProductName TEXT, Qty DOUBLE,Weights Double," +
                "DeliveryDate TEXT,Notes TEXT,ReferenceNumber TEXT)")
        ;
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblCreditNotesLines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,CustomerName TEXT,ProductName TEXT, Qty DOUBLE,Weights Double," +
                "DeliveryDate TEXT,Notes TEXT,ReferenceNumber TEXT,Uploaded boolean DEFAULT 0,Completed boolean DEFAULT 0)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblDeliveryNotesHeader (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,CustomerName TEXT ,DeliveryDate TEXT,ReferenceNumber TEXT,username TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblCreditNotesHeader (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,CustomerName TEXT ,DeliveryDate TEXT,ReferenceNumber TEXT," +
                "username TEXT,UniqueString Text,Completed boolean DEFAULT 0,Uploaded boolean DEFAULT 0,Signature TEXT,SignedBy TEXT,strEmail TEXT,Drivername TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS CheckLists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,checkListId INTEGER,checkListMessage TEXT);");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS ExtraProducts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ProductId INTEGER,ProductCode TEXT,ProductName TEXT);");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS ExtraProductsToPost (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ProductId INTEGER,InvoiceNo TEXT,Qty TEXT,Uploaded boolean DEFAULT 0);");
        // dbH.updateDeals("Insert into tblSettings (strServerIp,regKey,Company,DeviceID,Email) values('http://servenerip.com','1233456','Example Company','45121545','example.company.co.za')");
        // dbH.updateDeals("Insert into tblSettings (strServerIp,regKey,Company,DeviceID,Email) values('http://serverip.com','1233456','Example Company','45121545','example.company.co.za')");

        try {
            dbH.updateDeals("ALTER TABLE Filters  ADD COLUMN kmStart TEXT default null");
            dbH.updateDeals("ALTER TABLE Filters  ADD COLUMN kmEnd TEXT default null");
            dbH.updateDeals("ALTER TABLE tblSettings  ADD COLUMN GroupId INTEGER default 1");
            dbH.updateDeals("ALTER TABLE OrderHeaders  ADD COLUMN PaymentType TEXT");
            dbH.updateDeals("ALTER TABLE tblCreditNotesHeader  ADD COLUMN Signature TEXT");
            dbH.updateDeals("ALTER TABLE tblCreditNotesHeader  ADD COLUMN SignedBy TEXT");
            dbH.updateDeals("ALTER TABLE tblCreditNotesHeader  ADD COLUMN strEmail TEXT");
            //dbH.updateDeals("ALTER TABLE Filters  ADD COLUMN kmEnd TEXT default null");
        } catch (SQLiteException ex) {
            Log.w(TAG, "Altering Filter " + ex.getMessage());
        }

        ArrayList<SettingsModel> settIP = dbH.getSettings();

        if (settIP.size() < 1) {
            // dbH.updateDeals("Insert into tblSettings (strServerIp,regKey,Company,DeviceID,Email) values('http://serverip.com','1233456','Example Company','45121545','example.company.co.za')");
            SERVERIP = "http://serverip.com";
        }

        for (SettingsModel orderAttributes : settIP) {
            SERVERIP = orderAttributes.getstrServerIp();
        }
        Log.e("settIP", "**************************************" + SERVERIP);
        //mSensorService = new OrderService(getCtx());
        // mServiceIntent = new Intent(this, OrderService.class);
        // getApplicationContext().startForegroundService(mServiceIntent);

        if (dbH.selectCountNotUploaded() > 0) {
            ArrayList<OtherAttributes> oD = dbH.returnFilters();
            for (OtherAttributes orderAttributes : oD) {
                dbRoute = orderAttributes.getroute();
                dbLateOrder = orderAttributes.getordertype();
                dbDateFrom = orderAttributes.getdeliverydate();
                Intent i = new Intent(LandingPage.this, OrderNotUploadedActivity.class);
                i.putExtra("deldate", dbDateFrom);
                i.putExtra("routes", dbRoute);
                i.putExtra("ordertype", dbLateOrder);
                startActivity(i);
            }
        }

        get = findViewById(R.id.closelines);
        start_trip = findViewById(R.id.start_trip);
        printers = findViewById(R.id.printers);
        refresh = findViewById(R.id.refresh);
        saveddata = findViewById(R.id.saveddata);
        btndeliverynotes = findViewById(R.id.btndeliverynotes);
        btncreditrequest = findViewById(R.id.btncreditrequest);
        endtrip = findViewById(R.id.endtrip);
        ordertypes = findViewById(R.id.ordertypes);
        route = findViewById(R.id.routeid);
        dateTextView = findViewById(R.id.datetime);
        deliveryDateCard = findViewById(R.id.deliverDateCard);
        deliveryDateCard = findViewById(R.id.deliverDateCard);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        deliveryDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        //dte_from =  findViewById(R.id.datetime);
        saveddata.setVisibility(View.GONE);
        endtrip.setVisibility(View.GONE);
        // endtrip.setVisibility(View.INVISIBLE);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowDate = dateFormat.format(tomorrow);
        //dateTextView.setText(tomorrowDate);
        dateTextView.setText("2020-6-23");
        //returnFilters()
       /* Log.e("rule log", "**************************************" + dbH.getThings("Login"));
        if(dbH.getThings("Login") !=0)
        {
            if(dbH.checkIfUserLoggedIn() ==0)
            {
                Intent l = new Intent(LandingPage.this,LoginActivity.class);
                startActivity(l);
            }

        }*/
        if (dbH.countOffloaded() < 1) {
            endtrip.setVisibility(View.VISIBLE);
        }
        if (dbH.countFilters() < 1) {
            endtrip.setVisibility(View.GONE);
        }
//
//        dte_from.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                year = c.get(Calendar.YEAR);
//                month = c.get(Calendar.MONTH);
//                day = c.get(Calendar.DAY_OF_MONTH);
//                showDialog(DATE_DIALOG_ID);
//
//            }
//        });
        Log.e("subID", "**************************************************" + LINX + subscriberId);
        new getOrderTypes().execute(SERVERIP + "OrderTypesTest.php?key=" + subscriberId);
        new checkIfRegistered().execute(LINX + "Registration.php?key=" + subscriberId);

        //new getRoutes().execute(SERVERIP + "Routes.php?key=" + subscriberId);
        new getRoutes().execute(SERVERIP + "Routes.php");
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPage.this, LandingPage.class);
                startActivity(i);
            }
        });
        //TODO: Set Up Btn
        printers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(LandingPage.this, PrinterFunctionActivity.class);
                startActivity(p);
            }
        });
        ArrayList<OtherAttributes> sett = dbH.thereIsData();
        for (OtherAttributes orderAttributes : sett) {
            filterTable = orderAttributes.getdeliverydate();
        }
        if (filterTable != null && !filterTable.isEmpty()) {

            Log.e("*******", "check is there is data*****************" + dbH.thereIsData());
            saveddata.setVisibility(View.VISIBLE);
            //  endtrip.setVisibility(View.VISIBLE);

            saveddata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<OtherAttributes> oD = dbH.returnFilters();
                    for (OtherAttributes orderAttributes : oD) {
                        dbRoute = orderAttributes.getroute();
                        dbLateOrder = orderAttributes.getordertype();
                        dbDateFrom = orderAttributes.getdeliverydate();
                        Intent i = new Intent(LandingPage.this, MainActivity.class);
                        i.putExtra("deldate", dbDateFrom);
                        i.putExtra("routes", dbRoute);
                        i.putExtra("ordertype", dbLateOrder);
                        startActivity(i);
                    }
                }
            });
            endtrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent p = new Intent(LandingPage.this, EndTripActivity.class);
                    startActivity(p);

                }
            });

        }
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbH.isUploaded()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
                    builder
                            .setTitle("Transactions")
                            .setMessage("You have some outstanding Instruction, please make sure you have better connectivity for the data to upload ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Please End Trip before you GET again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //OKAY
                                    /*Intent intent = new Intent(LandingPage.this, OrderService.class);
                                    stopService(intent);*/
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    dbH.updateDeals("delete from OrderLines");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Intent i = new Intent(LandingPage.this, MainActivity.class);
                    Intent i = new Intent(LandingPage.this, MainActivity.class);
                    //Toast.makeText(LandingPage.this, ""+dateTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    i.putExtra("deldate", dateTextView.getText().toString());
                    i.putExtra("routes", route.getSelectedItem().toString());
                    i.putExtra("ordertype", ordertypes.getSelectedItem().toString());
                    i.putExtra("orderId", selectedOrderTypes);
                    i.putExtra("routeId", selectedRoute);
                    startActivity(i);

                    //Log.d("CHECKING_ID", "onClick: "+selectedOrderTypes + " - "+selectedRoute);
                }
            }
        });

        btndeliverynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPage.this, DeliveryNotesLandingPage.class);
                startActivity(i);
            }
        });
        btncreditrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingPage.this, CreditRequitionLandingPage.class);
                startActivity(i);
            }
        });
        //Send data tothe cloud

        final Handler handler = new Handler();
        Runnable runnableNotify = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {

                while (dbH.NotificationTableHasData() > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            ArrayList<OtherAttributes> dealLineToUpload = dbH.sendANotification();
                            for (OtherAttributes orderAttributes : dealLineToUpload) {

                                new UploadNotifications(orderAttributes.getMessages(), orderAttributes.getconDocId()).execute();
                            }
                            if (dbH.countOffloaded() < 1) {
                                endtrip.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnableNotify).start();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
           /* if(dbH.selectCountNotUploaded()>0)
            {*/
            ArrayList<OtherAttributes> oD = dbH.returnFilters();
            for (OtherAttributes orderAttributes : oD) {
                dbRoute = orderAttributes.getroute();
                dbLateOrder = orderAttributes.getordertype();
                dbDateFrom = orderAttributes.getdeliverydate();

            }
            //}
            new checkfirebasetrip().execute(LINX + "registerfirebasetoken?token=" + newToken + "&ordertype=" + dbLateOrder + "&route=" + dbRoute + "&deldate=" + dbDateFrom + "&counts=" + dbH.selectCountNotUploaded());
        });
    }

    private void openDatePicker() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                //Month
                int j = i1 + 1;

                //date = i + "-" + j + "-" + i2;
                //date = i2 + "-" + j + "-" + i;
                date = i + "-" + j + "-" + i2;
                //2022-1-15
                dateTextView.setText(date);

            }
            //}, day, month, year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        new DatePickerDialog(AddTimeActivity.this, null, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        datePickerDialog.show();
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }


    private class getOrderTypes extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //progressBar.setVisibility(View.VISIBLE);
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

                    List<OrderTypeModel> products = new Gson().fromJson(customerOrders, new TypeToken<List<OrderTypeModel>>() {
                    }.getType());
                    int i = 1;
//                    for (OrderTypes product : products) {
//                        product.setId(i);
//                        i++;
//                    }
//
//                    String productListString = new Gson().toJson(
//                            products,
//                            new TypeToken<ArrayList<OrderTypes>>() {
//                            }.getType());
//
//                    JSONArray product_json = new JSONArray(productListString);
//
//                    // Persist arrays to database
////                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
////                    persister.persistArray(OrderTypes.class, product_json);
//                    //readDatabaseProducts();
//                    Log.e("**ql*", "done sync");
                    List<OrderTypeModel> ordertype = products;

                    List<String> labels = new ArrayList<String>();
                    for (OrderTypeModel orderAttributes4 : ordertype) {
                        labels.add(orderAttributes4.getOrderType());
                    }
                    ArrayAdapter<String> ordertypeA =
                            new ArrayAdapter<String>(LandingPage.this,
                                    android.R.layout.simple_spinner_item, labels);
                    ordertypeA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    ordertypes.setAdapter(ordertypeA);
                    ordertypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            //selectedRoute = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
                            selectedOrderTypes = products.get(position).getOrderTypeId();
                            //routeName = routeList.get(position).getRouteName();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class checkIfRegistered extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Service!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + customerOrders);
            if (len > 0) {
                try {
                    //lastmess
                    //JSONObject data = new JSONObject(customerOrders);
                    JSONArray BoardInfo = new JSONArray(customerOrders);
                    Log.e("feeed", "feedback*****************************************" + BoardInfo.length());
                    for (int j = 0; j < BoardInfo.length(); ++j) {
                        Log.e("overhere", "//////sludooooooo");
                        JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                        String results;
                        results = BoardDetails.getString("results");

                        if (results.equals("NOT REGISTERED")) {
                            get.setVisibility(View.GONE);
                            saveddata.setText(results);
                            saveddata.setBackgroundColor(Color.RED);
                            Log.e("this is definitely", "not registered!!!!!!!!");
                        } else {
                            Log.e("registered", "device!!!!!!!!!!!!!!");

                        }
                    }
                    //progressDoalog.dismiss();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
                builder.setTitle("Please talk to your manager about this ID.")
                        .setMessage("ID :"+customerOrders)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent main = new Intent( LandingPage.this,LandingPage.class);
                                startActivity(main);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class checkfirebasetrip extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getBaseContext(), "firebase!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + customerOrders);
            if (len > 0) {
                try {
                    //lastmess
                    //JSONObject data = new JSONObject(customerOrders);
                    JSONArray BoardInfo = new JSONArray(customerOrders);
                    Log.e("firebase", "firebase*****************************************" + BoardInfo.length());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void DriverPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/,
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        if (isMyServiceRunning()) {
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(LandingPage.this, OrderService.class));
                        } else {
                            startService(new Intent(LandingPage.this, OrderService.class));
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private class getRoutes extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //progressBar.setVisibility(View.VISIBLE);
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
            Log.e("TEST_RESULT", "len**************" + len);
            if (len > 0) {
                try {

                    dbH.updateDeals("DROP TABLE IF EXISTS Routes");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS Routes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, RouteId INTEGER , RouteName TEXT)");

//                    List<Routes> products = new Gson().fromJson(customerOrders, new TypeToken<List<Routes>>() {
//                    }.getType());
//                    int i = 1;
//                    for (Routes product : products) {
//                        product.setId(i);
//                        i++;
//                    }
//
//                    String productListString = new Gson().toJson(
//                            products,
//                            new TypeToken<ArrayList<Routes>>() {
//                            }.getType());
//
//                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(Routes.class, product_json);
                    //readDatabaseProducts();
                    Log.e("SYNCHRONIZATION", "done sync");

                    List<RouteModel> products = new Gson().fromJson(customerOrders, new TypeToken<List<RouteModel>>() {
                    }.getType());

//                    for (RouteModel model : products) {
//                        Log.d("RESULT", "onPostExecute: "+model.getRouteName());
//                    }
                    new NetworkingFeedback(dbH).insertRouteIntoLocalStorage(products);

                    // List<RouteModel> routesdata = dbH.getRoutes();
                    List<RouteModel> routesdata = products;
//                    Toast.makeText(LandingPage.this, "" + routesdata.size(), Toast.LENGTH_SHORT).show();
                    List<String> labels = new ArrayList<String>();
                    for (RouteModel orderAttributes4 : routesdata) {
                        labels.add(orderAttributes4.getRouteName());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(LandingPage.this,
                                    android.R.layout.simple_spinner_item, labels);
//                    ArrayAdapter<RouteModel> adapter =
//                            new ArrayAdapter<RouteModel>(LandingPage.this,
//                                    android.R.layout.simple_spinner_item, products);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    route.setAdapter(adapter);
                    route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            //selectedRoute = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
                            selectedRoute = products.get(position).getRouteId();
                            //routeName = routeList.get(position).getRouteName();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(LandingPage.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("Synchronizing data");
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
            dateTextView.setText(currentDate);
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

    public static BixolonPrinter getPrinterInstance() {


        Log.e("bxlPrinter", "****************" + bxlPrinter);
        return bxlPrinter;
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (OrderService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }*/

    @Override
    protected void onDestroy() {
        // stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent refresh = new Intent(LandingPage.this, LandingPage.class);
                    startActivity(refresh);

                }
                return;
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
        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {

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
    public void onLocationChanged(final android.location.Location location) {

      /*  String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());*/
        lat = location.getLatitude();
        lon = location.getLongitude();
        if (!haveNetwork()) {
            saveddata.setBackgroundColor(Color.RED);
        } else {
            saveddata.setBackgroundColor(Color.GREEN);
        }
        // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        //  mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        // Toast.makeText(this, ""+haveNetwork(), Toast.LENGTH_SHORT).show();

        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

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

    private class UploadNotifications extends AsyncTask<Void, Void, Void> {
        String mess;
        String tabledId;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadNotifications(String mess, String tabledId) {
            this.mess = mess;
            this.tabledId = tabledId;

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            Log.e("sendmessage", "***********************************" + SERVERIP + "sendMessage.php");
            HttpPost httppost = new HttpPost(SERVERIP + "sendMessage.php");
            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("mess", mess);
                json.put("ids", tabledId);


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
                    String ID, strPartNumber;
                    ID = BoardDetails.getString("id");

                    Log.e("JSON-*", "RESPONSE is lines ID ********: " + ID);
                    dbH.updateDeals("Delete from  Notifications where TabletId = '" + ID + "'");
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

    private boolean haveNetwork() {
        boolean has_wifi = false;
        boolean has_mobile_data = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("Wifi")) {
                if (info.isConnected()) {
                    has_wifi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("Mobile")) {
                if (info.isConnected()) {
                    has_mobile_data = true;
                }
            }
        }
        return has_wifi || has_mobile_data;
    }
}


