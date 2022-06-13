package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;

import java.util.List;

public interface OrderListInterface {

    void gotOrders(List<OrderModel> list);
    void onError(String errorMessage);
}
