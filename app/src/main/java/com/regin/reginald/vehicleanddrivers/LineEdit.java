package com.regin.reginald.vehicleanddrivers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;

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

public class LineEdit extends AppCompatActivity {

    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;
        String ItemString7;
        String ItemString8;


        Item(String t, String t2,String t3,String t4,String t5,String t6,String t7,String t8) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;
            ItemString7 = t7;
            ItemString8 = t8;

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

        public float lastTouchedX;
        public float lastTouchedY;

    }
    public class ItemsListAdapter extends BaseAdapter implements View.OnTouchListener {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.orders_rows, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.storename);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.address);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.orderid);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.del);
                viewHolder.text6 = (CheckBox) rowView.findViewById(R.id.offload);

                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);
            holder.text4.setText(list.get(position).ItemString4);



            if((list.get(position).ItemString4).equals(1))
            {
                holder.text6.setChecked(true);
            }
            else
            {
                holder.text6.setChecked(false);
            }

            holder.text6.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    // States _state = (States) cb.getTag();



                    //_state.setSelected(cb.isChecked());
                }
            });

            if (holder.text4.getText().toString().equals("1")){
                //SPECIAL COLOR

                holder.text.setBackgroundColor(Color.GREEN);
               /* holder.text2.setBackgroundColor(Color.GREEN);
                holder.text3.setBackgroundColor(Color.GREEN);
                holder.text4.setBackgroundColor(Color.GREEN);
                holder.text5.setBackgroundColor(Color.GREEN);*/
            }else
            {

                holder.text4.setBackgroundColor(Color.WHITE);
                /*holder.text2.setBackgroundColor(Color.WHITE);
                holder.text3.setBackgroundColor(Color.WHITE);
                holder.text4.setBackgroundColor(Color.WHITE);
                holder.text5.setBackgroundColor(Color.WHITE);*/
            }


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            InvoiceDetails.ViewHolder vh = (InvoiceDetails.ViewHolder) v.getTag();

            vh.lastTouchedX = event.getX();
            vh.lastTouchedY = event.getY();

            return false;
        }
    }

    List<Item> items1,lineinfo;
    InvoiceDetails.ItemsListAdapter myItemsListAdapter;

    EditText dte_from,qtyordered,notecomment;
    TextView product_name,pastelcode,priceline,commentline,deliverdate,ordertype,routename;
    Button get,start_trip,closelines,btndoneoffloading,donelineinfo,close_line_info;
    String deldate,ordertypestring,route,orderdetailid,invoiceno,originalqty,SERVERIP;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    Spinner reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_details);
        AndroidNetworking.initialize(getApplicationContext());
        qtyordered =  findViewById(R.id.qtyordered);
        notecomment =  findViewById(R.id.notecomment);
        product_name =  findViewById(R.id.product_name);
        pastelcode =  findViewById(R.id.pastelcode);
        priceline =  findViewById(R.id.priceline);
        commentline =  findViewById(R.id.commentline);
        donelineinfo =  findViewById(R.id.donelineinfo);
        close_line_info =  findViewById(R.id.close_line_info);
        reason =  findViewById(R.id.reason);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<SettingsModel> settIP = dbH.getSettings();
        for (SettingsModel orderAttributes : settIP) {
            SERVERIP = orderAttributes.getstrServerIp();
        }
        Intent returndata = getIntent();

        deldate = returndata.getStringExtra("deldate");
        ordertypestring =returndata.getStringExtra("ordertype");
        route = returndata.getStringExtra("routes");
        orderdetailid = returndata.getStringExtra("orderdetailId");
        invoiceno = returndata.getStringExtra("invoiceno");

        Log.e("orderdetailid","*****************"+orderdetailid);
        ArrayList<String> spinnerArray = new ArrayList<String>();

        items1 = new ArrayList<Item>();
        ArrayList<OrderLines> odLineInfo= dbH.returnOrderLinesInfo(orderdetailid);


        for (OrderLines orderAttributes: odLineInfo){
            qtyordered.setText(orderAttributes.getQty());
            notecomment.setText(orderAttributes.getoffLoadComment());
            product_name.setText(orderAttributes.getPastelDescription());
            pastelcode.setText(orderAttributes.getPastelCode());
            priceline.setText(orderAttributes.getPrice());
            commentline.setText(orderAttributes.getComment());
            originalqty = orderAttributes.getQty();

            String theReason ="";
            if (orderAttributes.getstrCustomerReason() != null && !orderAttributes.getstrCustomerReason().isEmpty())
            {
                theReason = orderAttributes.getstrCustomerReason();
            }
            //spinnerArray.add(theReason);

            Item item = new Item(orderAttributes.getPastelDescription(), orderAttributes.getPrice(),orderAttributes.getQty(),
                    "","1","Lines","",orderAttributes.getOrderDetailId());
            items1.add(item);

        }

        spinnerArray.add("Short delivered");
        spinnerArray.add("Incorrect Item");
        spinnerArray.add("Did not Order");
        spinnerArray.add("Stock Damaged");
        spinnerArray.add("Stock not delivered");
        spinnerArray.add("Expired");
        spinnerArray.add("Price is too high");
        spinnerArray.add("Send back");
        spinnerArray.add("Please never send this product again.");
        spinnerArray.add("Other");
        //Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        reason.setAdapter(spinnerArrayAdapter);

        close_line_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  AlertDialog.Builder builder = new AlertDialog.Builder(LineEdit.this);
                builder
                        .setTitle("Closing the Order Line Edit Form ")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent lineEdit = new Intent(LineEdit.this,InvoiceDetails.class);

                                lineEdit.putExtra("deldate",deldate);
                                lineEdit.putExtra("routes",route);
                                lineEdit.putExtra("ordertype",ordertypestring);
                                lineEdit.putExtra("invoiceno",invoiceno);

                                startActivity(lineEdit);
                            }
                        })
                        .show();
            }
        });

        donelineinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qtyordered.getText().length() > 0) {

                    ArrayList<Orders> customerHeader = dbH.ReturnCustomerHeader(invoiceno);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    final String idTimestamp = subscriberId + "-" + ts;
                    String theReason = reason.getSelectedItem().toString();
                    String offLoadComment = notecomment.getText().toString();
                    //   theReason = theReason.replace("'"," ");
                    offLoadComment = offLoadComment.replace("'", " ");
                    for (Orders orderAttributes : customerHeader) {
                        String message = '$' + orderAttributes.getCustomerPastelCode() + ':' + orderAttributes.getStoreName() + " ( " + orderAttributes.getDeliveryAddress() + " ) \n" + theReason + " \n Item :" + pastelcode.getText() + " :" +
                                product_name.getText() + "\n Quantity on the Invoice " + originalqty + " \n Reasoning Quantity " + qtyordered.getText().toString() + "\n Captured by @" + orderAttributes.getUser() + "\n Route Name(Area): " + route + "\n Delivery Type" + ordertypestring;

                        dbH.InserMessage(idTimestamp, message);
                    }
                    // Log.e("*****","Update OrderLines set offLoadComment='"+notecomment.getText().toString()+"' , returnQty ="+qtyordered.getText().toString()+", Uploaded = 0 where OrderDetailId = "+selectedItem_line.ItemString8);
                    dbH.updateDeals("Update OrderLines set offLoadComment='" + offLoadComment + "' , returnQty =" + qtyordered.getText().toString() + ", strCustomerReason ='" + theReason + "' , Uploaded = 0  where OrderDetailId = " + Integer.parseInt(orderdetailid));
                    final Handler handler = new Handler();
                    Runnable runnableNotify = new Runnable() {
                        private long startTime = System.currentTimeMillis();

                        public void run() {

                            while (dbH.NotificationTableHasData() > 0) {
                                try {
                                    Thread.sleep(2000);
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

                    Intent lineEdit = new Intent(LineEdit.this, InvoiceDetails.class);
                    lineEdit.putExtra("deldate", deldate);
                    lineEdit.putExtra("routes", route);
                    lineEdit.putExtra("ordertype", ordertypestring);
                    lineEdit.putExtra("invoiceno", invoiceno);

                    startActivity(lineEdit);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LineEdit.this);
                    builder
                            .setTitle("Blank Quantity ")
                            .setMessage("Please, Make sure That The Quantity Is Not Blank.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }})
                            .show();
                }
            }

        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Please Press SAVE CHANGES OR CLOSE",
                    Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
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
                    dbH.updateDeals("Delete from  Notifications where TabletId = '" + ID+"'");
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
