package com.regin.reginald.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tblSettings")
public class SettingsModel {

    @DatabaseField(id = true, columnName = "id")
    //@MapFrom("id")
    private int id;

    @DatabaseField(columnName = "strServerIp")
    //@MapFrom("strServerIp")
    private String strServerIp;

    @DatabaseField(columnName = "regKey")
   // @MapFrom("regKey")
    private String regKey;
    @DatabaseField(columnName = "Company")
   // @MapFrom("Company")
    private String Company;

    @DatabaseField(columnName = "DeviceID")
    //@MapFrom("DeviceID")
    private String DeviceID;

    @DatabaseField(columnName = "Email")
   // @MapFrom("Email")
    private String Email;



    public int getId(){ return id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setregKey(String regKey) {
        this.regKey = regKey;
    }
    public void setDeviceID(String DeviceID) {
        this.DeviceID = DeviceID;
    }
    public void setstrServerIp(String strServerIp) {
        this.strServerIp = strServerIp;
    }
    public void setCompany(String Company) {
        this.Company = Company;
    }
    public void setEmail(String Email) {
        this.Email = Email;
    }


    public String getregKey() {
        return regKey;
    }
    public String getstrServerIp() {
        return strServerIp;
    }
    public String getDeviceID() {
        return DeviceID;
    }
    public String getCompany() {
        return Company;
    }
    public String getEmail() {
        return Email;
    }

}
