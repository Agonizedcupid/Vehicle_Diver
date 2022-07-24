package com.regin.reginald.vehicleanddrivers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import com.bxl.config.util.BXLBluetoothLE;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.model.WareHouses;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.loopj.android.http.AsyncHttpClient.log;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderLinesModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.WareHousesModel;
import com.regin.reginald.vehicleanddrivers.PrinterControl.BixolonPrinter;

import jpos.JposException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jakewharton.fliptables.FlipTable;

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

public class InvoiceDetails extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;
        String ItemString7;
        String ItemString8;
        boolean checked;


        Item(String t, String t2, String t3, String t4, String t5, String t6, String t7, String t8) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;
            ItemString7 = t7;
            ItemString8 = t8;

        }

        public boolean isChecked() {
            return checked;
        }
    }

    public class CheckTruckItem {
        String ItemString;
        String ItemString2;


        CheckTruckItem(String t, String t2) {
            ItemString = t;
            ItemString2 = t2;


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
        TextView text7;

        public float lastTouchedX;
        public float lastTouchedY;

    }

    static class ViewHolderTruck {
        //ImageView icon;
        TextView text;
        TextView text2;
    }

    /*
     * Inner list view*/

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;
        private boolean[] checkBoxState = null;
        private HashMap<Item, Boolean> checkedForCountry = new HashMap<>();

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

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View rowView = convertView;


            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.orderlisting_rows, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.storename);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.address);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.orderid);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.del);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.comment);
                viewHolder.text6 = (CheckBox) rowView.findViewById(R.id.offload);
                viewHolder.text7 = (TextView) rowView.findViewById(R.id.returned);

                rowView.setTag(viewHolder);
            }

            final ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);
            holder.text4.setText(list.get(position).ItemString4);
            //  holder.text5.setText(list.get(position).ItemString5);
            holder.text7.setText(list.get(position).ItemString5);
            holder.text5.setBackgroundColor(Color.rgb(206, 204, 204));//


            if ((list.get(position).ItemString7).equals("1")) {
                list.get(position).checked = true;
                holder.text6.setChecked(true);

                //
            } else {
                list.get(position).checked = false;
                holder.text6.setChecked(false);
            }
            final Item country = list.get(position);
            checkBoxState = new boolean[list.size()];
            if (checkBoxState != null)
                holder.text6.setChecked(checkBoxState[position]);


            // holder.text6.setChecked(checkBoxState[position]);

            Log.e("*******", "************PPPP" + checkedForCountry.get(list.get(position)));
            Log.e("*******", "************QQQQ" + checkedForCountry.get(country));

            if (checkedForCountry.get(country) != null) {
                // holder.text6.setChecked(checkedForCountry.get(checkedForCountry.get(country)));
                holder.text6.setChecked(checkedForCountry.get(country));
            }
            holder.text6.setTag(country);
            holder.text6.setChecked(isChecked(position));
            return rowView;
        }

        public List<Item> getList() {
            return list;
        }

        public void ischecked(int position, boolean flag) {
            Log.e("this--", "------------------------" + this.list.get(position));
            Log.e("position--", "------------------------" + position);
            Log.e("flag--", "------------------------" + flag);

            checkedForCountry.put(this.list.get(position), flag);

            Log.e("len+++", "++++++++++++++++++++++++++++++++++++++++" + checkedForCountry);
        }

    }

    public class ItemsListAdapterTruck extends BaseAdapter {

        private Context context;
        private List<Item> list;
        private boolean[] checkBoxState = null;
        private HashMap<Item, Boolean> checkedForCountry = new HashMap<>();

        ItemsListAdapterTruck(Context c, List<Item> l) {
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

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            checkBoxState = new boolean[list.size()];
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.offloading_truck_rows, null);

                ViewHolderTruck viewHolder = new ViewHolderTruck();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.truck_row_productname);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.truck_qty);

                rowView.setTag(viewHolder);
            }

            final ViewHolderTruck holder = (ViewHolderTruck) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }


    }

    List<Item> items1, itemsChecked, lineinfo, items2, items2Checked, itemsfull, lineinforemove;
    ItemsListAdapter myItemsListAdapter, myItemsListAdapter2, myItemsListAdapterchecked, myItemsListAdapter2Checked;
    ItemsListAdapterTruck listAdapterAddRemove, listAdapterAddRemoveRightSide;


    ListView _orderdlistlines, _orderdlistlinescheckd, listviewsummary, fullviewlist, listview1, listview2;
    TextView invoice_no, invoice_nosummary, setInvTotIncl, currentlocation, paymenttype;
    TextView dot, calculated, cost, zero, one, price_vs_quantity, trend_header, avg_quantity, two, three, four, five, six, seven, eight, nine, backspace;
    Button btndoneoffloading, acceptthesummary, checkunattitems, closecash, submitcash, cash, zoom, closefullview, cancel_order;
    private ImageView closelines;
    private TextView truckorder;
    EditText cashfielddialog;
    private FloatingActionButton closelinessummary;
    int len = 0;
    //http://so-ca.ddns.net:8179/driver/
    // String customerOrders, SERVERIP = "http://linxsystems3.dedicated.co.za:8881/DriverGas/",deldate,ordertype,route,InvoiceNo,priceinput,emailaddress,ts,storename;= "http://192.168.0.18:8181/driver/"
    String customerOrders, SERVERIP, deldate, ordertype, route, InvoiceNo, priceinput, emailaddress, ts, storename;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    ProgressDialog progressDoalog;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private SignaturePad mNotes, cash_sig;
    CheckBox accept, checkBoxacceptcash;
    GPSTracker gps;
    Button btndocnote, save_note, tickall;
    Spinner productcats_checked, productcats_nochecked, products_warehouseses, productcats_nocheckedMain, productcats_checkedMain;

    private FloatingActionButton closeproductlists;

    double mass;
    double lat = -33.966145;
    double lon = 22.466218, custlat, custlon;
    static double PI_RAD = Math.PI / 180.0;
    private static BixolonPrinter bxlPrinter = null;

    private final int REQUEST_PERMISSION = 0;
    private final String DEVICE_ADDRESS_START = " (";
    private final String DEVICE_ADDRESS_END = ")";

    private final ArrayList<CharSequence> bondedDevices = new ArrayList<>();
    private ArrayAdapter<CharSequence> arrayAdapter;

    private static final String TAG = "InvoiceActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;

    /**
     * Instances & Variable
     */

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout._orderdlistlines);
        setContentView(R.layout.dummy_layout);
        AndroidNetworking.initialize(getApplicationContext());

        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();

        final int ANDROID_NOUGAT = 24;
        if (Build.VERSION.SDK_INT >= ANDROID_NOUGAT) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        ArrayList<SettingsModel> settIP = dbH.getSettings();
