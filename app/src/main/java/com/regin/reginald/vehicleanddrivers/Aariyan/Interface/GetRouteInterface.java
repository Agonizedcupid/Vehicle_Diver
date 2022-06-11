package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;

import java.util.List;

public interface GetRouteInterface {

    void gotRoute(List<RouteModel> listOfRoutes);
    void error(String errorMessage);
}
