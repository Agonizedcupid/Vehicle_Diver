package com.regin.reginald.vehicleanddrivers;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bxl.config.editor.BXLConfigLoader;
import com.bxl.config.util.BXLBluetoothLE;
import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.OrderTypes;
import com.regin.reginald.model.SettingsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jpos.JposException;


public class PrinterFunctionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener {
    private final int REQUEST_PERMISSION = 0;
    private final String DEVICE_ADDRESS_START = " (";
    private final String DEVICE_ADDRESS_END = ")";

    private final ArrayList<CharSequence> bondedDevices = new ArrayList<>();
    private ArrayAdapter<CharSequence> arrayAdapter;

    private int portType = BXLConfigLoader.DEVICE_BUS_BLUETOOTH;
    private String logicalName = "";
    private String address = "";

    private LinearLayout layoutModel;
    private LinearLayout layoutIPAddress;

    private RadioGroup radioGroupPortType;
    private TextView textViewBluetooth;
    private ListView listView;
    private EditText editTextIPAddress;
    private CheckBox checkBoxAsyncMode;
    ProgressDialog progressDoalog;

    private CardView btnPrinterOpen;
    private CardView registerbtn, connectio_string, devicegroups;
    private ImageView delete_db;
    int len = 0;
    String customerOrders, SERVERIP, LINX = "http://102.37.0.48/driversapp/";// = "http://192.168.0.18:8181/driver/";
    private ProgressBar mProgressLarge;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    //private DatabaseHelper mDatabaseHelper;

