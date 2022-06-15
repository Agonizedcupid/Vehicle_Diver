package com.regin.reginald.vehicleanddrivers.Aariyan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;
    public OrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_order_rows,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel model = list.get(position);
        holder.storeName.setText(model.getStoreName());
        holder.orderId.setText(""+model.getOrderId());
        holder.deliveryAddress.setText(model.getDeliveryAddress());
        int status = model.getOffloaded();
        if (status == 0) {
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
        }

        holder.offloadStatus.setText(""+model.getOffloaded());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName, orderId, deliveryAddress,offloadStatus;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storeName);
            orderId = itemView.findViewById(R.id.orderIds);
            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
            offloadStatus = itemView.findViewById(R.id.offloadStatus);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