//
//        for (SettingsModel orderAttributes : settIP) {
//            SERVERIP = orderAttributes.getstrServerIp();
//        }

        /**
         * Getting the server details:
         */

        databaseAdapter = new DatabaseAdapter(this);
        List<IpModel> list = databaseAdapter.getServerIpModel();
        if (list.size() > 0) {
            SERVERIP = list.get(0).getServerIp();
        } else {
            SERVERIP = "";
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation();

        invoice_no = findViewById(R.id.invoice_no);
        //paymenttype = findViewById(R.id.paymenttype);
        setInvTotIncl = findViewById(R.id.total);
        //currentlocation = findViewById(R.id.currentlocation);
        _orderdlistlines = findViewById(R.id._orderdlistlines);
        _orderdlistlinescheckd = findViewById(R.id._orderdlistlinescheckd);
        //  mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad_lines);
        btndoneoffloading = findViewById(R.id.acceptthesummary);
        closelines = findViewById(R.id.closelines);
        btndocnote = findViewById(R.id.btndocnote);
        //tickall = findViewById(R.id.tickall);
        //checkunattitems = findViewById(R.id.checkunattitems);
        zoom = findViewById(R.id.zoom);
        accept = findViewById(R.id.accept);
        cash = findViewById(R.id.cash);
        truckorder = findViewById(R.id.truckorder);
        cancel_order = findViewById(R.id.cancel_order);
        //products_warehouseses = (Spinner) findViewById(R.id.products_warehouseses);
        productcats_nocheckedMain = findViewById(R.id.productcats_nochecked);
        productcats_checkedMain = findViewById(R.id.productcats_checked);

        Intent returndata = getIntent();
        deldate = returndata.getStringExtra("deldate");
        ordertype = returndata.getStringExtra("ordertype");
        route = returndata.getStringExtra("routes");
        InvoiceNo = returndata.getStringExtra("invoiceno");
        invoice_no.setText(returndata.getStringExtra("invoiceno"));
        cash.setText(returndata.getStringExtra("cash"));
        final String cashPaid = returndata.getStringExtra("cash");
        Log.e("Cash Paid", "****************************" + cashPaid);
        setPairedDevices();

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<Orders> infoheader = dbH.isSaved(InvoiceNo);
        ArrayList<Orders> orderheader = dbH.returnOrderHeadersInfoByInvoice(InvoiceNo);

        /**
         * New Code
         */
        //List<OrderModel> infoHeader = databaseAdapter.getOrdersByInvoice(InvoiceNo);

        btndoneoffloading.setVisibility(View.GONE);

        ArrayList<WareHouses> ordertypeWarehouse = dbH.warehouse();
        //List<WareHousesModel> ordertypeWarehouse = databaseAdapter.getWareHouses();
        //List<RouteModel> ordertypeWarehouse = databaseAdapter.getRoutes();

        List<String> labelsWare = new ArrayList<String>();
        labelsWare.add("ALL");
        for (WareHouses orderAttributes4 : ordertypeWarehouse) {
            labelsWare.add(orderAttributes4.getWareHouse());
        }

        //Putting the warehouse into Spinner:

        final ArrayAdapter<String> ordertypeAWare =
                new ArrayAdapter<String>(InvoiceDetails.this,
                        android.R.layout.simple_spinner_item, labelsWare);


//        ArrayAdapter<WareHousesModel> ordertypeAWare = new ArrayAdapter<WareHousesModel>(InvoiceDetails.this,
//                android.R.layout.simple_spinner_item, ordertypeWarehouse);
        ordertypeAWare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //products_warehouseses.setAdapter(ordertypeAWare);
        productcats_nocheckedMain.setAdapter(ordertypeAWare);
        productcats_checkedMain.setAdapter(ordertypeAWare);

        //////////////////////////////       Left Side Spinner        /////////////////////////////////////////////////////////////////
        productcats_nocheckedMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ArrayList<OrderLinesModel> oD = dbH.returnOrderLinesoffloadedByCategory(InvoiceNo, productcats_nocheckedMain.getSelectedItem().toString());
                ArrayList<OrderLines> oD = databaseAdapter.returnOrderLinesoffloadedByCategory(InvoiceNo, productcats_nocheckedMain.getSelectedItem().toString());
                Log.e("group", productcats_nocheckedMain.getSelectedItem().toString());
                items1 = new ArrayList<Item>();
                //  itemsChecked = new ArrayList<Item>();

                for (OrderLines orderAttributes : oD) {

                    if (orderAttributes.getblnoffloaded().equals("0")) {
                        Item item = new Item(orderAttributes.getPastelDescription(), "" + orderAttributes.getPrice(), "" + orderAttributes.getQty(),
                                "", "Return: " + orderAttributes.getreturnQty(), "Lines", "" + orderAttributes.getblnoffloaded(), "" + orderAttributes.getOrderDetailId());
                        items1.add(item);
                    }
                    /*if(orderAttributes.getblnoffloaded() =="1")
                    {
                        Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(),orderAttributes.getQty(),
                                "",orderAttributes.getreturnQty(),"Lines",orderAttributes.getblnoffloaded(),orderAttributes.getOrderDetailId());
                        itemsChecked.add(item);
                    }*/

                }

                myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);
                // myItemsListAdapterchecked = new ItemsListAdapter(InvoiceDetails.this, itemsChecked);
                _orderdlistlines.setAdapter(myItemsListAdapter);
                //  _orderdlistlinescheckd.setAdapter(myItemsListAdapterchecked);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //////////////////////////////       Right Side Spinner        /////////////////////////////////////////////////////////////////
        productcats_checkedMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloadedByCategory(InvoiceNo, productcats_checkedMain.getSelectedItem().toString());
                //List<OrderLinesModel> oD = databaseAdapter.returnOrderLinesOffloadedByCategory(InvoiceNo, productcats_checkedMain.getSelectedItem().toString());
                Log.e("group", productcats_checkedMain.getSelectedItem().toString());
                //  items1 = new ArrayList<Item>();
                itemsChecked = new ArrayList<Item>();
                for (OrderLines orderAttributes : oD) {

                    if (orderAttributes.getblnoffloaded().equals("1")) {
                        Item item = new Item(orderAttributes.getPastelDescription(), "" + orderAttributes.getPrice(), "" + orderAttributes.getQty(),
                                "", "Return: " + orderAttributes.getreturnQty(), "Lines", "" + orderAttributes.getblnoffloaded(), "" + orderAttributes.getOrderDetailId());
                        itemsChecked.add(item);
                    }


                }

                // myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);
                myItemsListAdapterchecked = new ItemsListAdapter(InvoiceDetails.this, itemsChecked);

                _orderdlistlinescheckd.setAdapter(myItemsListAdapterchecked);
                //_orderdlistlines.setAdapter(myItemsListAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //order headers
        for (Orders orderAttributes : infoheader) {

            if (orderAttributes.getoffloaded().equals("1")) {
                accept.setChecked(true);
                //mSignaturePad.setVisibility(View.INVISIBLE);
                btndocnote.setVisibility(View.INVISIBLE);
            } else {
                accept.setChecked(false);
            }
            cash.setText(orderAttributes.getCashPaid());
        }
        for (Orders orderAttributes : orderheader) {
            emailaddress = orderAttributes.getstrEmailCustomer();
            invoice_no.setText(orderAttributes.getInvoiceNo() + "( " + orderAttributes.getStoreName() + ")");
            storename = orderAttributes.getStoreName();
            setInvTotIncl.setText(orderAttributes.getInvTotIncl());
            //paymenttype.setText(orderAttributes.getPaymentType());
            accept.setText(orderAttributes.getPaymentType());
            // total = orderAttributes.getInvTotIncl();
        }

        //New code:
