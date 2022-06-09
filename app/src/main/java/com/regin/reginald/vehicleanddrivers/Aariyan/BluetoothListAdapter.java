package com.regin.reginald.vehicleanddrivers.Aariyan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

    private Context context;
    private List<String> listOfDevices;

    public BluetoothListAdapter(Context context, List<String> listOfDevices) {
        this.context = context;
        this.listOfDevices = listOfDevices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_bluetooth_list,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.deviceName.setText(listOfDevices.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+listOfDevices.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listOfDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
        }
    }
}
