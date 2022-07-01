package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderLinesModel {

    @JsonProperty("OrderId")
    public String orderId;
    @JsonProperty("InvoiceNo")
    public String invoiceNo;
    @JsonProperty("CustomerPastelCode")
    public String customerPastelCode;
    @JsonProperty("StoreName")
    public String storeName;
    @JsonProperty("DeliveryDate")
    public String deliveryDate;
    @JsonProperty("Latitude")
    public double latitude;
    @JsonProperty("Longitude")
    public double longitude;
    @JsonProperty("OrderValueExc")
    public double orderValueExc;
    @JsonProperty("OrderValueInc")
    public double orderValueInc;
    @JsonProperty("DeliveryAddress")
    public String deliveryAddress;
    @JsonProperty("User")
    public String user;
    @JsonProperty("OrderMass")
    public int orderMass;
    @JsonProperty("ProductId")
    public int productId;
    @JsonProperty("Qty")
    public int qty;
    @JsonProperty("Price")
    public double price;
    @JsonProperty("PastelDescription")
    public String pastelDescription;
    @JsonProperty("PastelCode")
    public String pastelCode;
    @JsonProperty("OrderDetailId")
    public int orderDetailId;
    @JsonProperty("Comment")
    public String comment;
    public String returnQty;
    public String offLoadComment;
    public int blnoffloaded;
    public String strCustomerReason;
    public String intWareHouseId;
    @JsonProperty("WareHouseName")
    public String wareHouseName;

    public OrderLinesModel(){}

    public OrderLinesModel(String orderId, String invoiceNo, String customerPastelCode, String storeName, String deliveryDate, double latitude, double longitude, double orderValueExc, double orderValueInc, String deliveryAddress, String user, int orderMass, int productId, int qty, double price, String pastelDescription, String pastelCode, int orderDetailId, String comment, String returnQty, String offLoadComment, int blnoffloaded, String strCustomerReason, String intWareHouseId, String wareHouseName) {
        this.orderId = orderId;
        this.invoiceNo = invoiceNo;
        this.customerPastelCode = customerPastelCode;
        this.storeName = storeName;
        this.deliveryDate = deliveryDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderValueExc = orderValueExc;
        this.orderValueInc = orderValueInc;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
        this.orderMass = orderMass;
        this.productId = productId;
        this.qty = qty;
        this.price = price;
        this.pastelDescription = pastelDescription;
        this.pastelCode = pastelCode;
        this.orderDetailId = orderDetailId;
        this.comment = comment;
        this.returnQty = returnQty;
        this.offLoadComment = offLoadComment;
        this.blnoffloaded = blnoffloaded;
        this.strCustomerReason = strCustomerReason;
        this.intWareHouseId = intWareHouseId;
        this.wareHouseName = wareHouseName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerPastelCode() {
        return customerPastelCode;
    }

    public void setCustomerPastelCode(String customerPastelCode) {
        this.customerPastelCode = customerPastelCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getOrderValueExc() {
        return orderValueExc;
    }

    public void setOrderValueExc(double orderValueExc) {
        this.orderValueExc = orderValueExc;
    }

    public double getOrderValueInc() {
        return orderValueInc;
    }

    public void setOrderValueInc(double orderValueInc) {
        this.orderValueInc = orderValueInc;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getOrderMass() {
        return orderMass;
    }

    public void setOrderMass(int orderMass) {
        this.orderMass = orderMass;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPastelDescription() {
        return pastelDescription;
    }

    public void setPastelDescription(String pastelDescription) {
        this.pastelDescription = pastelDescription;
    }

    public String getPastelCode() {
        return pastelCode;
    }

    public void setPastelCode(String pastelCode) {
        this.pastelCode = pastelCode;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public String getOffLoadComment() {
        return offLoadComment;
    }

    public void setOffLoadComment(String offLoadComment) {
        this.offLoadComment = offLoadComment;
    }

    public int getBlnoffloaded() {
        return blnoffloaded;
    }

    public void setBlnoffloaded(int blnoffloaded) {
        this.blnoffloaded = blnoffloaded;
    }

    public String getStrCustomerReason() {
        return strCustomerReason;
    }

    public void setStrCustomerReason(String strCustomerReason) {
        this.strCustomerReason = strCustomerReason;
    }

    public String getIntWareHouseId() {
        return intWareHouseId;
    }

    public void setIntWareHouseId(String intWareHouseId) {
        this.intWareHouseId = intWareHouseId;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }
}