//        for (OrderModel orderAttributes : infoHeader) {
//            emailaddress = orderAttributes.getStrEmailCustomer();
//            invoice_no.setText(orderAttributes.getInvoiceNo() + "( " + orderAttributes.getStoreName() + ")");
//            storename = orderAttributes.getStoreName();
//            setInvTotIncl.setText(orderAttributes.getInvTotIncl());
//            //paymenttype.setText(orderAttributes.getPaymentType());
//            //paymenttype.setText("CASH");
//            // total = orderAttributes.getInvTotIncl();
//        }


        ArrayList<OrderLines> oD = databaseAdapter.returnOrderLines(returndata.getStringExtra("invoiceno"));
        Log.e("Cas invoi", "****************************++++++++++++++++++++++++++++++++++++++++++++++++++++++" + returndata.getStringExtra("invoiceno"));
        items1 = new ArrayList<Item>();
        itemsChecked = new ArrayList<Item>();

        for (OrderLines orderAttributes : oD) {

            if (orderAttributes.getblnoffloaded().equals("0")) {

                Item item = new Item(orderAttributes.getPastelDescription(), ""+orderAttributes.getPrice(), ""+orderAttributes.getQty(),
                        "", "Return: " + orderAttributes.getreturnQty(), "Lines", ""+orderAttributes.getblnoffloaded(), ""+orderAttributes.getOrderDetailId());
                items1.add(item);
                Log.e("items1", "" + items1);
            }
            if (orderAttributes.getblnoffloaded().equals("1")) {

                Item item = new Item(orderAttributes.getPastelDescription(), ""+orderAttributes.getPrice(), ""+orderAttributes.getQty(),
                        "", "Return: " + orderAttributes.getreturnQty(), "Lines", ""+orderAttributes.getblnoffloaded(), ""+orderAttributes.getOrderDetailId());
                itemsChecked.add(item);
                Log.e("itemsChecked", "" + itemsChecked);
            }

        }

        Log.e("items1", "" + items1);
        Log.e("itemsChecked", "" + itemsChecked);
        myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);
        myItemsListAdapterchecked = new ItemsListAdapter(InvoiceDetails.this, itemsChecked);

        _orderdlistlinescheckd.setAdapter(myItemsListAdapterchecked);
        _orderdlistlines.setAdapter(myItemsListAdapter);

       /* products_warehouseses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloadedByCategory(InvoiceNo,products_warehouseses.getSelectedItem().toString());
                items1 = new ArrayList<Item>();
                itemsChecked = new ArrayList<Item>();

                for (OrderLines orderAttributes: oD){

                    Log.e("ontheitem","////////////////////////////"+orderAttributes.getblnoffloaded());


                        Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(),orderAttributes.getQty(),
                                "",orderAttributes.getreturnQty(),"Lines",orderAttributes.getblnoffloaded(),orderAttributes.getOrderDetailId());
                        items1.add(item);


                }
                myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);

                _orderdlistlines.setAdapter(myItemsListAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogfull = new Dialog(InvoiceDetails.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                dialogfull.setContentView(R.layout.orderlinesfullscreen);

                dialogfull.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloaded(InvoiceNo);
                itemsfull = new ArrayList<Item>();

                for (OrderLines orderAttributes : oD) {

                    Log.e("ontheitem", "////////////////////////////" + orderAttributes.getblnoffloaded());
                    Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
                            "", "Return: " + orderAttributes.getreturnQty(), "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
                    itemsfull.add(item);

                }
                myItemsListAdapter2 = new ItemsListAdapter(InvoiceDetails.this, itemsfull);
                fullviewlist = (ListView) dialogfull.findViewById(R.id.fullviewlist);
                closefullview = (Button) dialogfull.findViewById(R.id.closefullview);

                fullviewlist.setAdapter(myItemsListAdapter2);
                closefullview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<OrderLines> oD = dbH.returnOrderLines(InvoiceNo);
                        items1 = new ArrayList<Item>();

                        for (OrderLines orderAttributes : oD) {

                            Log.e("ontheitem", "////////////////////////////" + orderAttributes.getblnoffloaded());
                            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
                                    "", "Return: " + orderAttributes.getreturnQty(), "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
                            items1.add(item);

                        }
                        myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);
                        _orderdlistlines.setAdapter(myItemsListAdapter);
                        myItemsListAdapter.notifyDataSetChanged();
                        dialogfull.dismiss();
                    }
                });
                dialogfull.show();

                fullviewlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        final Item selectedItem_line = (Item) (parent.getItemAtPosition(position));

                        Intent lineEdit = new Intent(InvoiceDetails.this, LineEdit.class);

                        lineEdit.putExtra("deldate", deldate);
                        lineEdit.putExtra("routes", route);
                        lineEdit.putExtra("ordertype", ordertype);
                        lineEdit.putExtra("orderdetailId", selectedItem_line.ItemString8);
                        lineEdit.putExtra("invoiceno", InvoiceNo);
                        startActivity(lineEdit);
                        return true;
                    }
                });
            }
        });

        truckorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogLists = new Dialog(InvoiceDetails.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                dialogLists.setContentView(R.layout.products_listview);

                dialogLists.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloaded(InvoiceNo);
                lineinfo = new ArrayList<Item>();
                lineinforemove = new ArrayList<Item>();

                for (OrderLines orderAttributes : oD) {

                    Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
                            "", "Return: " + orderAttributes.getreturnQty(), "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
                    lineinfo.add(item);
                }
                listAdapterAddRemove = new ItemsListAdapterTruck(InvoiceDetails.this, lineinfo);
                listAdapterAddRemoveRightSide = new ItemsListAdapterTruck(InvoiceDetails.this, lineinforemove);
                productcats_nochecked = (Spinner) dialogLists.findViewById(R.id.productcats_nochecked);
                productcats_checked = (Spinner) dialogLists.findViewById(R.id.productcats_checked);
                listview1 =  dialogLists.findViewById(R.id.listview1);
                listview2 =  dialogLists.findViewById(R.id.listview2);
                closeproductlists =  dialogLists.findViewById(R.id.closeproductlists);

                listview1.setAdapter(listAdapterAddRemove);
                listview2.setAdapter(listAdapterAddRemoveRightSide);
                listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item selectedItem = (Item) (parent.getItemAtPosition(position));

                        ItemsListAdapterTruck associatedAdapter = (ItemsListAdapterTruck) (parent.getAdapter());
                        List<Item> associatedList = associatedAdapter.getList();
                        Item associatedItem = associatedList.get(position);
                        if (removeItemToList(associatedList, associatedItem)) {

                            dbH.updateDeals("Update OrderLines set blnTruckchecked = 1 where OrderDetailId='" + selectedItem.ItemString8.toString() + "'");
                            view.invalidate();
                            associatedAdapter.notifyDataSetChanged();

                            ItemsListAdapterTruck list2Adapter = (ItemsListAdapterTruck) (listview2.getAdapter());
                            List<Item> list2List = list2Adapter.getList();

                            addItemToList(list2List, selectedItem);
                            list2Adapter.notifyDataSetChanged();
                        }
                    }
                });
                listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item selectedItem = (Item) (parent.getItemAtPosition(position));

                        ItemsListAdapterTruck associatedAdapter = (ItemsListAdapterTruck) (parent.getAdapter());
                        List<Item> associatedList = associatedAdapter.getList();
                        Item associatedItem = associatedList.get(position);
                        if (removeItemToList(associatedList, associatedItem)) {

                            dbH.updateDeals("Update OrderLines set blnTruckchecked = 0 where OrderDetailId='" + selectedItem.ItemString8.toString() + "'");

                            view.invalidate();
                            associatedAdapter.notifyDataSetChanged();

                            ItemsListAdapterTruck list2Adapter = (ItemsListAdapterTruck) (listview1.getAdapter());
                            List<Item> list2List = list2Adapter.getList();

                            addItemToList(list2List, selectedItem);
                            list2Adapter.notifyDataSetChanged();
                        }
                    }
                });
                closeproductlists.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* ArrayList<OrderLines> oD= dbH.returnOrderLines(InvoiceNo);
                        items1 = new ArrayList<Item>();

                        for (OrderLines orderAttributes: oD){

                            Log.e("ontheitem","////////////////////////////"+orderAttributes.getblnoffloaded());
                            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(),orderAttributes.getQty(),
                                    "",orderAttributes.getreturnQty(),"Lines",orderAttributes.getblnoffloaded(),orderAttributes.getOrderDetailId());
                            items1.add(item);

                        }
                        myItemsListAdapter = new ItemsListAdapter(InvoiceDetails.this, items1);
                        _orderdlistlines.setAdapter(myItemsListAdapter);*/
                        listAdapterAddRemove.notifyDataSetChanged();
                        dialogLists.dismiss();
                    }
                });
                productcats_nochecked.setAdapter(ordertypeAWare);
                productcats_checked.setAdapter(ordertypeAWare);
                productcats_nochecked.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloadedByCategory(InvoiceNo, productcats_nochecked.getSelectedItem().toString());
                        Log.e("group", productcats_nochecked.getSelectedItem().toString());
                        lineinfo = new ArrayList<Item>();
                        lineinforemove = new ArrayList<Item>();

                        for (OrderLines orderAttributes : oD) {

                            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
                                    "", "Return: " + orderAttributes.getreturnQty(), "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
                            lineinfo.add(item);
                        }
                        listAdapterAddRemove = new ItemsListAdapterTruck(InvoiceDetails.this, lineinfo);
                        listview1.setAdapter(listAdapterAddRemove);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                dialogLists.show();
            }
        });

