package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class OrderTypeModel {
    private int OrderTypeId;
    private String OrderType;

    public OrderTypeModel(){}

    public OrderTypeModel(int orderTypeId, String orderType) {
        OrderTypeId = orderTypeId;
        OrderType = orderType;
    }

    public int getOrderTypeId() {
        return OrderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        OrderTypeId = orderTypeId;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    @Override
    public String toString() {
        return getOrderType();
    }
}
