package com.regin.reginald.vehicleanddrivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.model.OrderLines;

import java.util.ArrayList;
import java.util.List;

public class CreditRequitionLandingPage  extends AppCompatActivity {
    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;



        Item(String t, String t2,String t3,String t4) {
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
        TextView text4;
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
                rowView = inflater.inflate(R.layout.multipurpose_deliverynotes_credits_row, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.productdescription);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.notes);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.qty);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.wghts);

                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);
            holder.text4.setText(list.get(position).ItemString4);

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }

    List<Item> items1,lineinfo;

    Button createdeliverynote;
    EditText datetime;
    ListView lvtobeposted;
    ItemsListAdapter myItemsListAdapter;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requisitionlist);

        createdeliverynote = (Button) findViewById(R.id.createdeliverynote);

        lvtobeposted = (ListView) findViewById(R.id.lvtobeposted);

        createdeliverynote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreditRequitionLandingPage.this,CreateCreditRequisition.class);
                startActivity(i);
            }
        });
      /*  items1 = new ArrayList<Item>();
        ArrayList<OrderLines> sett = dbH.ReturnUnPostedCreditLines();
        for (OrderLines orderAttributes : sett) {
            Item item = new Item(orderAttributes.getPastelCode() , orderAttributes.getPastelDescription(), orderAttributes.getQty(),
                    "");
            items1.add(item);
        }


        myItemsListAdapter = new ItemsListAdapter(CreditRequitionLandingPage.this, items1);
        lvtobeposted.setAdapter(myItemsListAdapter);*/
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //reload content
                handler.postDelayed(runnable, delay);
                items1 = new ArrayList<Item>();
                ArrayList<OrderLines> sett = dbH.ReturnUnPostedCreditLines();
                for (OrderLines orderAttributes : sett) {
                    Item item = new Item(orderAttributes.getPastelCode() , orderAttributes.getPastelDescription(), orderAttributes.getQty(),
                            "");
                    items1.add(item);
                }


                myItemsListAdapter = new ItemsListAdapter(CreditRequitionLandingPage.this, items1);
                lvtobeposted.setAdapter(myItemsListAdapter);

                myItemsListAdapter.notifyDataSetChanged();
                lvtobeposted.invalidateViews();
                lvtobeposted.refreshDrawableState();
                //Toast.makeText(CreditRequitionLandingPage.this, "Refreshing the list view", Toast.LENGTH_SHORT).show();
            }
        }, delay);
        (new Thread(runnable)).start();
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent i = new Intent(CreditRequitionLandingPage.this,LandingPage.class);
        startActivity(i);
    }
}
