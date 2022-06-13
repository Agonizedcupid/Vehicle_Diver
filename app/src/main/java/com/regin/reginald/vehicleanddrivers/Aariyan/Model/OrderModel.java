package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;

public class OrderModel {
//    private int id;
//    private String OrderId;
//    private String InvoiceNo;
//    private String CustomerPastelCode;
//    private String StoreName;
//    private String DeliveryDate;
//    private String DeliverySequence;
//    private String Latitude;
//    private String Longitude;
//    private String OrderValueExc;
//    private String OrderValueInc;
//    private String User;
//    private String DeliveryAddress;
//    private String OrderMass;
//    private String offloaded;
//    private String Uploaded;
//    private String imageString;
//    private String CashPaid;
//    private String strNotesDrivers;
//    private String strEmailCustomer;
//    private String strCashSignature;
//    private String InvTotIncl;
//    private String StartTripTime;
//    private String EndTripTime;
//    private int DeliverySeq;
//    private String strCoordinateStart;
//    private String DriverName;
//    private String DriverEmail;
//    private String DriverPassword;
//    private String strCustomerSignedBy;
//    private String PaymentType;
//    private String Loyalty;
//    private String Threshold;

    public OrderModel() {
    }
    @JsonProperty("OrderId")
    public int orderId;
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
    public String orderValueExc;
    @JsonProperty("OrderValueInc")
    public String orderValueInc;
    @JsonProperty("DeliveryAddress")
    public String deliveryAddress;
    @JsonProperty("User")
    public String user;
    @JsonProperty("OrderMass")
    public String orderMass;
    @JsonProperty("Uploaded")
    public int uploaded;
    @JsonProperty("CashPaid")
    public String cashPaid;
    public int offloaded;
    public String strEmailCustomer;
    public String strCashsignature;
    @JsonProperty("InvTotIncl")
    public String invTotIncl;
    @JsonProperty("StartTripTime")
    public String startTripTime;
    @JsonProperty("EndTripTime")
    public String endTripTime;
    @JsonProperty("DeliverySeq")
    public int deliverySeq;
    public String strCoordinateStart;
    @JsonProperty("DriverName")
    public String driverName;
    @JsonProperty("DriverEmail")
    public String driverEmail;
    @JsonProperty("DriverPassword")
    public String driverPassword;
    public String strCustomerSignedBy;
    @JsonProperty("Threshold")
    public String threshold;

    public OrderModel(int orderId, String invoiceNo, String customerPastelCode, String storeName, String deliveryDate, double latitude, double longitude, String orderValueExc, String orderValueInc, String deliveryAddress, String user, String orderMass, int uploaded, String cashPaid, int offloaded, String strEmailCustomer, String strCashsignature, String invTotIncl, String startTripTime, String endTripTime, int deliverySeq, String strCoordinateStart, String driverName, String driverEmail, String driverPassword, String strCustomerSignedBy, String threshold) {
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
        this.uploaded = uploaded;
        this.cashPaid = cashPaid;
        this.offloaded = offloaded;
        this.strEmailCustomer = strEmailCustomer;
        this.strCashsignature = strCashsignature;
        this.invTotIncl = invTotIncl;
        this.startTripTime = startTripTime;
        this.endTripTime = endTripTime;
        this.deliverySeq = deliverySeq;
        this.strCoordinateStart = strCoordinateStart;
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverPassword = driverPassword;
        this.strCustomerSignedBy = strCustomerSignedBy;
        this.threshold = threshold;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
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

    public String getOrderValueExc() {
        return orderValueExc;
    }

    public void setOrderValueExc(String orderValueExc) {
        this.orderValueExc = orderValueExc;
    }

    public String getOrderValueInc() {
        return orderValueInc;
    }

    public void setOrderValueInc(String orderValueInc) {
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

    public String getOrderMass() {
        return orderMass;
    }

    public void setOrderMass(String orderMass) {
        this.orderMass = orderMass;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public String getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(String cashPaid) {
        this.cashPaid = cashPaid;
    }

    public int getOffloaded() {
        return offloaded;
    }

    public void setOffloaded(int offloaded) {
        this.offloaded = offloaded;
    }

    public String getStrEmailCustomer() {
        return strEmailCustomer;
    }

    public void setStrEmailCustomer(String strEmailCustomer) {
        this.strEmailCustomer = strEmailCustomer;
    }

    public String getStrCashsignature() {
        return strCashsignature;
    }

    public void setStrCashsignature(String strCashsignature) {
        this.strCashsignature = strCashsignature;
    }

    public String getInvTotIncl() {
        return invTotIncl;
    }

    public void setInvTotIncl(String invTotIncl) {
        this.invTotIncl = invTotIncl;
    }

    public String getStartTripTime() {
        return startTripTime;
    }

    public void setStartTripTime(String startTripTime) {
        this.startTripTime = startTripTime;
    }

    public String getEndTripTime() {
        return endTripTime;
    }

    public void setEndTripTime(String endTripTime) {
        this.endTripTime = endTripTime;
    }

    public int getDeliverySeq() {
        return deliverySeq;
    }

    public void setDeliverySeq(int deliverySeq) {
        this.deliverySeq = deliverySeq;
    }

    public String getStrCoordinateStart() {
        return strCoordinateStart;
    }

    public void setStrCoordinateStart(String strCoordinateStart) {
        this.strCoordinateStart = strCoordinateStart;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public String getStrCustomerSignedBy() {
        return strCustomerSignedBy;
    }

    public void setStrCustomerSignedBy(String strCustomerSignedBy) {
        this.strCustomerSignedBy = strCustomerSignedBy;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }
}
