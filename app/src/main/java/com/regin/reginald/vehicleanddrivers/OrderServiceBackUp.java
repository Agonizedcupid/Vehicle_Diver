package com.regin.reginald.vehicleanddrivers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.regin.reginald.model.OrderLines;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import android.os.Handler;

public class OrderServiceBackUp extends Service {
    public OrderServiceBackUp(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public OrderServiceBackUp() {
    }
    private static final String TAG = "DimsDriver";

    private boolean isRunning = false;
    //private SQLiteDatabase db1, db2, db3, db=null;
    private Cursor c, d;
    String strBarCode, strLon, strLat, strQty, strComments, date_created, strDeviceId, strImageName, strSentToServer, returnedBarCode, returnedToken, returnedstrImageName, strStorename;

    final Context myContext = this;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    String IP;//="http://192.168.0.18:8181/driver/";
    //String IP="http://so-ca.ddns.net:8179/driver/";
    //String IP="http://linxsystems3.dedicated.co.za:8881/driver/";

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand new");
        ArrayList<SettingsModel> sett= dbH.getSettings();

        for (SettingsModel orderAttributes: sett){
            IP = orderAttributes.getstrServerIp();
        }
        final Handler handler = new Handler();
        Timer time = new Timer(); // Instantiate Timer Object
        time.schedule(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        OrderHeaderPost();
                        OrderLinesPost();
                        ManagementPost();
                    }
                });
            }
        }, 0, 8000);

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
        // db.close();
    }
    public void OrderHeaderPost() {

        ArrayList<Orders> dealLineToUpload= dbH.getOrderHeadersNotUploaded();
        for (Orders orderAttributes: dealLineToUpload){

            String strNotesDrivers = "NULL";
            String strEmailAddress = "NULL";
            String strCashSig = "NULL";
            String strStartTime  = "NULL";
            String strEndTime  = "NULL";
            String strTheImage  = "NoImage";
            String signedBy  = "NULL";
            if (orderAttributes.getstrNotesDrivers() != null && !orderAttributes.getstrNotesDrivers().isEmpty())
            {
                strNotesDrivers = orderAttributes.getstrNotesDrivers();
            }
            if (orderAttributes.getstrEmailCustomer() != null && !orderAttributes.getstrEmailCustomer().isEmpty())
            {
                strEmailAddress = orderAttributes.getstrEmailCustomer();
            }
            if (orderAttributes.getstrCashsignature() != null && !orderAttributes.getstrCashsignature().isEmpty())
            {
                strCashSig = orderAttributes.getstrCashsignature();
            }

            if (orderAttributes.getStartTripTime() != null && !orderAttributes.getStartTripTime().isEmpty())
            {
                strStartTime = orderAttributes.getStartTripTime();
            }
            if (orderAttributes.getEndTripTime() != null && !orderAttributes.getEndTripTime().isEmpty())
            {
                strEndTime = orderAttributes.getEndTripTime();
            }
            if (orderAttributes.getimagestring() != null && !orderAttributes.getimagestring().isEmpty())
            {
                strTheImage = orderAttributes.getimagestring();
            }
            if (orderAttributes.getstrCustomerSignedBy() != null && !orderAttributes.getstrCustomerSignedBy().isEmpty())
            {
                signedBy = orderAttributes.getstrCustomerSignedBy();
            }
            Log.e("*****","********************************note "+ strEmailAddress);
            new UploadNewOrderLines(orderAttributes.getInvoiceNo(), orderAttributes.getLatitude(), orderAttributes.getLongitude(),
                    strTheImage,orderAttributes.getCashPaid(),strNotesDrivers,orderAttributes.getoffloaded(),strEmailAddress,strCashSig,strStartTime,strEndTime,orderAttributes.getDeliverySequence(),orderAttributes.getstrCoordinateStart(),signedBy).execute();
        }

    }
    public void OrderLinesPost() {

        ArrayList<OrderLines> dealLineToUpload= dbH.returnOrderLinesInfoUploaded();
        for (OrderLines orderAttributes: dealLineToUpload){

            //String orderDID, int offloaded, float returnQty,String offLoadComment,int blnoffloaded

            String returning = "NULL";
            String offcomment = "NULL";
            String reasons = "NULL";

            if (orderAttributes.getreturnQty() != null && !orderAttributes.getreturnQty().isEmpty())
            {
                returning = orderAttributes.getreturnQty();
            }
            if (orderAttributes.getoffLoadComment() != null && !orderAttributes.getoffLoadComment().isEmpty())
            {
                offcomment = orderAttributes.getoffLoadComment();
            }
            if (orderAttributes.getstrCustomerReason() != null && !orderAttributes.getstrCustomerReason().isEmpty())
            {
                reasons = orderAttributes.getstrCustomerReason();
            }
            new UploadNewOrderLinesDetails(orderAttributes.getOrderDetailId(), returning, offcomment,orderAttributes.getblnoffloaded(), reasons).execute();
        }

    }
    public void ManagementPost() {

        ArrayList<OtherAttributes> dealLineToUpload= dbH.managementConsole();
        for (OtherAttributes orderAttributes: dealLineToUpload){

            //String orderDID, int offloaded, float returnQty,String offLoadComment,int blnoffloaded

            new UploadCosoles(orderAttributes.getconMess(), orderAttributes.getconDocId(),  orderAttributes.getcondteTm(),Integer.toString(orderAttributes.getId()) ).execute();
        }

    }

    private class UploadNewOrderLines extends AsyncTask<Void, Void, Void> {

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


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadNewOrderLines(String invoice, String lat, String lon, String image,String cash, String note,String offload,String email,String strCashSig,String strStartTime,
                                   String strEndTime,String delseq,String strCoord,String signedBy) {
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


        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            Log.e("postIP","++++++++++++++++++++++++++++++++"+IP + "PostHeaders");
            Log.e("postIP","++++++++++++++++++++++++++++++++ signedBy" + signedBy);
            HttpPost httppost = new HttpPost(IP + "PostHeaders");
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

                Log.d("JSON", json.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is HEADERS**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID, strPartNumber;
                    ID = BoardDetails.getString("InvoiceNo");

                    Log.e("JSON-*", "RESPONSE IS HEADERS**: " + ID);
                    dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + ID + "'");
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


    private class UploadCosoles extends AsyncTask<Void, Void, Void> {
    String mess;
    String docid;
    String dtetm;
    String ids;


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    public UploadCosoles(String mess, String docid, String dtetm,String ids) {
        this.mess = mess;
        this.docid = docid;
        this.dtetm = dtetm;
        this.ids = ids;

    }


    @Override
    protected Void doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();

        //dbCreation();
        //}
        HttpPost httppost = new HttpPost(IP + "ManagementConsole.php");
        try {
            // Add your data

            JSONObject json = new JSONObject();
            json.put("mess", mess);
            json.put("docid", docid);
            json.put("dtetm", dtetm);
            json.put("ids", ids);


            Log.d("JSON", json.toString());
            List nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.e("JSON-*", "RESPONSE is CONSOLE**: " + responseBody);
            JSONArray BoardInfo = new JSONArray(responseBody);

            for (int j = 0; j < BoardInfo.length(); ++j) {

                JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                String ID, strPartNumber;
                ID = BoardDetails.getString("id");

                Log.e("JSON-*", "RESPONSE is CONSOLE**: " + ID);
                dbH.updateDeals("Delete from  ManagementConsole where id = " + ID );
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
    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {

        String orderDID;
        String returnQty;
        String offLoadComment;
        String blnoffloaded;
        String reasons;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadNewOrderLinesDetails(String orderDID,  String returnQty,String offLoadComment,String blnoffloaded,String reasons) {
            this.orderDID = orderDID;
            this.returnQty = returnQty;
            this.offLoadComment = offLoadComment;
            this.blnoffloaded = blnoffloaded;
            this.reasons = reasons;

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            HttpPost httppost = new HttpPost(IP + "PostLines");
            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("orderDID", orderDID);
                json.put("returnQty", returnQty);
                json.put("offLoadComment", offLoadComment);
                json.put("blnoffloaded", blnoffloaded);
                json.put("reasons", reasons);
                Log.e("blnoffloaded","*************+"+"****"+offLoadComment+"******"+returnQty);

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
                    ID = BoardDetails.getString("OrderDetailId");

                    Log.e("JSON-*", "RESPONSE is lines**: " + ID);
                    dbH.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId = '" + ID + "'");
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
