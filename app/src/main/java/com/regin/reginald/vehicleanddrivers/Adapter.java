package com.regin.reginald.vehicleanddrivers;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.CustomClickListener;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.CustomLongClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takaiwa.net on 2016/04/08.
 */
public class Adapter extends ArrayAdapter<Data> {

    final int INVALID_ID = -1;
    public interface Listener {
        void onGrab(int position, TableLayout row);
    }

    CustomClickListener customClickListener;
    CustomLongClickListener customLongClickListener;

    final Listener listener;
    final Map<Data, Integer> mIdMap = new HashMap<>();
    List<Orders> oH ;

    public Adapter(Context context, List<Data> list,List<Orders> oH, Listener listener, CustomClickListener customClickListener, CustomLongClickListener customLongClickListener) {
        super(context, 0, list);
        this.listener = listener;
        this.oH = oH;
        this.customClickListener = customClickListener;
        this.customLongClickListener = customLongClickListener;
        for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Context context = getContext();
        final Data data = getItem(position);
        if(null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.single_order_rows, null);
        }

        final TableLayout row =  view.findViewById(
                R.id.lytPattern);

        View color = view.findViewById(R.id.orderid);
        //color.setBackgroundColor(Color.rgb(206, 204, 204));//
        TextView textView = view.findViewById(R.id.storename);
        textView.setText(data.ItemString);

        TextView timerange = view.findViewById(R.id.address);
        timerange.setText(data.ItemString2);

        TextView orderid = view.findViewById(R.id.orderid);
        orderid.setText(data.ItemString3);



        TextView del = view.findViewById(R.id.del);
        try {
            del.setText(""+data.ItemString4);

            CheckBox offload =  view.findViewById(R.id.offload);
            //del.setText(data.ItemString4);
            if(data.ItemString4.equals("1"))
            {
                ((CheckBox) view.findViewById(R.id.offload)).setChecked(true);
            }
            else
            {
                ((CheckBox) view.findViewById(R.id.offload)).setChecked(false);
            }
        }catch (Exception e) {
            Log.d("NULL_POINTER", "getView: "+e.getMessage());
        }

        ConstraintLayout layoutClick = view.findViewById(R.id.cardClickListener);
        layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customClickListener.normalClick(oH.get(position).getInvoiceNo(),oH.get(position).getCashPaid(),
                        oH.get(position).getThreshold(), oH.get(position).getStoreName());
            }
        });
        layoutClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                customLongClickListener.longClick(position,oH.get(position).getLatitude(),
                        oH.get(position).getLongitude(),oH.get(position).getDeliverySequence(),oH.get(position).getStoreName());
                return false;
            }
        });

        view.findViewById(R.id.orderid)
            .setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    listener.onGrab(position, row);
                    return false;
                }
            });

        return view;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Data item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
