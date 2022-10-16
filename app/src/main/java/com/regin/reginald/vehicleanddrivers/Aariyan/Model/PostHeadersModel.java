package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class PostHeadersModel {
    private String invoice, lat, lon, image, cash, note, offload,
            strEmailAddress, strCashSig, strStartTime, strEndTime, delseq,
            strCoord, signedBy, fridgetemp;

    public PostHeadersModel(){}

    public PostHeadersModel(String invoice, String lat, String lon, String image, String cash, String note, String offload, String strEmailAddress, String strCashSig, String strStartTime, String strEndTime, String delseq, String strCoord, String signedBy, String fridgetemp) {
        this.invoice = invoice;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.cash = cash;
        this.note = note;
        this.offload = offload;
        this.strEmailAddress = strEmailAddress;
        this.strCashSig = strCashSig;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.delseq = delseq;
        this.strCoord = strCoord;
        this.signedBy = signedBy;
        this.fridgetemp = fridgetemp;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOffload() {
        return offload;
    }

    public void setOffload(String offload) {
        this.offload = offload;
    }

    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public void setStrEmailAddress(String strEmailAddress) {
        this.strEmailAddress = strEmailAddress;
    }

    public String getStrCashSig() {
        return strCashSig;
    }

    public void setStrCashSig(String strCashSig) {
        this.strCashSig = strCashSig;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getDelseq() {
        return delseq;
    }

    public void setDelseq(String delseq) {
        this.delseq = delseq;
    }

    public String getStrCoord() {
        return strCoord;
    }

    public void setStrCoord(String strCoord) {
        this.strCoord = strCoord;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getFridgetemp() {
        return fridgetemp;
    }

    public void setFridgetemp(String fridgetemp) {
        this.fridgetemp = fridgetemp;
    }
}
