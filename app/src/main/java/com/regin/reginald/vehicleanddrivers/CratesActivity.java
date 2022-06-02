package com.regin.reginald.vehicleanddrivers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CratesActivity extends AppCompatActivity {

    Button savecrates;
    EditText cratedelivered,cratespickedup,cratesclaimed,referenceno;
    String deldate,routes,ordertype,SERVERIP,threshold,invoiceno,subscriberID,storename;
    TextView txtstorename,txtcount;
    int delivervspickpercent = 0;
    float createsCal = 0;
    private SQLiteDatabase db;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crates);
        db = this.openOrCreateDatabase("LinxDriversOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        ArrayList<SettingsModel> settIP= dbH.getSettings();

        for (SettingsModel orderAttributes: settIP){
            SERVERIP = orderAttributes.getstrServerIp();
        }
        Intent returndata = getIntent();

        invoiceno = returndata.getStringExtra("invoiceno");
        threshold = returndata.getStringExtra("threshold");
        storename = returndata.getStringExtra("storename");
        deldate = returndata.getStringExtra("deldate");
        routes = returndata.getStringExtra("routes");
        ordertype = returndata.getStringExtra("ordertype");


        savecrates = (Button) findViewById(R.id.savecrates);
        cratedelivered = (EditText) findViewById(R.id.cratedelivered);
        cratespickedup = (EditText) findViewById(R.id.cratespickedup);
        cratesclaimed = (EditText) findViewById(R.id.cratesclaimed);
        referenceno = (EditText) findViewById(R.id.referenceno);
        txtstorename = (TextView) findViewById(R.id.textView37);
        txtcount = (TextView) findViewById(R.id.textView38);
        int cratesToDeliver = dbH.returnOrderLinesCrateCount(invoiceno);
        txtstorename.setText(storename+"-"+invoiceno+" ("+cratesToDeliver+" )");


        savecrates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(cratedelivered.getText().toString()) < 1){
                    delivervspickpercent = 100;
                }else{
                    createsCal = (Float.parseFloat(cratespickedup.getText().toString())/Float.parseFloat(cratedelivered.getText().toString()))*100;

                    delivervspickpercent =(int)(Math.round(createsCal));
                }

                if(delivervspickpercent< Integer.parseInt(threshold)){
                    final  AlertDialog.Builder dialogll = new  AlertDialog.Builder(CratesActivity.this);
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
                }else{
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
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(CratesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("In Progress");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
    public void postCrates(){

        try {
            String url = SERVERIP+"PostCrates.php";
            Log.e("url","*****************"+url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = response.toString();
                            result = result.replace("\"","");
//                            Log.d("zzzz","res "+result);
                            //Toast.makeText(OrderConfirmationActivity.this, "over here" +response, Toast.LENGTH_SHORT).show();
                            progressDoalog.dismiss();
                            if(result.equals("SUCCESS")){
                                Toast.makeText(CratesActivity.this, "  " +result, Toast.LENGTH_SHORT).show();
                                savecrates.setVisibility(View.INVISIBLE);
                                dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + invoiceno + "'");

                                new Thread()
                                {
                                    public void run()
                                    {
                                        CratesActivity.this.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                //Do your UI operations like dialog opening or Toast here

                                                final  AlertDialog.Builder dialogll = new  AlertDialog.Builder(CratesActivity.this);
                                                dialogll.setTitle("Done")
                                                        .setMessage("Crates Posted Successfully.")
                                                        .setPositiveButton("Alright", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                                //db.execSQL("delete from StockTakeLines");

                                                                Intent i = new Intent(CratesActivity.this,MainActivity.class );
                                                                i.putExtra("deldate",deldate);
                                                                i.putExtra("routes",routes);
                                                                i.putExtra("ordertype",ordertype);
                                                                i.putExtra("edtkm_start","0");
                                                                i.putExtra("edt_km_end","0");
                                                                startActivity(i);
                                                            }
                                                        });
                                                dialogll.show();
                                            }
                                        });
                                    }
                                }.start();
                            }else{
                                Toast.makeText(CratesActivity.this, "in " +result, Toast.LENGTH_SHORT).show();
                                Log.e("result","*****************************"+result);

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
                        json.put("Claim",  cratesclaimed.getText().toString());


                        jsonArray.put(json);


                        postData.put("jsonobject", jsonArray);
                        Log.e("JSON", postData.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    subscriberID = ts+"-"+android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    params.put("pickedup", cratespickedup.getText().toString());
                    params.put("dropped", cratedelivered.getText().toString());
                    params.put("reference", referenceno.getText().toString());
                    params.put("Claim",  cratesclaimed.getText().toString());
                    params.put("invoiceNo",  invoiceno);
                    params.put("ticks",  subscriberID);
                    params.put("route",  routes);
                    params.put("deliverydate",  deldate);
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