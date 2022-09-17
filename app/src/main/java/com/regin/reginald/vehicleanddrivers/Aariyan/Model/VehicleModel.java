package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class VehicleModel {
    private String TruckId,TruckName;
    public VehicleModel() {}

    public VehicleModel(String truckId, String truckName) {
        TruckId = truckId;
        TruckName = truckName;
    }

    public String getTruckId() {
        return TruckId;
    }

    public void setTruckId(String truckId) {
        TruckId = truckId;
    }

    public String getTruckName() {
        return TruckName;
    }

    public void setTruckName(String truckName) {
        TruckName = truckName;
    }

    @Override
    public String toString() {
        return getTruckName();
    }
}
