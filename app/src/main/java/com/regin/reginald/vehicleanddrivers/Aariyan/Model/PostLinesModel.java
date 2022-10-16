package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class PostLinesModel {
    private String orderDID, returnQty, offLoadComment, blnoffloaded, reasons;

    public PostLinesModel() {
    }

    public PostLinesModel(String orderDID, String returnQty, String offLoadComment, String blnoffloaded, String reasons) {
        this.orderDID = orderDID;
        this.returnQty = returnQty;
        this.offLoadComment = offLoadComment;
        this.blnoffloaded = blnoffloaded;
        this.reasons = reasons;
    }

    public String getOrderDID() {
        return orderDID;
    }

    public void setOrderDID(String orderDID) {
        this.orderDID = orderDID;
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

    public String getBlnoffloaded() {
        return blnoffloaded;
    }

    public void setBlnoffloaded(String blnoffloaded) {
        this.blnoffloaded = blnoffloaded;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
}
