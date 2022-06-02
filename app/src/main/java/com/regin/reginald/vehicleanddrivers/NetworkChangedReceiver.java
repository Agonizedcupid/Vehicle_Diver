package com.regin.reginald.vehicleanddrivers;


    import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
    import android.os.AsyncTask;
    import android.os.Build;
import android.util.Log;

    import com.regin.reginald.model.OrderLines;
    import com.regin.reginald.model.Orders;
    import com.regin.reginald.model.OtherAttributes;
    import com.regin.reginald.model.SettingsModel;

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

    import java.io.IOException;
    import java.net.HttpURLConnection;
    import java.util.ArrayList;
    import java.util.Calendar;
import java.util.Date;
import java.util.List;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    import java.util.stream.IntStream;

import pk.codebase.requests.FormData;
import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

    public class NetworkChangedReceiver extends BroadcastReceiver {

        private Context mContext;

        private AlarmManager alarmMgr;
        private PendingIntent pendingIntent;
        private AlarmReciever alarmReciever;
        private int id = 0;
        String IP,DeviceID;
        final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());


        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            AppApplication.isInternetWorking();
            new android.os.Handler().postDelayed(() -> {
                try {
                    if (AppApplication.isNetworkAvailable(context)) {
                        AppApplication.hasInternet = true;
                        startRepeatingTask();
                        Log.e("NetworkAvailable", "-------------");

                    } else {
                        AppApplication.hasInternet = false;
                        stopRepeatingTask();
                        Log.e("Network Error", "-------------");
//
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }, 10000);
        }


        void stopRepeatingTask() {
            if (alarmReciever != null) {
                AppApplication.getContext().unregisterReceiver(alarmReciever);
                alarmReciever = null;
                alarmMgr.cancel(pendingIntent);
            }
        }

        void startRepeatingTask() {
            startAlarmManager();
        }

        public void startAlarmManager() {
            if (alarmReciever != null) {
                return;
            }
            alarmReciever = new AlarmReciever();
            IntentFilter filter = new IntentFilter("fire");
            AppApplication.getContext().registerReceiver(alarmReciever, filter);
            Intent dialogIntent = new Intent("fire");
            alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(mContext, id, dialogIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar time = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            Date date = new Date();
            time.setTime(date);
            cal_now.setTime(date);
            Log.e("NSTART ALARM", " " + "----------------------- ******************** ------------------------------ ******************* ----------------");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 8000, pendingIntent);
            } else if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, 800, 8000, pendingIntent);
            } else {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 8000, pendingIntent);
            }
        }

        private void getPackagesList() {
            new Thread(() -> {
                ArrayList<SettingsModel> sett= dbH.getSettings();

                for (SettingsModel orderAttributes: sett){
                    IP = orderAttributes.getstrServerIp();
                    DeviceID = orderAttributes.getDeviceID();
                }
                Log.e("NEW SERVICE METHOD", " " + "----------------------- ******************** ------------------------------ ******************* ----------------");
                pushToServer(  IP );
               // OrderHeaderPost();
             //   OrderLinesPost();
                ManagementPost();
                CreditLines();
                CreditHeader();
                PushCompleteTrip();
                PushExtraProducts();
                //THE QUERIES


            }).start();
        }

        public void pushToServer(String IPAddress ) {
          /*  HttpRequest request = new HttpRequest();
            request.setOnResponseListener(response -> {
                if (response.code == HttpResponse.HTTP_OK) {
                    Log.e("HTTP_OK", " " + "----------------------- ");

                } else if (response.code == HttpResponse.HTTP_BAD_REQUEST) {
                    Log.e("HTTP_BAD_REQUEST", " " + "----------------------- ");
                } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
                    Log.e("HTTP_NOT_FOUND", " " + "----------------------- ");
                }
            });
            request.setOnErrorListener(error -> {
                switch (error.code) {
                    case HttpError.CONNECTION_TIMED_OUT:
                        break;
                    case HttpError.NETWORK_UNREACHABLE:
                        break;
                    case HttpError.INVALID_URL:
                        break;
                    case HttpError.UNKNOWN:
                        break;
                }
            });
            FormData formData;*/
            HttpRequest request = new HttpRequest();
            request.setOnResponseListener(response -> {
                if (response.code == HttpResponse.HTTP_OK) {
                    Log.e("HTTP_OK", "------------------------" + response.text);
                    try {
                        JSONArray jsonArray = response.toJSONArray();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String returnedIP = jsonObject.getString("IP");
                            Log.e("IP RETURNS", "------------------------" + returnedIP);

                            new Thread(() -> {

                                if(IPAddress.length() > 10)
                                {
                                    dbH.updateDeals("update tblSettings set strServerIp='"+returnedIP+"'");
                                }

                            }).start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (response.code == HttpResponse.HTTP_BAD_REQUEST) {

                } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
                }
            });
            request.setOnErrorListener(error -> {
                switch (error.code) {
                    case HttpError.CONNECTION_TIMED_OUT:
                        break;
                    case HttpError.NETWORK_UNREACHABLE:
                        break;
                    case HttpError.INVALID_URL:
                        break;
                    case HttpError.UNKNOWN:
                        break;
                }
            });

            String id = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e("id*******","***************************"+id);
            request.get("http://102.37.0.48/driversapp/getIp.php?id="+id);
           /* formData = new FormData();
            formData.put("DeviceID",a );

            request.post("http://102.37.0.48/driversappdemo/poststring.php", formData);*/
        }

        class AlarmReciever extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar time = Calendar.getInstance();
                Calendar cal_now = Calendar.getInstance();
                Date date = new Date();
                time.setTime(date);
                cal_now.setTime(date);
                time.set(Calendar.MILLISECOND, 8000);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                } else if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {

                } else {
                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 8000, pendingIntent);
                }
                getPackagesList();
            }
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
            new UploadNewOrderLinesDetails().execute();
        }
        public int countNumberOfLinesNeededToUpload()
        {
            int count = 0;


            return count;
        }
        public void CreditLines(){
            new UploadCreditNotesLines().execute();
        }
        public void CreditHeader(){
            new UploadCreditNotesHeaders().execute();
        }
        public void PushCompleteTrip(){
            new CompleteTrip().execute();
        }
        public void PushExtraProducts(){
            new PostExtraProducts().execute();
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
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
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
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
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

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            public UploadNewOrderLinesDetails(/*String orderDID,  String returnQty,String offLoadComment,String blnoffloaded,String reasons*/) {
            }


            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();

                //dbCreation();
                //}
                int count = 0;
                HttpPost httppost = new HttpPost(IP + "PostLinesNew.php");
                try {
                    // Add your data

                    JSONArray jsonArray = new JSONArray();
                    ArrayList<com.regin.reginald.model.OrderLines> dealLineToUpload= dbH.returnOrderLinesInfoUploaded();
                    for (com.regin.reginald.model.OrderLines orderAttributes: dealLineToUpload){
                        JSONObject json = new JSONObject();
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


                        json.put("orderDID", orderAttributes.getOrderDetailId());
                        json.put("returnQty", returning);
                        json.put("offLoadComment", offcomment);
                        json.put("blnoffloaded", orderAttributes.getblnoffloaded());
                        json.put("reasons", reasons);
                        Log.e("blnoffloaded","*************+"+"****"+orderAttributes.getblnoffloaded()+"******"+returning);
                        Log.e("offcomment","*************+"+"****"+offcomment);
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
                    responseBody =  responseBody.replaceAll("\"", "");
                    Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);   //The response
                    Log.e("sql", "UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");
                    //JSONArray BoardInfo = new JSONArray(responseBody);
                    dbH.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");

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

        private class UploadCreditNotesLines extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }

            public UploadCreditNotesLines() {


            }


            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();

                //dbCreation();
                //}
                HttpPost httppost = new HttpPost(IP + "PostLinesCreditRequests.php");
                try {
                    // Add your data

                    JSONArray jsonArray = new JSONArray();
                    ArrayList<com.regin.reginald.model.OrderLines> dealLineToUpload= dbH.ReturnUnPostedCreditLines();
                    for (OrderLines orderAttributes: dealLineToUpload){
                        JSONObject json = new JSONObject();


                        json.put("Qty", orderAttributes.getQty());
                        json.put("weights", orderAttributes.getreturnQty());
                        json.put("DeliveryDate", orderAttributes.getstrCustomerReason());
                        json.put("Reference", orderAttributes.getComment()); //this is the ID
                        json.put("productname", orderAttributes.getPastelCode());
                        json.put("Notes", orderAttributes.getoffLoadComment());
                        json.put("id", orderAttributes.getId());

                        jsonArray.put(json);

                    }

                    JSONObject finalInfo = new JSONObject();
                    finalInfo.put("jsonobject", jsonArray);


                    Log.d("JSON", finalInfo.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    responseBody =  responseBody.replaceAll("\"", "");
                    Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);
                    Log.e("sql", "UPDATE  tblCreditNotesLines SET Uploaded = 1 where id in( " + responseBody + ")");
                    //JSONArray BoardInfo = new JSONArray(responseBody);
                    dbH.updateDeals("UPDATE  tblCreditNotesLines SET Uploaded = 1 where id in( " + responseBody + ")");


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

        private class CompleteTrip extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
            public CompleteTrip() {


            }

            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();

                //dbCreation();
                //}
                HttpPost httppost = new HttpPost(IP + "PostCompleteTrip.php");
                try {
                    // Add your data

                    JSONArray jsonArray = new JSONArray();

                    ArrayList<com.regin.reginald.model.OtherAttributes> dealLineToUpload= dbH.CompleteTrip();
                    for (OtherAttributes orderAttributes: dealLineToUpload){
                        JSONObject json = new JSONObject();

                        json.put("sealnumber", orderAttributes.getSealNumber());
                        json.put("routename", orderAttributes.getroute());
                        json.put("ordertypes", orderAttributes.getordertype());
                        json.put("kmout", orderAttributes.getkmout()); //this is the ID
                        json.put("kmdone", orderAttributes.getkmdone());
                        json.put("drivername", orderAttributes.getDriverName());
                        json.put("signature", orderAttributes.getsignature());
                        json.put("DelivDate", orderAttributes.getdeliverydate());
                        json.put("id", orderAttributes.getId());
                        json.put("DeviceID",DeviceID);
                        jsonArray.put(json);

                    }

                    JSONObject finalInfo = new JSONObject();
                    finalInfo.put("jsonobject", jsonArray);

                    Log.d("JSON", finalInfo.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    responseBody =  responseBody.replaceAll("\"", "");
                    Log.e("TripHeader-*", "RESPONSE is lines**: " + responseBody);
                    Log.e("sql", "UPDATE  TripHeader SET Uploaded = 1 where id in( " + responseBody + ")");
                    //JSONArray BoardInfo = new JSONArray(responseBody);
                    dbH.updateDeals("UPDATE  TripHeader SET Uploaded = 1 where id in( " + responseBody + ")");
                    dbH.updateDeals("Delete from  TripHeader where id in( " + responseBody + ")");
                    if (responseBody.equals("1"))
                    {

                    }else
                    {
                        dbH.updateDeals("Delete from  Filters");
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
        private class PostExtraProducts extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
            public PostExtraProducts() {


            }

            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();

                //dbCreation();
                //}
                HttpPost httppost = new HttpPost(IP + "PostExtraProducts.php");
                try {
                    // Add your data

                    JSONArray jsonArray = new JSONArray();

                    ArrayList<OrderLines> dealLineToUpload= dbH.returnExtraProductsToPost();
                    for (OrderLines orderAttributes: dealLineToUpload){
                        JSONObject json = new JSONObject();

                        json.put("ProductId", orderAttributes.getProductId());
                        json.put("InvoiceNo", orderAttributes.getOrderId());
                        json.put("Qty", orderAttributes.getQty());

                        json.put("id", orderAttributes.getId());

                        jsonArray.put(json);

                    }

                    JSONObject finalInfo = new JSONObject();
                    finalInfo.put("jsonobject", jsonArray);

                    Log.d("JSON", finalInfo.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    responseBody =  responseBody.replaceAll("\"", "");
                    Log.e("ExtraProductsToPost-*", "RESPONSE is lines**: " + responseBody);
                    Log.e("sql", "UPDATE  ExtraProductsToPost SET Uploaded = 1 where id in( " + responseBody + ")");
                    //JSONArray BoardInfo = new JSONArray(responseBody);
                    dbH.updateDeals("UPDATE  ExtraProductsToPost SET Uploaded = 1 where id in( " + responseBody + ")");
                    dbH.updateDeals("Delete from  ExtraProductsToPost where id in( " + responseBody + ")");


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
        private class UploadCreditNotesHeaders extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }

            public UploadCreditNotesHeaders() {


            }


            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();

                //dbCreation();
                //}
                HttpPost httppost = new HttpPost(IP + "PostHeadersCreditRequests.php");
                try {
                    // Add your data

                    JSONArray jsonArray = new JSONArray();
                    ArrayList<Orders> dealLineToUpload= dbH.ReturnUnPostedCreditHeaders();
                    for (Orders orderAttributes: dealLineToUpload){
                        JSONObject json = new JSONObject();


                        json.put("customer", orderAttributes.getStoreName());
                        json.put("referenceNumber", orderAttributes.getCustomerPastelCode());
                        json.put("signature", orderAttributes.getimagestring());
                        json.put("signedby", orderAttributes.getstrCustomerSignedBy()); //this is the ID
                        json.put("deliverydate", orderAttributes.getDeliveryDate());
                        json.put("id", orderAttributes.getId());
                        json.put("strEmail", orderAttributes.getstrEmailCustomer());
                        json.put("Drivername", orderAttributes.getDriverName());

                        jsonArray.put(json);

                    }

                    JSONObject finalInfo = new JSONObject();
                    finalInfo.put("jsonobject", jsonArray);


                    Log.d("JSON", finalInfo.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    responseBody =  responseBody.replaceAll("\"", "");
                    Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);
                    Log.e("sql", "UPDATE  tblCreditNotesHeader SET Uploaded = 1 where id in( " + responseBody + ")");
                    //JSONArray BoardInfo = new JSONArray(responseBody);
                    dbH.updateDeals("UPDATE  tblCreditNotesHeader SET Uploaded = 1 where id in( " + responseBody + ")");


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
