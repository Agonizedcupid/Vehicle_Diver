package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class IpModel {
    private String serverIp, emailAddress, companyName,deviceId, regKey;

    public IpModel(){}

    public IpModel(String serverIp, String emailAddress, String companyName, String deviceId, String regKey) {
        this.serverIp = serverIp;
        this.emailAddress = emailAddress;
        this.companyName = companyName;
        this.deviceId = deviceId;
        this.regKey = regKey;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegKey() {
        return regKey;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }
}
