package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.Aariyan.Abstraction.BaseActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.Resource;
import com.regin.reginald.vehicleanddrivers.Aariyan.ViewModel.OrdersViewModel;
import com.regin.reginald.vehicleanddrivers.Data;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;

public class OrdersActivity extends BaseActivity {

    private IpModel serverModel;
    private String deliveryDate;
    private int routeId, orderId;
    private DatabaseAdapter databaseAdapter;
    private OrdersViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        databaseAdapter = new DatabaseAdapter(this);
    }

    @Override
    protected void onResume() {

        //Checking is the server is exist:
        if (isServerExist(this)) {
            serverModel = getServerModel(this);
        }

        //getting the intent value:
        if (getIntent() != null) {
            deliveryDate = getIntent().getStringExtra("deldate");
            routeId = getIntent().getIntExtra("routes", 0);
            orderId = getIntent().getIntExtra("ordertype", 0);

            loadOrders(serverModel, deliveryDate, routeId, orderId);
        }

        super.onResume();
    }

    private void loadOrders(IpModel serverModel, String deliveryDate, int routeId, int orderId) {
        orderViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);
        orderViewModel.init(databaseAdapter, serverModel.getServerIp(), deliveryDate, routeId, orderId);
        orderViewModel.listOfOrders().observe(this, new Observer<Resource<List<OrderModel>>>() {
            @Override
            public void onChanged(Resource<List<OrderModel>> listResource) {
                switch (listResource.status) {
                    case ERROR:
                        Toast.makeText(OrdersActivity.this, "Error: "+listResource.errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Toast.makeText(OrdersActivity.this, "Size: "+listResource.response.size(), Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        Toast.makeText(OrdersActivity.this, "Loading: "+listResource.status, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}