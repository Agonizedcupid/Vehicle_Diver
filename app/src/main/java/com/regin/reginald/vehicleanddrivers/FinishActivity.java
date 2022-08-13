package com.regin.reginald.vehicleanddrivers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FinishActivity extends AppCompatActivity {
    //final Handler handler;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    String InvoiceNo, Status, deldate, ordertype, route;
    TextView post_status, cust_email, signedby, productextras;
    Button donewiththeorder;
    LinearLayout ll;
    EditText temp;
    int i = 0;

    List<String> list = new ArrayList<>();
    String IP, DeviceID;

    EditText[] textBoxes = new EditText[4];
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        post_status = findViewById(R.id.post_status);
        cust_email = findViewById(R.id.cust_email);
        signedby = findViewById(R.id.signedby);
        temp = findViewById(R.id.temp);
        donewiththeorder = (Button) findViewById(R.id.donewiththeorder);
        Intent returndata = getIntent();
        InvoiceNo = returndata.getStringExtra("invoiceno");
        signedby.setText(returndata.getStringExtra("signedby"));
        cust_email.setText(returndata.getStringExtra("email"));
        deldate = returndata.getStringExtra("deldate");
        ordertype = returndata.getStringExtra("ordertype");
        route = returndata.getStringExtra("routes");
        ll = findViewById(R.id.mainlayout);
        productextras = new TextView(this);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<SettingsModel> sett = dbH.getSettings();

        for (SettingsModel orderAttributes : sett) {
            IP = orderAttributes.getstrServerIp();
            DeviceID = orderAttributes.getDeviceID();
        }
        ArrayList<OrderLines> expensesList = dbH.returnExtraProducts();
        for (OrderLines orderAttributes : expensesList) {

            textBoxes[i] = new EditText(this);
            textBoxes[i].setHint(orderAttributes.getPastelDescription());
            //textBoxes[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ll.addView(textBoxes[i]);
            list.add(i, orderAttributes.getProductId());
            i++;
        }

       /* if (i> 0)
        {
            productextras.setText("Please Fill In The Required Info");
            productextras.setTextColor(Color.BLACK);
            ll.addView(productextras);
        }*/

//        donewiththeorder = new Button(this);
//
//
//        donewiththeorder.setText("Submit");
//
//        donewiththeorder.setBackgroundColor(Color.GREEN);
//        donewiththeorder.setTextColor(Color.BLACK);
//        ll.addView(donewiththeorder);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {
                //  Status = dbH.islineUploaded(InvoiceNo);
                Log.e("*******", "-------------------------------" + dbH.islineUploaded(InvoiceNo));
                post_status.setText("" + dbH.islineUploaded(InvoiceNo));
                while (dbH.islineUploaded(InvoiceNo) != "Order posted to the office") {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            post_status.setText("" + Status);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
        post_status.setText("" + dbH.islineUploaded(InvoiceNo));
        donewiththeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (temp.getText().toString().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FinishActivity.this);
                    builder
                            .setTitle("Fridge Temperature")
                            .setMessage("Please put in the fridge temperature.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    temp.requestFocus();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (isInternetAvailable()) {
                        //Toast.makeText(FinishActivity.this, "You Are Connected " + InvoiceNo, Toast.LENGTH_SHORT).show();
                        startProgress("Posting The Transaction.");
                        new UploadNewOrderLinesDetails().execute();

                    } else {
                        Toast.makeText(FinishActivity.this, "Turn On the internet!", Toast.LENGTH_SHORT).show();

                        for (int k = 0; k < i; k++) {
                            //  Log.e("Expenses","----------------------------------------"+  textBoxes[k].getText().toString());
                            if ((textBoxes[k].getText().toString().trim().length()) > 0) {
                                dbH.updateDeals("Insert into ExtraProductsToPost(ProductId,Qty,InvoiceNo) values('" + list.get(k) + "','" + textBoxes[k].getText().toString() + "','" + InvoiceNo + "')");
                            }

                        }

                        Intent main = new Intent(FinishActivity.this, MainActivity.class);
                        main.putExtra("deldate", deldate);
                        main.putExtra("routes", route);
                        main.putExtra("ordertype", ordertype);
                        //freeze temperature.
                        main.putExtra("temp", temp.getText().toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, 0);
                        Date tomorrow = calendar.getTime();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
                        String tomorrowDate = dateFormat.format(tomorrow);
                        try {
                            dbH.updateDeals("Insert into ManagementConsole (Messages,DocID,datetimes) values('Drivers App ,User Pressed Finished','" + InvoiceNo + "','" + tomorrowDate + "')");
                        } catch (Exception e) {

                        }

                        startActivity(main);
                    }
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public UploadNewOrderLinesDetails() {
        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            Log.d("TESTING_THINGS", "doInBackground: " + IP + "PostLinesNew.php");
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "PostLinesNew.php");
            try {
                // Add your data

                JSONArray jsonArray = new JSONArray();
                ArrayList<com.regin.reginald.model.OrderLines> dealLineToUpload = dbH.returnOrderLinesInfoUploadedByInvoice(InvoiceNo);
                for (com.regin.reginald.model.OrderLines orderAttributes : dealLineToUpload) {
                    JSONObject json = new JSONObject();
                    //String orderDID, int offloaded, float returnQty,String offLoadComment,int blnoffloaded

                    String returning = "NULL";
                    String offcomment = "NULL";
                    String reasons = "NULL";

                    if (orderAttributes.getreturnQty() != null && !orderAttributes.getreturnQty().isEmpty()) {
                        returning = orderAttributes.getreturnQty();
                    }
                    if (orderAttributes.getoffLoadComment() != null && !orderAttributes.getoffLoadComment().isEmpty()) {
                        offcomment = orderAttributes.getoffLoadComment();
                    }
                    if (orderAttributes.getstrCustomerReason() != null && !orderAttributes.getstrCustomerReason().isEmpty()) {
                        reasons = orderAttributes.getstrCustomerReason();
                    }


                    json.put("orderDID", orderAttributes.getOrderDetailId());
                    json.put("returnQty", returning);
                    json.put("offLoadComment", offcomment);
                    json.put("blnoffloaded", orderAttributes.getblnoffloaded());
                    json.put("reasons", reasons);
                    Log.e("blnoffloaded", "*************+" + "****" + orderAttributes.getblnoffloaded() + "******" + returning);
                    Log.e("offcomment", "*************+" + "****" + offcomment);
                    jsonArray.put(json);
                    count++;

                }

                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);

                Log.e("JSON", finalInfo.toString()); // before pos
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                responseBody = responseBody.replaceAll("\"", "");
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);   //The response
                Log.e("sql", "UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");
                //JSONArray BoardInfo = new JSONArray(responseBody);
                dbH.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");


                Intent main = new Intent(FinishActivity.this, MainActivity.class);
                main.putExtra("deldate", deldate);
                main.putExtra("routes", route);
                main.putExtra("ordertype", ordertype);
                main.putExtra("temp", temp.getText().toString());
                startActivity(main);

            } catch (Exception e) {
                Log.e("JSON_ERROR", e.getMessage());
            }
            // db.close();
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

    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(FinishActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("Busy Saving.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
}
