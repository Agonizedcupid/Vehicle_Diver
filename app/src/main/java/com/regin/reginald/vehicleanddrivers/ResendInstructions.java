package com.regin.reginald.vehicleanddrivers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResendInstructions extends AppCompatActivity {
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
    public class ItemsListAdapter extends BaseAdapter{

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

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }
    List<Item> items1,lineinfo;
    ItemsListAdapter myItemsListAdapter;
    Button btn_resend;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    String delivdate,ordertype,routename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resendinstructions);
        Intent intent = new Intent(ResendInstructions.this, OrderService.class);
        startService(intent);
        btn_resend =(Button) findViewById(R.id.btn_resend);

        Intent returndata = getIntent();

        delivdate = returndata.getStringExtra("deldate");
        ordertype = returndata.getStringExtra("ordertype");
        routename = returndata.getStringExtra("routes");
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbH.isUploaded())
                {
                    Intent i = new Intent(ResendInstructions.this, ResendInstructions.class);
                    i.putExtra("deldate", delivdate);
                    i.putExtra("routes", routename);
                    i.putExtra("ordertype",ordertype);
                    startActivity(i);
                }
                else
                {
                     AlertDialog.Builder builder = new  AlertDialog.Builder(ResendInstructions.this);
                    builder
                            .setTitle("Instructions Cleared :) ")
                            .setMessage("Continue filtering "+delivdate+" - "+ordertype+" - "+routename)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //OKAY
                                    Intent i = new Intent(ResendInstructions.this, LandingPage.class);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //OKAY
                                    Intent i = new Intent(ResendInstructions.this, MainActivity.class);
                                    i.putExtra("deldate", delivdate);
                                    i.putExtra("routes", routename);
                                    i.putExtra("ordertype",ordertype);
                                    startActivity(i);
                                }
                            })
                            .show();
                }
            }
        });



    }

}
