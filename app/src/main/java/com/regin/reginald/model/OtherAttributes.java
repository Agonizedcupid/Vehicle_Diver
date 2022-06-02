package com.regin.reginald.model;

public class OtherAttributes {
    String prototype;
    String printermodelLogicalName;
    String address;
    String route;
    String ordertype;
    String deliverydate;
    String conDocId;
    String conMess;
    String condteTm;
    String Messages;
    String ExpenseName;
    String GlCode;
    String DriverName;
    String signature;
    String kmout;
    String kmdone;
    String SealNumber;
    int id;
    int ExpenseId;
    public void setProtoType(String prototype) {
        this.prototype = prototype;
    }
    public void setroute(String route) {
        this.route = route;
    }
    public void setSealNumber(String SealNumber) {
        this.SealNumber = SealNumber;
    }
    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }
    public void setsignature(String signature) {
        this.signature = signature;
    }
    public void setkmout(String kmout) {
        this.kmout = kmout;
    }
    public void setkmdone(String kmdone) {
        this.kmdone = kmdone;
    }
    public void setordertype(String ordertype) {
        this.ordertype = ordertype;
    }
    public void setdeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }
    public void setprintermodelLogicalName(String printermodelLogicalName) {
        this.printermodelLogicalName = printermodelLogicalName;
    }
    public void setaddress(String address) {
        this.address = address;
    }
    public void setconDocId(String conDocId) {
        this.conDocId = conDocId;
    }
    public void setconMess(String conMess) {
        this.conMess = conMess;
    }
    public void setcondteTm(String condteTm) {
        this.condteTm = condteTm;
    }
    public void setMessages(String Messages) {
        this.Messages = Messages;
    }
    public void setExpenseName(String ExpenseName) {
        this.ExpenseName = ExpenseName;
    }
    public void setGlCode(String GlCode) {
        this.GlCode = GlCode;
    }
    public void setExpenseId(int ExpenseId) {
        this.ExpenseId = ExpenseId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getprototype()
    {
        return prototype;
    }
    public String getprintermodelLogicalName()
    {
        return printermodelLogicalName;
    }
    public String getaddress() {
        return address;
    }
    public String getroute() {
        return route;
    }
    public String getordertype() {
        return ordertype;
    }
    public String getdeliverydate() {
        return deliverydate;
    }
    public String getconDocId() {
        return conDocId;
    }
    public String getconMess() {
        return conMess;
    }
    public String getcondteTm() {
        return condteTm;
    }
    public String getMessages() {
        return Messages;
    }
    public String getExpenseName() {
        return ExpenseName;
    }
    public String getGlCode() {
        return GlCode;
    }
    public String getSealNumber() {
        return SealNumber;
    }
    public String getDriverName() {
        return DriverName;
    }
    public String getsignature() {
        return signature;
    }
    public String getkmout() {
        return kmout;
    }
    public String getkmdone() {
        return kmdone;
    }
    public int getExpenseId() {
        return ExpenseId;
    }
    public int getId(){ return id; }


}
