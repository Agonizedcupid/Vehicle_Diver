package com.regin.reginald.vehicleanddrivers.Aariyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.CratesActivity;
import com.regin.reginald.vehicleanddrivers.InvoiceDetails;
import com.regin.reginald.vehicleanddrivers.MainActivity;
import com.regin.reginald.vehicleanddrivers.MyMapActivity;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;
    double lat = -33.966145;
    double lon = 22.466218;

    public OrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_order_rows, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        OrderModel model = list.get(position);
//        holder.storeName.setText(model.getStoreName());
//        holder.orderId.setText("" + model.getOrderId());
//        holder.deliveryAddress.setText(model.getDeliveryAddress());
//        int status = model.getOffloaded();
//        if (status == 0) {
//            holder.checkBox.setChecked(false);
//        } else {
//            holder.checkBox.setChecked(true);
//        }
//
//        holder.offloadStatus.setText("" + model.getOffloaded());
//
//        /**
//         *
//         * Click Listener:
//         */
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Original Threshold 0 & 1 that i understood from the older source code:
//                //if (model.getThreshold().equals("0")) {
//                // Using 50 for the testing purpose
//                if (model.getThreshold().equals("50")) {
//                    Intent b = new Intent(context, InvoiceDetails.class);
//                    //UpdateDeliverySeq();
//                    b.putExtra("deldate", Constant.DELIVERY_DATE);
//                    b.putExtra("routes", Constant.ROUTES_NAME);
//                    b.putExtra("ordertype", Constant.ORDER_TYPE);
//                    b.putExtra("invoiceno", model.getInvoiceNo());
//                    b.putExtra("cash", model.getCashPaid());
//                    context.startActivity(b);
//                } else {
//                    Intent b = new Intent(context, CratesActivity.class);
//                    b.putExtra("invoiceno", model.getInvoiceNo());
//                    b.putExtra("threshold", model.getThreshold());
//                    b.putExtra("storename", model.getStoreName());
//                    b.putExtra("deldate", Constant.DELIVERY_DATE);
//                    b.putExtra("routes", Constant.ROUTES_NAME);
//                    b.putExtra("ordertype", Constant.ORDER_TYPE);
//                    context.startActivity(b);
//                }
//            }
//        });
//
//
//        /**
//         * Long Click
//         */
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Log.d("CHECKING_LOCATION", "" + model.getLatitude() + " - " + model.getLongitude());
//                Intent intent = new Intent(context, MyMapActivity.class);
//                intent.putExtra("Lat", "" + model.getLatitude());
//                intent.putExtra("Lon", "" + model.getLongitude());
//                intent.putExtra("seq", "");
////                intent.putExtra("currentLat", lat);
////                intent.putExtra("currentLon", lon);
//                intent.putExtra("custName", "" + model.getCustomerPastelCode());
//                context.startActivity(intent);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName, orderId, deliveryAddress, offloadStatus;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            storeName = itemView.findViewById(R.id.storeName);
//            orderId = itemView.findViewById(R.id.orderIds);
//            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
//            offloadStatus = itemView.findViewById(R.id.offloadStatus);
//            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