    private ConstraintLayout snackBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_print);
        ArrayList<SettingsModel> settIP = dbH.getSettings();

        for (SettingsModel orderAttributes : settIP) {
            SERVERIP = orderAttributes.getstrServerIp();
        }
        Intent returndata = getIntent();
        layoutModel = findViewById(R.id.LinearLayout2);
        layoutIPAddress = findViewById(R.id.LinearLayout3);
        layoutIPAddress.setVisibility(View.GONE);

        textViewBluetooth = findViewById(R.id.textViewBluetoothList);
        checkBoxAsyncMode = findViewById(R.id.checkBoxAsyncMode);

        btnPrinterOpen = findViewById(R.id.btnPrinterOpen);
        registerbtn = findViewById(R.id.registerbtn);
        delete_db = findViewById(R.id.delete_db);
        connectio_string = findViewById(R.id.connectio_string);
        devicegroups = findViewById(R.id.devicegroups);
        btnPrinterOpen.setOnClickListener(this);

        snackBarLayout = findViewById(R.id.snackBarlayout);

        mProgressLarge = findViewById(R.id.progressBar1);
        mProgressLarge.setVisibility(ProgressBar.GONE);
        final String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        setPairedDevices();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bondedDevices);
        listView = findViewById(R.id.listViewPairedDevices);
        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        listView.setOnTouchListener(this);
        //mDatabaseHelper = DatabaseHelper.getHelper(this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress("...");
                try {
                    new registerTheDevice().execute(SERVERIP + "RegisterDevice.php?key=" + subscriberId);
                } catch (Exception var3) {

                }
            }
        });
        devicegroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress("...");
                try {
                    Log.e("skelere", "" + LINX + "GetGroupsFunctions.php?key=" + subscriberId);
                    new GetDeviceGroups().execute(LINX + "GetGroupsFunctions.php?key=" + subscriberId);
                } catch (Exception var3) {

                }
            }
        });
        connectio_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent s = new Intent(PrinterFunctionActivity.this, Settings.class);
                startActivity(s);
            }
        });

        delete_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbH.updateDeals("DROP TABLE IF EXISTS OrderTypes");
                dbH.updateDeals("DROP TABLE IF EXISTS PrinterInfo");
                dbH.updateDeals("DROP TABLE IF EXISTS Filters");
                dbH.updateDeals("DROP TABLE IF EXISTS ManagementConsole");
                dbH.updateDeals("DROP TABLE IF EXISTS OrderLines");
                dbH.updateDeals("DROP TABLE IF EXISTS OrderHeaders");
                Intent i = new Intent(PrinterFunctionActivity.this, LandingPage.class);
                startActivity(i);
            }
        });
        Spinner modelList = (Spinner) findViewById(R.id.spinnerModelList);

        ArrayAdapter modelAdapter = ArrayAdapter.createFromResource(this, R.array.modelList, android.R.layout.simple_spinner_dropdown_item);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelList.setAdapter(modelAdapter);
        modelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                logicalName = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                }
            }
        }
    }

    private void setPairedDevices() {
        bondedDevices.clear();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();

//        for (BluetoothDevice device : bondedDeviceSet) {
//            bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
//        }

        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void setBleDevices() {
        mHandler.obtainMessage(0).sendToTarget();
        BXLBluetoothLE.setBLEDeviceSearchOption(5, 1);
        new searchBLEPrinterTask().execute();
    }

    private class registerTheDevice extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                //lastmess
                progressDoalog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(PrinterFunctionActivity.this);
                builder.setTitle("Please talk to your manager about this ID.")
                        .setMessage("ID :" + customerOrders)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent main = new Intent(PrinterFunctionActivity.this, LandingPage.class);
                                startActivity(main);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private class GetDeviceGroups extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            dbH.updateDeals("DROP TABLE IF EXISTS tblGroupsetup");
            if (len > 0) {
                //lastmess

                try {
                    JSONArray BoardInfo = new JSONArray(customerOrders);
                    for (int j = 0; j < BoardInfo.length(); ++j) {

                        JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                        String results, OptionDesc, GroupID;
                        results = BoardDetails.getString("results");
                        OptionDesc = BoardDetails.getString("OptionDesc");
                        GroupID = BoardDetails.getString("GroupID");

                        if (results.equals("NOT REGISTERED")) {
                            //devicegroups.setText("Not Registered, please speak to your administrator");
                            Snackbar.make(snackBarLayout,"Not Registered, please speak to your administrator",
                                    Snackbar.LENGTH_LONG).show();
                        } else {

                            dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblGroupsetup (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,OptionDesc TEXT, GroupID ID)");
                            dbH.updateDeals("Insert into tblGroupsetup (OptionDesc,GroupID) values('" + OptionDesc + "','" + GroupID + "')");

                        }
                    }
                    progressDoalog.dismiss();

                } catch (Exception e) {
                    Log.e("JSON", e.getMessage());

                }
            }
        }
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
                    //bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
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

            mHandler.obtainMessage(1).sendToTarget();
        }

        @Override
        protected Set<BluetoothDevice> doInBackground(Integer... params) {
            try {
                return BXLBluetoothLE.getBLEPrinters(PrinterFunctionActivity.this, BXLBluetoothLE.SEARCH_BLE_ALWAYS);
            } catch (NumberFormatException | JposException e) {
                message = e.getMessage();
                return new HashSet<>();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioBT:
                portType = BXLConfigLoader.DEVICE_BUS_BLUETOOTH;
                textViewBluetooth.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                layoutIPAddress.setVisibility(View.GONE);

                setPairedDevices();
                break;
            case R.id.radioBLE:
                portType = BXLConfigLoader.DEVICE_BUS_BLUETOOTH_LE;
                textViewBluetooth.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                layoutIPAddress.setVisibility(View.GONE);

                setBleDevices();
                break;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP)
            listView.requestDisallowInterceptTouchEvent(false);
        else listView.requestDisallowInterceptTouchEvent(true);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String device = ((TextView) view).getText().toString();
        if (portType == BXLConfigLoader.DEVICE_BUS_WIFI) {
            editTextIPAddress.setText(device);
            address = device;
        } else {
            address = device.substring(device.indexOf(DEVICE_ADDRESS_START) + DEVICE_ADDRESS_START.length(), device.indexOf(DEVICE_ADDRESS_END));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrinterOpen:
                dbH.updateDeals("delete from PrinterInfo");
                dbH.updateDeals("INSERT INTO PrinterInfo (ProtoType,LogicalName,Address) VALUES ('" + portType + "','" + logicalName + "','" + address + "')");
                Intent a = new Intent(PrinterFunctionActivity.this, LandingPage.class);
                startActivity(a);
                /*Intent a =  new Intent(PrinterFunctionActivity.this,TestTextFragment.class);
                startActivity(a);
            /* mHandler.obtainMessage(1).sendToTarget();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (portType == BXLConfigLoader.DEVICE_BUS_WIFI) {
                            address = editTextIPAddress.getText().toString();
                        }

                        Log.e("result","*****************"+portType+"/////"+logicalName+"------"+address);

                        if (LandingPage.getPrinterInstance().printerOpen(portType, logicalName, address)) {
                            //finish();
                            Intent a =  new Intent(PrinterFunctionActivity.this,TestTextFragment.class);
                            startActivity(a);
                        } else {
                            mHandler.obtainMessage(1, 0, 0, "Fail to printer open!!").sendToTarget();
                        }
                    }
                }).start();
*/
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public final Handler mHandler = new Handler(new Handler.Callback() {
        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressLarge.setVisibility(ProgressBar.VISIBLE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    break;
                case 1:
                    String data = (String) msg.obj;
                    if (data != null && data.length() > 0) {
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                    }
                    mProgressLarge.setVisibility(ProgressBar.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    break;
            }
            return false;
        }
    });

    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(PrinterFunctionActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("Synchronizing data");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
}
