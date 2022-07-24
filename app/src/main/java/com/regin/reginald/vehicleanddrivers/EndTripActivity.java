package com.regin.reginald.vehicleanddrivers;

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
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.database.collection.LLRBNode;
import com.ibm.icu.text.BidiTransform;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.SettingsModel;

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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class EndTripActivity extends AppCompatActivity{
    //mainlayout

    LinearLayout ll;
    EditText editText,seal_number,kmdone,drivername;
    Button btnSyncOrd,submit,done;
    int len = 0;
    String customerOrders,SERVERIP;
    ProgressDialog progressDoalog;
    private DatabaseHelper mDatabaseHelper;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    int i = 0;
    List<String> list = new ArrayList<>();
    EditText[] textBoxes = new EditText[10];
    String dbRoute;
    String dbLateOrder;
    String dbDateFrom;
    private SignaturePad mSignaturePad;

    TextView txtdbRoute;
    TextView txtdbLateOrder;
    TextView txtdbDateFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrip);
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblExpenses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ExpenseId INTEGER, ExpenseName TEXT,GlCode TEXT)");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS postExpenses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ExpenseId INTEGER, ExpenseName TEXT)");
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        kmdone = (EditText) findViewById(R.id.kmdone) ;
        seal_number = (EditText) findViewById(R.id.seal_number) ;
        drivername = (EditText) findViewById(R.id.drivername) ;
        done = (Button) findViewById(R.id.done) ;
        ArrayList<OtherAttributes> oD = dbH.returnFilters();
        for (OtherAttributes orderAttributes : oD) {
            dbRoute = orderAttributes.getroute();
            dbLateOrder = orderAttributes.getordertype();
            dbDateFrom = orderAttributes.getdeliverydate();
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap,"")) {
                  //  startProgress("Ending Trip");//next version
                   // ValidatePODs(); // next version
                    Intent main = new Intent(EndTripActivity.this, LandingPage.class);
                    startActivity(main);
                }
            }
        });
        //GetDriversExpenses
      /*  ArrayList<OtherAttributes> expensesList = dbH.returnExpenses();
        ArrayList<SettingsModel> settIP = dbH.getSettings();
        for (SettingsModel orderAttributes : settIP) {
            SERVERIP = orderAttributes.getstrServerIp();
        }
        ArrayList<OtherAttributes> oD = dbH.returnFilters();
        for (OtherAttributes orderAttributes : oD) {
            dbRoute = orderAttributes.getroute();
            dbLateOrder = orderAttributes.getordertype();
            dbDateFrom = orderAttributes.getdeliverydate();
        }
        ll = (LinearLayout) findViewById(R.id.mainlayout);
        btnSyncOrd = new Button(this);
        submit = new Button(this);
        txtdbRoute = new TextView(this);

        btnSyncOrd.setText("Get Expenses");
        submit.setText("Submit");
        txtdbRoute.setText("TRIP :" +dbRoute+"Del Run "+dbLateOrder+" Date "+dbDateFrom);
        submit.setBackgroundColor(Color.BLUE);
        submit.setTextColor(Color.WHITE);
        ll.addView(btnSyncOrd);*/

        /*for(int i=0;i<5;i++)
        {
            editText = new EditText(this);
            editText.setHint("Geef een naam in");

            ll.addView(editText);
        }*/



       /* for (OtherAttributes orderAttributes : expensesList) {

            textBoxes[i] = new EditText(this);
            textBoxes[i].setHint(orderAttributes.getExpenseName());
            textBoxes[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ll.addView(textBoxes[i] );
            list.add(i, Integer.toString(orderAttributes.getExpenseId()));
            i++;
        }

        if(dbDateFrom.length() >7)
        {
            ll.addView(submit);
        }

        ll.addView(txtdbRoute);
       //Log.e("Expenses","------------------------------------------------"+ list.get(0));
      // Log.e("Expenses","----------------------------------------------"+ list.get(1));
        btnSyncOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress("...");
                Log.e("expe",""+SERVERIP + "GetDriversExpenses.php");
                new Expenses().execute(SERVERIP + "GetDriversExpenses.php");

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Expenses","----------------------------------------"+  textBoxes[0].getText().toString());
                dbH.updateDeals("delete from postExpenses");
                for(int k = 0;k< i ;k++)
                {
                  //  Log.e("Expenses","----------------------------------------"+  textBoxes[k].getText().toString());
                    if((textBoxes[k].getText().toString().trim().length()) > 0)
                    {
                        dbH.updateDeals("Insert into postExpenses(ExpenseId,ExpenseName) values('"+list.get(k)+"','"+textBoxes[k].getText().toString()+"')");
                    }

                }
                submit.setVisibility(View.INVISIBLE);
                new uploadExpenses().execute();
                //post functions
            }
        });*/
    }

    public void ValidatePODs()
    {
        new CheckPostedPod().execute();
    }
    private class Expenses extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Log.e("do in bckgrnd", "has been reached");
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
            //  Log.e("onpostexecute", "has been reached");
            Log.e("info","------------------"+customerOrders);

            if (len > 0) {


                try {
                    JSONArray BoardInfo = new JSONArray(customerOrders);
                    Log.e("info","------------------"+customerOrders);
                    dbH.updateDeals("DROP TABLE IF EXISTS tblExpenses");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS tblExpenses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ExpenseId INTEGER, ExpenseName TEXT,GlCode TEXT)");
                    progressDoalog.dismiss();
                    for (int j = 0; j < BoardInfo.length(); ++j) {

                        JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                        String ExpenseId,ExpenseName,GlCode;
                        ExpenseId = BoardDetails.getString("intExpenseId");
                        ExpenseName = BoardDetails.getString("strExpenseName");
                        GlCode = BoardDetails.getString("strGlCode");
                            dbH.updateDeals("insert into tblExpenses (ExpenseId,ExpenseName,GlCode) values('"+ExpenseId+"','"+ExpenseName+"','"+GlCode+"')");
                    }
                    Intent h = new Intent(EndTripActivity.this,EndTripActivity.class);
                    startActivity(h);

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

        try{
            url = new URL(urlp);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Initialize a new string buffer object
            StringBuffer stringBuffer = new StringBuffer();

            String line ="";
            // Loop through the lines
            while((line= bufferedReader.readLine())!=null){
                // Append the current line to string buffer
                stringBuffer.append(line);
            }

            movieJsonStr =  stringBuffer.toString();

        } catch (Throwable e) {
            Log.e("backgroundtask", "EXCEPTION", e);
        } finally {
            connection.disconnect();
            try {


                if(bufferedReader != null)
                    bufferedReader.close();
                if(inputStream != null)
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


    private class uploadExpenses extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(EndTripActivity.this,LandingPage.class);
            startActivity(i);
        }

        public uploadExpenses( ) {

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            HttpPost httppost = new HttpPost(SERVERIP + "postExpenses.php");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                //returnSavedExpenses()
                ArrayList<OtherAttributes> expensesList = dbH.returnSavedExpenses();

                for (OtherAttributes orderAttributes : expensesList) {
                    JSONObject json = new JSONObject();

                    json.put("id", Integer.toString(orderAttributes.getExpenseId()));
                    json.put("tabletId", Integer.toString(orderAttributes.getId()));
                    json.put("expenseval", orderAttributes.getExpenseName());
                    json.put("deldate", dbDateFrom);
                    json.put("ordertype", dbLateOrder);
                    json.put("route", dbRoute);

                    jsonArray.put(json);
                }
                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);


                Log.e("reginald", finalInfo.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is CONSOLE**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);
                dbH.updateDeals("Delete from  postExpenses where id in (" + responseBody+")" );

              /*  for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID, strPartNumber;
                    ID = BoardDetails.getString("id");

                    Log.e("JSON-*", "RESPONSE is CONSOLE**: " + ID);

                }*/

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
    private class CheckPostedPod extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public CheckPostedPod( ) {

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            HttpPost httppost = new HttpPost(SERVERIP + "postOrdersToCheckIfUploaded.php");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                //returnSavedExpenses()
                ArrayList<Orders> expensesList = dbH.getOrderHeadersPosted();

                for (Orders orderAttributes : expensesList) {
                    JSONObject json = new JSONObject();
                    json.put("OrderId", orderAttributes.getOrderId());
                    jsonArray.put(json);
                }
                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);


                Log.e("reginald", finalInfo.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is CONSOLE**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);
                if(responseBody.trim().length() > 3)
                {
                    new Thread()
                    {
                        public void run()
                        {
                            EndTripActivity.this.runOnUiThread(new Runnable()
                            {
                    public void run()
                    {
                        //Do your UI operations like dialog opening or Toast here

                        final  AlertDialog.Builder dialogll = new  AlertDialog.Builder(EndTripActivity.this);
                        dialogll.setTitle("Validation Failed")
                                .setMessage(""+responseBody)
                                .setPositiveButton("Check Why", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                        ArrayList<OtherAttributes> oD = dbH.returnFilters();
                                        for (OtherAttributes orderAttributes : oD) {
                                            dbRoute = orderAttributes.getroute();
                                            dbLateOrder = orderAttributes.getordertype();
                                            dbDateFrom = orderAttributes.getdeliverydate();
                                            Intent i = new Intent(EndTripActivity.this, OrderNotUploadedActivity.class);
                                            i.putExtra("deldate", dbDateFrom);
                                            i.putExtra("routes", dbRoute);
                                            i.putExtra("ordertype", dbLateOrder);
                                            startActivity(i);
                                        }
                                    }
                                })
                                ;
                        dialogll.show();
                    }
                            });
                        }
                    }.start();
                }else
                {

                }
               // dbH.updateDeals("Delete from  postExpenses where id in (" + responseBody+")" );
              /*  for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID, strPartNumber;
                    ID = BoardDetails.getString("id");

                    Log.e("JSON-*", "RESPONSE is CONSOLE**: " + ID);

                }*/

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
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(EndTripActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("I am checking yoursomething for you");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent i = new Intent(EndTripActivity.this,LandingPage.class);
        startActivity(i);
    }

    public boolean addJpgSignatureToGallery(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo,invoiceNo);
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
        EndTripActivity.this.sendBroadcast(mediaScanIntent);
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
    public void saveBitmapToJPG(Bitmap bitmap, File photo,String InvoiceNo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //  OutputStream stream = new FileOutputStream(photo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteImage =outputStream.toByteArray();
        String s = Base64.encodeToString(byteImage,Base64.DEFAULT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tomorrowDate = dateFormat.format(tomorrow);

        try{
            Log.e("updating","*********************************************************************************update order header");
            dbH.updateDeals("Update TripHeader set KmEnd='"+kmdone.getText().toString()+"', DriverCompleteSignature='"+s+"', SealNumber='"+ seal_number.getText().toString()+"', DriverName='"+drivername.getText().toString()+"', Completed=1 where RouteName ='"+dbRoute+"' and OrderType='"+dbLateOrder+"' and DelDate='"+dbDateFrom+"'");
        }catch (Exception e)
        {

        }

    }
}
