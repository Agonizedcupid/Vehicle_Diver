package com.regin.reginald.vehicleanddrivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.CheckList;
import com.regin.reginald.model.SettingsModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CheckListActivity extends AppCompatActivity {
    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;



        Item(String t, String t2,String t3) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;

        }


    }
    static class ViewHolder {
        //ImageView icon;
        TextView text;




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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.checkingrow, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.checkingname);
                //viewHolder.text2 = (TextView) rowView.findViewById(R.id.answer);


                rowView.setTag(viewHolder);
            }

           ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            //holder.text2.setText(list.get(position).ItemString2);




            return rowView;
        }

        public List<Item> getList() {
            return list;
        }

    }
    List<Item> items1,lineinfo;
    List<Data> listdata;
    ItemsListAdapter myItemsListAdapter;
    String customerOrders, SERVERIP;
    int len = 0;
    //private DatabaseHelper mDatabaseHelper;
    TextView ordertype,textView28,routename;
    EditText edtkm_start,edt_km_end;
    Button btnstarttrip;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    ListView lstchecklists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehiclecheckings);

        ordertype =(TextView) findViewById(R.id.ordertype);
        textView28 =(TextView) findViewById(R.id.textView28);
        routename =(TextView) findViewById(R.id.routename);
        edtkm_start =(EditText) findViewById(R.id.edtkm_start);
        edt_km_end =(EditText) findViewById(R.id.edt_km_end);
        btnstarttrip =(Button) findViewById(R.id.btnstarttrip);
        lstchecklists =(ListView) findViewById(R.id.lstchecklists);
        //mDatabaseHelper = DatabaseHelper.getHelper(this);
        edt_km_end.setText("9999");
        dbH.updateDeals("CREATE TABLE IF NOT EXISTS CheckLists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,checkListId INTEGER,checkListMessage TEXT);");
        AndroidNetworking.initialize(getApplicationContext());
        ArrayList<SettingsModel> settIP= dbH.getSettings();

        for (SettingsModel orderAttributes: settIP){
            SERVERIP = orderAttributes.getstrServerIp();
        }

        Intent returndata = getIntent();

        textView28.setText(returndata.getStringExtra("deldate"));
        ordertype.setText(returndata.getStringExtra("ordertype"));
        routename.setText(returndata.getStringExtra("routes"));

        new getCheckList().execute(SERVERIP + "TruckCheckList.php");
        btnstarttrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckListActivity.this,MainActivity.class);
                i.putExtra("deldate",textView28.getText().toString());
                i.putExtra("routes",routename.getText().toString());
                i.putExtra("ordertype",ordertype.getText().toString());
                i.putExtra("edt_km_end",edt_km_end.getText().toString());
                i.putExtra("edtkm_start",edtkm_start.getText().toString());
                startActivity(i);

            }
        });
    }

    private class getCheckList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + customerOrders);
            if (len > 0) {
                try {
                    dbH.updateDeals("DROP TABLE IF EXISTS CheckLists");
                    dbH.updateDeals("CREATE TABLE IF NOT EXISTS CheckLists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,checkListId INTEGER,checkListMessage TEXT);");

                    List<CheckList> products = new Gson().fromJson(customerOrders, new TypeToken<List<CheckList>>() {
                    }.getType());
                    int i = 1;
                    for (CheckList product : products) {
                        product.setId(i);
                        i++;
                    }

                    String productListString = new Gson().toJson(
                            products,
                            new TypeToken<ArrayList<CheckList>>() {
                            }.getType());

                    JSONArray product_json = new JSONArray(productListString);

                    // Persist arrays to database
//                    JsonPersister persister = new JsonPersister(mDatabaseHelper.getWritableDatabase());
//                    persister.persistArray(CheckList.class, product_json);
                    //readDatabaseProducts();
                    Log.e("**ql*", "done sync");
                    ArrayList<CheckList> ordertype = dbH.getCheckList();

                    List<String> labels = new ArrayList<String>();
                    items1 = new ArrayList<Item>();
                    for (CheckList orderAttributes4 : ordertype) {
                        Item item = new Item(orderAttributes4.getcheckListMessage(),Integer.toString(orderAttributes4.getcheckListId()),"");
                        items1.add(item);
                    }
                    myItemsListAdapter = new ItemsListAdapter(CheckListActivity.this, items1);
                    // myItemsListAdapterchecked = new ItemsListAdapter(InvoiceDetails.this, itemsChecked);
                    lstchecklists.setAdapter(myItemsListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
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

}
