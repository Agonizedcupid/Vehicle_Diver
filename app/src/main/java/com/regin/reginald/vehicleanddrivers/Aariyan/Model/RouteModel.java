package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class RouteModel {
    private int RouteId;
    private String RouteName;

    public RouteModel(){}

    public RouteModel(int routeId, String routeName) {
        RouteId = routeId;
        RouteName = routeName;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    @Override
    public String toString() {
        return getRouteName();
    }
}
