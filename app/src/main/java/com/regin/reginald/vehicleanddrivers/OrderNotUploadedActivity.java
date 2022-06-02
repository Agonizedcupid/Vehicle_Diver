package com.regin.reginald.vehicleanddrivers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.regin.reginald.model.OrderLines;

public class OrderNotUploadedActivity extends AppCompatActivity {

    public class Item {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;


        Item(String t, String t2, String t3,String t4) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;

        }
    }

    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;
        TextView button1;

    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.notuploadedstop_row, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.inv_no);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.custname);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.deladdress);
                viewHolder.button1 = (TextView) rowView.findViewById(R.id.retrythisnow);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    List<Item> items1;
    ItemsListAdapter myItemsListAdapter1 ;

    int len = 0;
    TextView no_of_stops_mess;
    ListView lvordersnotuploaded;
    Button cont_savedfilters;
    String IP,DeviceID;
    ProgressDialog progressDoalog;
    String deliverdate,ordertype,routename,LINX = "http://102.37.0.48/driversapp/", customerOrders,answer;
    private OneTimeWorkRequest mWorkRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_not_uploaded);

        no_of_stops_mess = (TextView) findViewById(R.id.no_of_stops_mess);
        lvordersnotuploaded = (ListView) findViewById(R.id.lvordersnotuploaded);
        cont_savedfilters = (Button) findViewById(R.id.cont_savedfilters);
        cont_savedfilters.setBackgroundColor(Color.GREEN);

        Intent returndata = getIntent();
        deliverdate = returndata.getStringExtra("deldate");
        ordertype = returndata.getStringExtra("ordertype");
        routename = returndata.getStringExtra("routes");
        ArrayList<SettingsModel> sett= dbH.getSettings();

        for (SettingsModel orderAttributes: sett){
            IP = orderAttributes.getstrServerIp();
            DeviceID = orderAttributes.getDeviceID();
        }

        new UploadNewOrderLinesDetails().execute();
        new pullthetrigger().execute("http://102.37.0.48/firebaseidchecker/send.php");


        mWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        if(dbH.selectCountNotUploaded()>0)
        {
            no_of_stops_mess.setText(""+dbH.selectCountNotUploaded()+ "Invoice(s) Not Posted, Retry Posting");
        }


        if(dbH.checkiflinesuploaded()>0)
        {
           //
            OrderHeaderPost();
        }



        items1 = new ArrayList<Item>();
        ArrayList<Orders> oH= dbH.returnOrderHeadersNotUploaded();
        for (Orders orderAttributes: oH){
            Item item = new Item((orderAttributes.getInvoiceNo()).trim(),orderAttributes.getStoreName(), orderAttributes.getDeliveryAddress(),"");
            items1.add(item);
        }
        myItemsListAdapter1 = new ItemsListAdapter(OrderNotUploadedActivity.this, items1);
        lvordersnotuploaded.setAdapter(myItemsListAdapter1);

        cont_savedfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderNotUploadedActivity.this, MainActivity.class);
                i.putExtra("deldate", deliverdate);
                i.putExtra("routes", routename);
                i.putExtra("ordertype",ordertype);
                startActivity(i);
            }
        });

        try{

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                new checkfirebasetrip().execute(LINX+"registerfirebasetoken?token=" + newToken+"&ordertype="+ordertype+"&route="+routename+"&deldate="+deliverdate+"&counts="+dbH.selectCountNotUploaded());
            });
        }catch (Exception e){

        }

        lvordersnotuploaded.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Item selectedItem = ( Item)(parent.getItemAtPosition(position));

                    startProgress("Retrying To Post");
                         new IndividualPostLines(selectedItem.ItemString.toString()).execute();

                return false;
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(OrderNotUploadedActivity.this,LandingPage.class);
            startActivity(myIntent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private class checkfirebasetrip extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "firebase!", Toast.LENGTH_LONG).show();
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
    private class pullthetrigger extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "firebase!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("sendfirebase", "ire**************" + customerOrders);

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
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "PostLinesNew.php");
            Log.e("PostLinesNew","PostLinesNew----------******************");
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


    public class UploadNewOrderLines extends AsyncTask<Void, Void, Void> {

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
            Log.e("postIP","++++++++++++++++++++++++++++++++"+IP + "PostHeaders");
            Log.e("postIP","++++++++++++++++++++++++++++++++ signedBy" + signedBy);
            HttpPost httppost = new HttpPost(IP + "PostHeaders");
            try {
                // Add your data

                String r = signedBy;
                Pattern ptr = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
                Matcher matchr = ptr.matcher(r);
                if (!matchr.matches()) {
                    r = r.replaceAll(ptr.pattern(), " ");
                }
                signedBy = r;
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
                //json.put("Loyalty", Loyalty);

                Log.d("JSON", json.toString());
                Log.e("Marawhy","ozozi uzwa ****************"+ json.toString());
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
                Intent main = new Intent(OrderNotUploadedActivity.this, OrderNotUploadedActivity.class);

                startActivity(main);

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

    private class IndividualPostLines extends AsyncTask<Void, Void, Void> {
String InvoiceNo;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public IndividualPostLines(String  InvoiceNo) {

            this.InvoiceNo = InvoiceNo;
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
                ArrayList<OrderLines> dealLineToUpload= dbH.returnOrderLinesInfoUploadedByInvoice(InvoiceNo);
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
                OrderHeaderPost();




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
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(OrderNotUploadedActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Make sure you are connected to the internet, check your mobile data or WIFI connectivity");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
}