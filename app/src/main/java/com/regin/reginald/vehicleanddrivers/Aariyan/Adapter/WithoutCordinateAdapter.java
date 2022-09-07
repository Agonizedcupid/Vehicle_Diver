package com.regin.reginald.vehicleanddrivers.Aariyan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DummyLocations;
import com.regin.reginald.vehicleanddrivers.LandingPage;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;

public class WithoutCordinateAdapter extends RecyclerView.Adapter<WithoutCordinateAdapter.ViewHolder> {

    private Context context;
    private List<Orders> list;

    public WithoutCordinateAdapter(Context context, List<Orders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_pastel_code, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();

        Orders location = list.get(pos);

        try {
            holder.title.setText(location.getStoreName());
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pastelCodeForLocation);
        }
    }
}
