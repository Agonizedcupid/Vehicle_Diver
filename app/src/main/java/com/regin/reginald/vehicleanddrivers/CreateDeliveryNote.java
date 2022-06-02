package com.regin.reginald.vehicleanddrivers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

public class CreateDeliveryNote extends AppCompatActivity {

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
    ItemsListAdapter myItemsListAdapter;
    EditText[] textBoxes = new EditText[20];
    Button btnadddeliv,vrfdelnote;
    TextView reference;
    EditText etcustname,edproducts,edtqty,etdWghts,storename,username;
    ListView lvaddedlines;
    String formattedDate;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdeliverynote);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();
        final String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());

        final String idTimestamp = subscriberId+"-"+ts;
        btnadddeliv =(Button) findViewById(R.id.btnadddeliv);
        vrfdelnote =(Button) findViewById(R.id.vrfdelnote);
        reference =(TextView) findViewById(R.id.reference);
        etcustname = (EditText) findViewById(R.id.etcustname);
        edproducts = (EditText) findViewById(R.id.edproducts);
        edtqty = (EditText) findViewById(R.id.edtqty);
        etdWghts = (EditText) findViewById(R.id.etdWghts);
        storename = (EditText) findViewById(R.id.storename);
        username = (EditText) findViewById(R.id.username);
        lvaddedlines = (ListView) findViewById(R.id.lvaddedlines);
        reference.setText(idTimestamp);
        items1 = new ArrayList<Item>();
        btnadddeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((storename.getText().toString().trim() ).length() !=0){
                    if((edproducts.getText().toString().trim()).length() !=0) {
                        dbH.insertDeliveryNoteLines(reference.getText().toString(),storename.getText().toString(),etcustname.getText().toString(),edtqty.getText().toString(),etdWghts.getText().toString()
                                ,formattedDate,edproducts.getText().toString());
                        Item item = new Item(etcustname.getText().toString(), edproducts.getText().toString(), edtqty.getText().toString(),
                                etdWghts.getText().toString());
                        items1.add(item);
                        myItemsListAdapter = new ItemsListAdapter(CreateDeliveryNote.this, items1);
                        lvaddedlines.setAdapter(myItemsListAdapter);
                        myItemsListAdapter.notifyDataSetChanged();
                        edproducts.setText("");
                        storename.setText("");

                    }
                    else
                    {
                        Toast.makeText(CreateDeliveryNote.this, "Please check that you have the product", Toast.LENGTH_SHORT).show();
                    }

                }else
                {

                    Toast.makeText(CreateDeliveryNote.this, "Please check that you have the customer name.", Toast.LENGTH_SHORT).show();
                }


              //  dbH.updateDeals("ALTER TABLE Filters  ADD COLUMN kmStart TEXT default null");
            }
        });
        vrfdelnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((username.getText().toString().trim()).length() !=0) {
                    dbH.insertDeliveryNoteHeaders(reference.getText().toString(),storename.getText().toString(),formattedDate,username.getText().toString());
                }  else
                {
                    Toast.makeText(CreateDeliveryNote.this, "Please Put In The Name Of The Person Requesting The Credit", Toast.LENGTH_SHORT).show();
                }

            }
        });

     //   savedeliverynote.


    }
}
