package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class FridgeTempModel {
    private String invoiceNo, fridgeTemp;

    public FridgeTempModel(){}

    public FridgeTempModel(String invoiceNo, String fridgeTemp) {
        this.invoiceNo = invoiceNo;
        this.fridgeTemp = fridgeTemp;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getFridgeTemp() {
        return fridgeTemp;
    }

    public void setFridgeTemp(String fridgeTemp) {
        this.fridgeTemp = fridgeTemp;
    }
}
