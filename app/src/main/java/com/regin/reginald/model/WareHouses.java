package com.regin.reginald.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.WareHousesModel;


@DatabaseTable(tableName = "WareHouses")
public class WareHouses {

    @DatabaseField(id = true, columnName = "id")
    // @MapFrom("id")
    private int id;

    @DatabaseField(columnName = "WareHouse")
    //@MapFrom("WareHouse")
    private String WareHouse;

    @DatabaseField(columnName = "WareHouseId")
    //@MapFrom("WareHouseId")
    private int WareHouseId;

    public WareHouses() {}

    public WareHouses(int id, String wareHouse, int wareHouseId) {
        this.id = id;
        WareHouse = wareHouse;
        WareHouseId = wareHouseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWareHouse(String WareHouse) {
        this.WareHouse = WareHouse;
    }

    public void setWareHouseId(int WareHouseId) {
        this.WareHouseId = WareHouseId;
    }

    public int getWareHouseId() {
        return WareHouseId;
    }

    public String getWareHouse() {
        return WareHouse;
    }
}