////////////////////////////////////      Left List       /////////////////////////////////////////////////////////////////////////////////////////////
        _orderdlistlines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selectedItem = (Item) (parent.getItemAtPosition(position));

                ItemsListAdapter associatedAdapter = (ItemsListAdapter) (parent.getAdapter());
                List<Item> associatedList = associatedAdapter.getList();
                Item associatedItem = associatedList.get(position);
                if (removeItemToList(associatedList, associatedItem)) {

                    //dbH.updateDeals("Update OrderLines set blnoffloaded=1,Uploaded=0 Where OrderDetailId='" + selectedItem.ItemString8.toString() + "'");
                    //dbH.updateDeals("Update OrderLines set blnoffloaded=1,Uploaded=0 Where OrderDetailId='" + selectedItem.ItemString8.toString() + "'");
                    databaseAdapter.listSwappingUpdate(selectedItem.ItemString8, 1);
                    view.invalidate();
                    associatedAdapter.notifyDataSetChanged();

                    ItemsListAdapter list2Adapter = (ItemsListAdapter) (_orderdlistlinescheckd.getAdapter());
                    List<Item> list2List = list2Adapter.getList();

                    addItemToList(list2List, selectedItem);
                    list2Adapter.notifyDataSetChanged();
                    _orderdlistlinescheckd.smoothScrollToPosition(list2Adapter.getCount());
                }
            }
        });
