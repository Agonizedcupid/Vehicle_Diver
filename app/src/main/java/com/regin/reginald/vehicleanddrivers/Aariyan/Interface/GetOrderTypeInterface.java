package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;

import java.util.List;

public interface GetOrderTypeInterface {
    void gotOrderType(List<OrderTypeModel> listOfOrders);
    void error(String errorMessage);
}
