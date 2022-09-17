package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DriverModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.VehicleModel;

import java.util.List;

public interface VehicleInterface {
    void listOfVehicle(List<VehicleModel> listOfVehicles);
    void error(String errorMessage);
}
