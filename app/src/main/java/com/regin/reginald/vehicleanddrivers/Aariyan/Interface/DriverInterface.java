package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DriverModel;

import java.sql.Driver;
import java.util.List;

public interface DriverInterface {

    void listOfDrivers(List<DriverModel> listOfDrivers);
    void error(String errorMessage);
}
