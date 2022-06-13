package com.regin.reginald.vehicleanddrivers.Aariyan.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.OrderListInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.NetworkingFeedback;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.Resource;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositories {

    private static OrderRepositories orderRepositories;

    public static OrderRepositories getInstance(DatabaseAdapter databaseAdapter) {
        synchronized (OrderRepositories.class) {
            if (orderRepositories == null) {
                orderRepositories = new OrderRepositories(databaseAdapter);
            }
        }

        return orderRepositories;
    }

    private DatabaseAdapter databaseAdapter;

    private OrderRepositories(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    private MutableLiveData<Resource<List<OrderModel>>> listOfOrders = new MutableLiveData<>();
    String error = "";

    public MutableLiveData<Resource<List<OrderModel>>> getAllOrders(String serverIp, String orderDate, int routeId, int orderId) {
        List<OrderModel> floatingList = new ArrayList<>();

        NetworkingFeedback networkingFeedback = new NetworkingFeedback(databaseAdapter);
        networkingFeedback.getOrdersList(new OrderListInterface() {
            @Override
            public void gotOrders(List<OrderModel> list) {
                //floatingList.addAll(list);
                listOfOrders.setValue(Resource.success(list));
            }

            @Override
            public void onError(String errorMessage) {
                error = errorMessage;
                listOfOrders.setValue(Resource.error(errorMessage, null));
            }
        }, serverIp, orderDate, routeId, orderId);
        return listOfOrders;
    }

}