//////////////////////////////////////////////////////    Right List    ///////////////////////////////////////////////////////////////////////////
        _orderdlistlinescheckd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selectedItem = (Item) (parent.getItemAtPosition(position));

                ItemsListAdapter associatedAdapter = (ItemsListAdapter) (parent.getAdapter());
                List<Item> associatedList = associatedAdapter.getList();
                Item associatedItem = associatedList.get(position);
                if (removeItemToList(associatedList, associatedItem)) {

                    //dbH.updateDeals("Update OrderLines set blnoffloaded=0,Uploaded=0 Where OrderDetailId='" + selectedItem.ItemString8.toString() + "'");
                    databaseAdapter.listSwappingUpdate(selectedItem.ItemString8, 0);
                    view.invalidate();
                    associatedAdapter.notifyDataSetChanged();

                    ItemsListAdapter list2Adapter = (ItemsListAdapter) (_orderdlistlines.getAdapter());
                    List<Item> list2List = list2Adapter.getList();

                    addItemToList(list2List, selectedItem);
                    list2Adapter.notifyDataSetChanged();
                    _orderdlistlines.smoothScrollToPosition(list2Adapter.getCount());
                }
            }
        });

        /**
         * Have to work on that part later on
         */
//        tickall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbH.updateDeals("Update OrderLines set blnoffloaded = 1 where orderId='" + InvoiceNo + "'");
//                Intent doc = new Intent(InvoiceDetails.this, InvoiceDetails.class);
//                doc.putExtra("invoiceno", InvoiceNo);
//                doc.putExtra("deldate", deldate);
//                doc.putExtra("ordertype", ordertype);
//                doc.putExtra("routes", route);
//                doc.putExtra("cash", cashPaid);
//                startActivity(doc);
//            }
//        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InvoiceDetails.this, android.R.style.Theme_Light_NoTitleBar);
                dialog.setContentView(R.layout.number_pick_with_delete);

      /*  TextView textView = new TextView(context);
        textView.setText();*/
                dialog.setTitle("Please Type in the Cash");
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                cashfielddialog = (EditText) dialog.findViewById(R.id.password_field);
                cashfielddialog.setText(cash.getText().toString());
                // mPasswordField.setText("1");
                zero = (TextView) dialog.findViewById(R.id.t9_key_0);
                one = (TextView) dialog.findViewById(R.id.t9_key_1);
                two = (TextView) dialog.findViewById(R.id.t9_key_2);
                three = (TextView) dialog.findViewById(R.id.t9_key_3);
                four = (TextView) dialog.findViewById(R.id.t9_key_4);
                five = (TextView) dialog.findViewById(R.id.t9_key_5);
                six = (TextView) dialog.findViewById(R.id.t9_key_6);
                seven = (TextView) dialog.findViewById(R.id.t9_key_7);
                eight = (TextView) dialog.findViewById(R.id.t9_key_8);
                nine = (TextView) dialog.findViewById(R.id.t9_key_9);
                dot = (TextView) dialog.findViewById(R.id.t9_key_dot);
                submitcash = (Button) dialog.findViewById(R.id.accept);
                closecash = (Button) dialog.findViewById(R.id.close);
                backspace = (TextView) dialog.findViewById(R.id.t9_key_backspace);
                cash_sig = (SignaturePad) dialog.findViewById(R.id.cash_sig);
                checkBoxacceptcash = (CheckBox) dialog.findViewById(R.id.checkBoxacceptcash);

                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cashfielddialog.append(((TextView) v).getText());
                    }
                });
                one.setOnClickListener(InvoiceDetails.this);
                two.setOnClickListener(InvoiceDetails.this);
                three.setOnClickListener(InvoiceDetails.this);
                four.setOnClickListener(InvoiceDetails.this);
                five.setOnClickListener(InvoiceDetails.this);
                six.setOnClickListener(InvoiceDetails.this);
                seven.setOnClickListener(InvoiceDetails.this);
                eight.setOnClickListener(InvoiceDetails.this);
                nine.setOnClickListener(InvoiceDetails.this);
                backspace.setOnClickListener(InvoiceDetails.this);

                dot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cashfielddialog.getText().toString().contains(".")) {
                            Toast.makeText(getBaseContext(), "You cannot type in a DOT(.) multiple times", Toast.LENGTH_SHORT).show();
                        } else {
                            cashfielddialog.setText(cashfielddialog.getText() + ".");
                        }

                    }
                });
                closecash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkBoxacceptcash.isChecked()) {
                            if ((cashfielddialog.getText().toString()) != null) {
                                cash.setText(cashfielddialog.getText().toString());
                            } else {
                                cash.setText("0.0");
                            }

                            //dbH.updateDeals("Update OrderHeaders set CashPaid='" + cashfielddialog.getText().toString() + "' where InvoiceNo='" + InvoiceNo + "'");
                            dbH.updateDeals("Update OrderHeaders set cashPaid='" + cashfielddialog.getText().toString() + "' where invoiceNo='" + InvoiceNo + "'");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, 0);
                            Date tomorrow = calendar.getTime();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
                            String tomorrowDate = dateFormat.format(tomorrow);

                            dbH.updateDeals("Insert into ManagementConsole (Messages,DocID,datetimes) values('" + cashfielddialog.getText().toString() + " Collected','" + InvoiceNo + "','" + tomorrowDate + "')");
                            dialog.dismiss();


                            Bitmap signatureBitmap = cash_sig.getSignatureBitmap();
                            //new UploadImage(signatureBitmap,IDs).execute();
                            //new MainActivity.UploadImage(signatureBitmap,selectedItem.ItemString3,lat,lon, tomorrowDate).execute();
                            if (addSignatureJpg(signatureBitmap, InvoiceNo)) {

                                Toast.makeText(InvoiceDetails.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(InvoiceDetails.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceDetails.this);
                            builder
                                    .setTitle("Cash Acceptance ")
                                    .setMessage("Please Accept that you are collection cash")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }
                });


                dialog.show();
            }
        });

        btndocnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doc = new Intent(InvoiceDetails.this, DocumentNotes.class);
                doc.putExtra("invoiceno", InvoiceNo);
                doc.putExtra("deldate", deldate);
                doc.putExtra("ordertype", ordertype);
                doc.putExtra("routes", route);
                doc.putExtra("cash", cashPaid);
                startActivity(doc);


            }
        });

        btndoneoffloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log.e("*****", "latlon********++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + lat + "," + lon);
                log.e("*****", "cash  cash********++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + (cash.getText().toString()).length());
                if (accept.isChecked() && ((cash.getText().toString()).trim() != null && (cash.getText().toString()).length() > 0)) {
                    checkLocation();
                    //need to remove test this
                    dbH.updateDeals("Update OrderHeaders set cashPaid='" + cash.getText().toString() + "', Latitude=" + lat + ", Longitude=" + lon + " where invoiceNo ='" + InvoiceNo + "'");

                    Intent main = new Intent(InvoiceDetails.this, SignaturePage.class);
                    main.putExtra("deldate", deldate);
                    main.putExtra("routes", route);
                    main.putExtra("ordertype", ordertype);
                    main.putExtra("invoiceno", InvoiceNo);
                    main.putExtra("cash", cashPaid);
                    main.putExtra("emailaddress", emailaddress);
                    main.putExtra("storename", storename);

                    startActivity(main);


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceDetails.this);
                    builder
                            .setTitle("Check box ")
                            .setMessage("Please make sure cash box is not empty and all items are ticked ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

            }
        });
        closelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceDetails.this);
                builder
                        .setTitle("Are you? ")
                        .setMessage("This will not save any changes made")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent main = new Intent(InvoiceDetails.this, MainActivity.class);
                                main.putExtra("deldate", deldate);
                                main.putExtra("routes", route);
                                main.putExtra("ordertype", ordertype);
                                //main.putExtra("orderdetailid",ordertype);
                                //
                                startActivity(main);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        _orderdlistlines.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Item selectedItem_line = (Item) (parent.getItemAtPosition(position));

                Intent lineEdit = new Intent(InvoiceDetails.this, LineEdit.class);

                lineEdit.putExtra("deldate", deldate);
                lineEdit.putExtra("routes", route);
                lineEdit.putExtra("ordertype", ordertype);
                lineEdit.putExtra("orderdetailId", selectedItem_line.ItemString8);
                lineEdit.putExtra("invoiceno", InvoiceNo);
                startActivity(lineEdit);
                return true;
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                        Toast.LENGTH_LONG).show();
                Log.e("boolean*", "*******************************************" + dbH.isoffladedline(InvoiceNo));
                if (!dbH.isoffladedline(InvoiceNo)) {
                    final Dialog dialog = new Dialog(InvoiceDetails.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                    dialog.setContentView(R.layout.summarry);

                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloaded(InvoiceNo);
                    items2 = new ArrayList<Item>();

                    for (OrderLines orderAttributes : oD) {

                        Log.e("ontheitem", "////////////////////////////" + orderAttributes.getblnoffloaded());
                        Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
                                "", "1", "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
                        items2.add(item);
                    }
                    myItemsListAdapter2 = new ItemsListAdapter(InvoiceDetails.this, items2);
                    listviewsummary =  dialog.findViewById(R.id._orderdlistlines);
                    invoice_nosummary =  dialog.findViewById(R.id.invoice_no);
                    closelinessummary =  dialog.findViewById(R.id.closelines);
                    acceptthesummary =  dialog.findViewById(R.id.acceptthesummary);
                    listviewsummary.setAdapter(myItemsListAdapter2);
                    closelinessummary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    closelinessummary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    acceptthesummary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("this", "++++++++++++++++++++++++++++++++++++****************************l");
                            Intent b = new Intent(InvoiceDetails.this, InvoiceDetails.class);
                            b.putExtra("deldate", deldate);
                            b.putExtra("routes", route);
                            b.putExtra("ordertype", ordertype);
                            b.putExtra("invoiceno", InvoiceNo);
                            b.putExtra("cash", cash.getText().toString());
                            startActivity(b);
                        }
                    });
                    listviewsummary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                            final Item selectedItem_line = (Item) (parent.getItemAtPosition(position));

                            Intent lineEdit = new Intent(InvoiceDetails.this, LineEdit.class);

                            lineEdit.putExtra("deldate", deldate);
                            lineEdit.putExtra("routes", route);
                            lineEdit.putExtra("ordertype", ordertype);
                            lineEdit.putExtra("orderdetailId", selectedItem_line.ItemString8);
                            lineEdit.putExtra("invoiceno", InvoiceNo);
                            startActivity(lineEdit);
                            return true;
                        }
                    });
                    dialog.show();
                    accept.setChecked(false);
                    btndoneoffloading.setVisibility(View.GONE);
                    Toast.makeText(InvoiceDetails.this, "Ekhane", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.e("isoffloaded","*******************"+dbH.isoffladedline());
                    btndoneoffloading.setVisibility(View.VISIBLE);
                    Toast.makeText(InvoiceDetails.this, "Na Ekhane", Toast.LENGTH_SHORT).show();

                }

            }
        });

        final Handler handler = new Handler();
        Runnable runnableNotify = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {

                while (dbH.NotificationTableHasData() > 0) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            ArrayList<OtherAttributes> dealLineToUpload = dbH.sendANotification();
                            for (OtherAttributes orderAttributes : dealLineToUpload) {

                                new UploadNotifications(orderAttributes.getMessages(), orderAttributes.getconDocId()).execute();
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnableNotify).start();

        /**
         * TODO: Have to work on that part later on. I've just commented out for now
         */

//        checkunattitems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!dbH.isoffladedline(InvoiceNo)) {
//                    final Dialog dialog = new Dialog(InvoiceDetails.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
//                    dialog.setContentView(R.layout.summarry);
//
//                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//                    ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloaded(InvoiceNo);
//                    items2 = new ArrayList<Item>();
//                    items2Checked = new ArrayList<Item>();
//
//                    for (OrderLines orderAttributes : oD) {
//
//                        Log.e("ontheitem", "////////////////////////////" + orderAttributes.getblnoffloaded());
//
//                        if (orderAttributes.getblnoffloaded() == "0") {
//                            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
//                                    "", "1", "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
//                            items2.add(item);
//                        }
//                        if (orderAttributes.getblnoffloaded() == "1") {
//                            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(), orderAttributes.getQty(),
//                                    "", "1", "Lines", orderAttributes.getblnoffloaded(), orderAttributes.getOrderDetailId());
//                            items2Checked.add(item);
//                        }
//
//
//                    }
//
//                    myItemsListAdapter2 = new ItemsListAdapter(InvoiceDetails.this, items2);
//                    myItemsListAdapter2Checked = new ItemsListAdapter(InvoiceDetails.this, items2Checked);
//                    listviewsummary = (ListView) dialog.findViewById(R.id._orderdlistlines);
//
//                    invoice_nosummary = (TextView) dialog.findViewById(R.id.invoice_no);
//                    closelinessummary = (Button) dialog.findViewById(R.id.closelines);
//                    acceptthesummary = (Button) dialog.findViewById(R.id.acceptthesummary);
//                    listviewsummary.setAdapter(myItemsListAdapter2);
//                    closelinessummary.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    acceptthesummary.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.e("this", "++++++++++++++++++++++++++++++++++++****************************l");
//                            Intent b = new Intent(InvoiceDetails.this, InvoiceDetails.class);
//                            b.putExtra("deldate", deldate);
//                            b.putExtra("routes", route);
//                            b.putExtra("ordertype", ordertype);
//                            b.putExtra("invoiceno", InvoiceNo);
//                            b.putExtra("cash", cash.getText().toString());
//                            startActivity(b);
//                        }
//                    });
//                    listviewsummary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                        @Override
//                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            final Item selectedItem_line = (Item) (parent.getItemAtPosition(position));
//
//                            Intent lineEdit = new Intent(InvoiceDetails.this, LineEdit.class);
//
//                            lineEdit.putExtra("deldate", deldate);
//                            lineEdit.putExtra("routes", route);
//                            lineEdit.putExtra("ordertype", ordertype);
//                            lineEdit.putExtra("orderdetailId", selectedItem_line.ItemString8);
//                            lineEdit.putExtra("invoiceno", InvoiceNo);
//                            startActivity(lineEdit);
//                            return true;
//                        }
//                    });
//                    dialog.show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "You have attended all the Items ",
//                            Toast.LENGTH_LONG).show();
//                }
//
//                //checkAndUncheck(list.get(position).ItemString6, cb.isChecked(),list.get(position).ItemString8);
//            }
//        });

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                final Dialog dialogView = new Dialog(InvoiceDetails.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                dialogView.setContentView(R.layout.cancel_order_dialog);
      /*  TextView textView = new TextView(context);
        textView.setText();*/
                dialogView.setTitle("Are you sure the customer is cancelling this order ?Please give a reason why.");
                dialogView.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.dismiss();
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // DO SOMETHINGS
                        ArrayList<OrderLines> oD = dbH.returnOrderLinesoffloaded(InvoiceNo);

                        for (OrderLines orderAttributes : oD) {

                            dbH.updateDeals("Update OrderLines set blnoffloaded=1,Uploaded=0 , offLoadComment='CANCELLED' , returnQty =" + orderAttributes.getQty() + ", strCustomerReason ='" + editText.getText().toString() + "'   where OrderDetailId = " + Integer.parseInt(orderAttributes.getOrderDetailId()));

                        }
                        //
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        final String idTimestamp = subscriberId + "-" + ts;

                        String message = InvoiceNo + " CANCELLED " + editText.getText().toString();
                        dbH.InserMessage(idTimestamp, message);
                        Intent b = new Intent(InvoiceDetails.this, InvoiceDetails.class);
                        b.putExtra("deldate", deldate);
                        b.putExtra("routes", route);
                        b.putExtra("ordertype", ordertype);
                        b.putExtra("invoiceno", InvoiceNo);
                        b.putExtra("cash", cash.getText().toString());
                        startActivity(b);
                    }
                });

                //dialogView.setView(dialogView);
                dialogView.show();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "back press",
                    Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
    }

    public static boolean containsIgnoreCase(String str, char c) {
        str = str.toLowerCase();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == Character.toLowerCase(c)) {
                return true;
            }
        }

        return false;
    }

    public boolean isFilePresent(String fileName) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public void checkAndUncheck(String tableName, boolean status, String detaild) {
        if (tableName.equals("Lines")) {
            Log.e("sql", "checkbox*******" + "Update OrderLines set blnoffloaded=" + status + " Where OrderDetailId=" + detaild);

            if (status) {
                dbH.updateDeals("Update OrderLines set blnoffloaded=1,Uploaded=0 Where OrderDetailId=" + detaild);
            } else {
                dbH.updateDeals("Update OrderLines set blnoffloaded=0,Uploaded=0 Where OrderDetailId=" + detaild);
            }

        }

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


        dbH.updateDeals("Update OrderHeaders set imagestring='" + s + "' where invoiceNo ='" + InvoiceNo + "'");
        // Log.e("********","***************"+s);
        //Log.e("********","***************InvoiceNo----"+InvoiceNo);
        //stream.close();
    }

    public void saveBitmapCash(Bitmap bitmap, String InvoiceNo) throws IOException {
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

        dbH.updateDeals("Update OrderHeaders set strCashsignature='" + s + "' where invoiceNo ='" + InvoiceNo + "'");
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

    public boolean addSignatureJpg(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            //   File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapCash(signature, invoiceNo);
            //scanMediaFile(photo);
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
        InvoiceDetails.this.sendBroadcast(mediaScanIntent);
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

    @Override
    public void onClick(View v) {
        // handle number button click
        Log.e("clicked", "Button clicked*****************************");
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            cashfielddialog.append(((TextView) v).getText());
            return;
        }
        switch (v.getId()) {
           /* case R.id.t9_key_clear: { // handle clear button
                mPasswordField.setText(null);
            }
            break;*/
            case R.id.t9_key_backspace: { // handle backspace button
                // delete one character
                Editable editable = cashfielddialog.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }

            break;
        }
    }

    private void setPairedDevices() {
        bondedDevices.clear();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device : bondedDeviceSet) {
            bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
        }

        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
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

    private void saveImage(Bitmap finalBitmap, String invoiceNo) {

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "";
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = invoiceNo + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class searchBLEPrinterTask extends AsyncTask<Integer, Integer, Set<BluetoothDevice>> {
        private String message;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Set<BluetoothDevice> bluetoothDeviceSet) {
            bondedDevices.clear();

            if (bluetoothDeviceSet.size() > 0) {
                for (BluetoothDevice device : bluetoothDeviceSet) {
                    if (ActivityCompat.checkSelfPermission(InvoiceDetails.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Can't found BLE devices. ", Toast.LENGTH_SHORT).show();
            }

            if (arrayAdapter != null) {
                arrayAdapter.notifyDataSetChanged();
            }

            if (message != null) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected Set<BluetoothDevice> doInBackground(Integer... params) {
            try {
                return BXLBluetoothLE.getBLEPrinters(InvoiceDetails.this, BXLBluetoothLE.SEARCH_BLE_ALWAYS);
            } catch (NumberFormatException | JposException e) {
                message = e.getMessage();
                return new HashSet<>();
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
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();
        // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        //  mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //   Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
//        currentlocation.setText(Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude()));
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
}

