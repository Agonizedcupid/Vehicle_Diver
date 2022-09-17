package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class DriverModel {
    private String DriverId,DriverName;

    public DriverModel () {}

    public DriverModel(String driverId, String driverName) {
        DriverId = driverId;
        DriverName = driverName;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    @Override
    public String toString() {
        return getDriverName();
    }
}
