package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.Aariyan.Abstraction.BaseActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.OrdersAdapter;
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
    private RecyclerView recyclerView;

    private TextView routeNames, orderTypes,dDate;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        databaseAdapter = new DatabaseAdapter(this);

        initUi();
    }

    private void initUi() {
        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeNames = findViewById(R.id.routeName);
        orderTypes = findViewById(R.id.orderType);
        dDate = findViewById(R.id.deliverDate);

        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void onResume() {

        //Checking is the server is exist:
        if (isServerExist(this)) {
            serverModel = getServerModel(this);
        }

        //getting the intent value:
        if (getIntent() != null) {
            progressBar.setVisibility(View.VISIBLE);
            deliveryDate = getIntent().getStringExtra("deldate");
            routeId = getIntent().getIntExtra("routes", 0);
            orderId = getIntent().getIntExtra("ordertype", 0);
            String delivery = getIntent().getStringExtra("delivery");
            String routeName = getIntent().getStringExtra("routeName");

            dDate.setText(deliveryDate);
            routeNames.setText(routeName);
            orderTypes.setText(delivery);
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
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, "Error: " + listResource.errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        OrdersAdapter adapter = new OrdersAdapter(OrdersActivity.this, listResource.response);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, "Size: " + listResource.response.size(), Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, "Loading: " + listResource.status, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}