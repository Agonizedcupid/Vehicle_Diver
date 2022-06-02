package com.regin.reginald.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "Routes")
public class Routes {

    @DatabaseField(id = true, columnName = "id")
   // @MapFrom("id")
    private int id;

    @DatabaseField(columnName = "RouteId")
    //@MapFrom("RouteId")
    private String RouteId;

    @DatabaseField(columnName = "RouteName")
  //  @MapFrom("RouteName")
    private String RouteName;

    public int getId(){ return id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setRouteName(String RouteName) {
        this.RouteName = RouteName;
    }
    public String getRouteName() {
        return RouteName;
    }
}
