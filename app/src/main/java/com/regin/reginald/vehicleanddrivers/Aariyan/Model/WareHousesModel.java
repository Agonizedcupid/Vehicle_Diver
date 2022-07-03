package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class WareHousesModel {

    private String WareHouseId;
    private String WareHouse;

    public WareHousesModel(){}

    public WareHousesModel(String wareHouseId, String wareHouse) {
        WareHouseId = wareHouseId;
        WareHouse = wareHouse;
    }

    public String getWareHouseId() {
        return WareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        WareHouseId = wareHouseId;
    }

    public String getWareHouse() {
        return WareHouse;
    }

    public void setWareHouse(String wareHouse) {
        WareHouse = wareHouse;
    }

    @Override
    public String toString() {
        return getWareHouse();
    }
}
