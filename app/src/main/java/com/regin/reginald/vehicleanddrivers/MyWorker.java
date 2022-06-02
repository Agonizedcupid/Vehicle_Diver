package com.regin.reginald.vehicleanddrivers;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.work.WorkerParameters;

import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.SettingsModel;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    String IP,DeviceID;

    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        ArrayList<SettingsModel> sett= dbH.getSettings();

        for (SettingsModel orderAttributes: sett){
            IP = orderAttributes.getstrServerIp();
            DeviceID = orderAttributes.getDeviceID();
        }
        new UploadNewOrderLinesDetails().execute();
        return Result.success();
    }



    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            OrderHeaderPost();

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
                ArrayList<OrderLines> dealLineToUpload= dbH.returnOrderLinesInfoUploaded();
                for (OrderLines orderAttributes: dealLineToUpload){
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

                        String c = offcomment;
                        Pattern pt = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
                        Matcher match = pt.matcher(c);
                        if (!match.matches()) {
                            c = c.replaceAll(pt.pattern(), " ");
                        }
                        offcomment = c;

                        String r = reasons;
                        Pattern ptr = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
                        Matcher matchr = ptr.matcher(r);
                        if (!matchr.matches()) {
                            r = r.replaceAll(ptr.pattern(), " ");
                        }
                    reasons = r;

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


                //     Log.d("JSON", finalInfo.toString());
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
                    strTheImage,orderAttributes.getCashPaid(),strNotesDrivers,orderAttributes.getoffloaded(),strEmailAddress,strCashSig,strStartTime,strEndTime,orderAttributes.getDeliverySequence(),orderAttributes.getstrCoordinateStart(),signedBy,orderAttributes.getLoyalty()).execute();
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
        String Loyalty;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        public UploadNewOrderLines(String invoice, String lat, String lon, String image,String cash, String note,String offload,String email,String strCashSig,String strStartTime,
                                   String strEndTime,String delseq,String strCoord,String signedBy,String Loyalty) {
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
            this.Loyalty = Loyalty;


        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            Log.e("postIP","++++++++++++++++++++++++++++++++"+IP + "PostHeadersLoyalty");
            Log.e("postIP","++++++++++++++++++++++++++++++++ signedBy" + signedBy);
            Log.e("Loyalty","++++++++++++++++++++++++++++++++ Loyalty" + Loyalty);
           // HttpPost httppost = new HttpPost(IP + "PostHeaders"); real
            HttpPost httppost = new HttpPost(IP + "PostHeadersLoyalty");
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
                json.put("Loyalty", Loyalty);

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


}