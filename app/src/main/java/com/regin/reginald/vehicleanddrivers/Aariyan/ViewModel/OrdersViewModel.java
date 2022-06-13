package com.regin.reginald.vehicleanddrivers.Aariyan.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.Resource;
import com.regin.reginald.vehicleanddrivers.Aariyan.Repositories.OrderRepositories;
import com.regin.reginald.vehicleanddrivers.Data;

import java.util.List;

public class OrdersViewModel extends ViewModel {

    private MutableLiveData<Resource<List<OrderModel>>> listOfOrders;

    public void init(DatabaseAdapter databaseAdapter, String serverIp, String orderDate, int routeId, int orderId) {
        if (listOfOrders != null) {
            return;
        }
        listOfOrders = OrderRepositories.getInstance(databaseAdapter).getAllOrders(serverIp, orderDate, routeId, orderId);
    }

    public MutableLiveData<Resource<List<OrderModel>>> listOfOrders() {
        return listOfOrders;
    }
}
