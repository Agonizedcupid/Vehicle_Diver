package com.regin.reginald.vehicleanddrivers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.OrdersActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CratesActivity extends AppCompatActivity {

    TextView savecrates;
    EditText cratedelivered, cratespickedup, cratesclaimed, referenceno;
    String deldate, routes, ordertype, SERVERIP, threshold, invoiceno, subscriberID, storename;
    TextView txtstorename, txtcount;
    int delivervspickpercent = 0;
    float createsCal = 0;
    //    private SQLiteDatabase db;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    private DatabaseAdapter databaseAdapter;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crates);
        //db = this.openOrCreateDatabase("LinxDriversOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        //ArrayList<SettingsModel> settIP= dbH.getSettings();
        databaseAdapter = new DatabaseAdapter(this);
        List<IpModel> list = databaseAdapter.getServerIpModel();

//        for (IpModel orderAttributes: settIP){
//            SERVERIP = orderAttributes.getstrServerIp();
//        }
        if (list.size() > 0) {
            SERVERIP = list.get(0).getServerIp();
        } else {
            SERVERIP = "";
        }
        Intent returndata = getIntent();

        invoiceno = returndata.getStringExtra("invoiceno");
        threshold = returndata.getStringExtra("threshold");
        storename = returndata.getStringExtra("storename");
        deldate = returndata.getStringExtra("deldate");
        routes = returndata.getStringExtra("routes");
        ordertype = returndata.getStringExtra("ordertype");


        savecrates = findViewById(R.id.savecrates);
        cratedelivered =  findViewById(R.id.cratedelivered);
        cratespickedup =  findViewById(R.id.cratespickedup);
        cratesclaimed =  findViewById(R.id.cratesclaimed);
        referenceno =  findViewById(R.id.referenceno);
        txtstorename =  findViewById(R.id.textView37);
        txtcount =  findViewById(R.id.textView38);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //int cratesToDeliver = dbH.returnOrderLinesCrateCount(invoiceno);
        int cratesToDeliver = databaseAdapter.returnOrderLinesCrateCount(invoiceno);
        txtstorename.setText(storename + "-" + invoiceno + " (" + cratesToDeliver + " )");


        savecrates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(cratedelivered.getText().toString())) {
                    cratedelivered.setError("Can't be Empty");
                    cratedelivered.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(cratespickedup.getText().toString())) {
                    cratespickedup.setError("Can't be Empty");
                    cratespickedup.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(cratesclaimed.getText().toString())) {
                    cratesclaimed.setError("Can't be Empty");
                    cratesclaimed.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(referenceno.getText().toString())) {
                    referenceno.setError("Can't be Empty");
                    referenceno.requestFocus();
                    return;
                }

                if (Integer.parseInt(cratedelivered.getText().toString()) < 1) {
                    delivervspickpercent = 100;
                } else {
                    createsCal = (Float.parseFloat(cratespickedup.getText().toString()) / Float.parseFloat(cratedelivered.getText().toString())) * 100;

                    delivervspickpercent = (int) (Math.round(createsCal));
                }

                if (delivervspickpercent < Integer.parseInt(threshold)) {
                    final AlertDialog.Builder dialogll = new AlertDialog.Builder(CratesActivity.this);
                    dialogll.setTitle("ERROR")
                            .setMessage("Your pick up is way less than what you are delivering. Are you sure?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    //db.execSQL("delete from StockTakeLines");

                                    paramDialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    //db.execSQL("delete from StockTakeLines");

                                    paramDialogInterface.dismiss();
                                }
                            });
                    dialogll.show();
                } else {
                    savecrates.setVisibility(View.INVISIBLE);

                    new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            txtcount.setText(" " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            txtcount.setText("Connection issue!");
                            savecrates.setVisibility(View.VISIBLE);
                            savecrates.setText("Please Retry");
                            progressDoalog.dismiss();
                        }
                    }.start();

                    startProgress("SAVING");
                    postCrates();
                }


            }
        });
    }

    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(CratesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("In Progress");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }

    public void postCrates() {

        try {
            String url = SERVERIP + "PostCrates.php";
            Log.e("url", "*****************" + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = response.toString();
                            result = result.replace("\"", "");
//                            Log.d("zzzz","res "+result);
                            //Toast.makeText(OrderConfirmationActivity.this, "over here" +response, Toast.LENGTH_SHORT).show();
                            progressDoalog.dismiss();
                            if (result.equals("SUCCESS")) {
                                Toast.makeText(CratesActivity.this, "  " + result, Toast.LENGTH_SHORT).show();
                                savecrates.setVisibility(View.INVISIBLE);
                                //Have to work on that:
                                //dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + invoiceno + "'");

                                new Thread() {
                                    public void run() {
                                        CratesActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                //Do your UI operations like dialog opening or Toast here

                                                final AlertDialog.Builder dialogll = new AlertDialog.Builder(CratesActivity.this);
                                                dialogll.setTitle("Done")
                                                        .setMessage("Crates Posted Successfully.")
                                                        .setPositiveButton("Alright", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                                //db.execSQL("delete from StockTakeLines");

                                                                //Intent i = new Intent(CratesActivity.this, MainActivity.class);
                                                                Intent i = new Intent(CratesActivity.this, OrdersActivity.class);
                                                                i.putExtra("deldate", deldate);
                                                                i.putExtra("routes", routes);
                                                                i.putExtra("ordertype", ordertype);
                                                                i.putExtra("edtkm_start", "0");
                                                                i.putExtra("edt_km_end", "0");
                                                                startActivity(i);
                                                            }
                                                        });
                                                dialogll.show();
                                            }
                                        });
                                    }
                                }.start();
                            } else {
                                Toast.makeText(CratesActivity.this, "in " + result, Toast.LENGTH_SHORT).show();
                                Log.e("result", "*****************************" + result);

                            }
                            //Log.i("Check", result);
                            //Log.i("Check", response);

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CratesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", error.getMessage());


                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject postData = new JSONObject();
                    try {

                        JSONObject json = new JSONObject();


                        json.put("pickedup", cratespickedup.getText().toString());
                        json.put("dropped", cratedelivered.getText().toString());
                        json.put("reference", referenceno.getText().toString());
                        json.put("Claim", cratesclaimed.getText().toString());


                        jsonArray.put(json);


                        postData.put("jsonobject", jsonArray);
                        Log.e("JSON", postData.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    subscriberID = ts + "-" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    params.put("pickedup", cratespickedup.getText().toString());
                    params.put("dropped", cratedelivered.getText().toString());
                    params.put("reference", referenceno.getText().toString());
                    params.put("Claim", cratesclaimed.getText().toString());
                    params.put("invoiceNo", invoiceno);
                    params.put("ticks", subscriberID);
                    params.put("route", routes);
                    params.put("deliverydate", deldate);
                    //params.put("jsonobject",postData.atoString());
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            //App.handleUncaughtException(e);
        }


    }
}