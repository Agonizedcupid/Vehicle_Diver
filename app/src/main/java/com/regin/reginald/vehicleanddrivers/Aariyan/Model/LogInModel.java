package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class LogInModel {
    private int id;
    private String companyName,tabletRegId,driverName,driverEmail,driverPassword;
    private int groupId;
    private String IP;


    public LogInModel() {}

    public LogInModel(int id, String companyName, String tabletRegId, String driverName, String driverEmail, String driverPassword, int groupId, String IP) {
        this.id = id;
        this.companyName = companyName;
        this.tabletRegId = tabletRegId;
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverPassword = driverPassword;
        this.groupId = groupId;
        this.IP = IP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTabletRegId() {
        return tabletRegId;
    }

    public void setTabletRegId(String tabletRegId) {
        this.tabletRegId = tabletRegId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
