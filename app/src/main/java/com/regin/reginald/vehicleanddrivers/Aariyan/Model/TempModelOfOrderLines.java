package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class TempModelOfOrderLines {
    private String invoiceNo, lat, lng, image, castPaid, driverNotes, offloaded,
    emailAddress, cashSig, startTime, endTime, deliverySequence,
    coordinateStart, signedBy, loyalty;

    public TempModelOfOrderLines(){}

    public TempModelOfOrderLines(String invoiceNo,String lat, String lng, String image, String castPaid, String driverNotes, String offloaded, String emailAddress, String cashSig, String startTime, String endTime, String deliverySequence, String coordinateStart, String signedBy, String loyalty) {
        this.invoiceNo = invoiceNo;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.castPaid = castPaid;
        this.driverNotes = driverNotes;
        this.offloaded = offloaded;
        this.emailAddress = emailAddress;
        this.cashSig = cashSig;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deliverySequence = deliverySequence;
        this.coordinateStart = coordinateStart;
        this.signedBy = signedBy;
        this.loyalty = loyalty;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCastPaid() {
        return castPaid;
    }

    public void setCastPaid(String castPaid) {
        this.castPaid = castPaid;
    }

    public String getDriverNotes() {
        return driverNotes;
    }

    public void setDriverNotes(String driverNotes) {
        this.driverNotes = driverNotes;
    }

    public String getOffloaded() {
        return offloaded;
    }

    public void setOffloaded(String offloaded) {
        this.offloaded = offloaded;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCashSig() {
        return cashSig;
    }

    public void setCashSig(String cashSig) {
        this.cashSig = cashSig;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDeliverySequence() {
        return deliverySequence;
    }

    public void setDeliverySequence(String deliverySequence) {
        this.deliverySequence = deliverySequence;
    }

    public String getCoordinateStart() {
        return coordinateStart;
    }

    public void setCoordinateStart(String coordinateStart) {
        this.coordinateStart = coordinateStart;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }
}
