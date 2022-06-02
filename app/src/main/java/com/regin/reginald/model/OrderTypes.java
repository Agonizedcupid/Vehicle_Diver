package com.regin.reginald.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "OrderTypes")
public class OrderTypes {

    @DatabaseField(id = true, columnName = "id")
   // @MapFrom("id")
    private int id;

    @DatabaseField(columnName = "OrderTypeId")
   // @MapFrom("OrderTypeId")
    private String OrderTypeId;

    @DatabaseField(columnName = "OrderType")
  //  @MapFrom("OrderType")
    private String OrderType;

    public int getId(){ return id; }

    public void setId(int id) {
        this.id = id;
    }
    public void setOrderType(String OrderType) {
        this.OrderType = OrderType;
    }
    public String getOrderType() {
        return OrderType;
    }
}
