package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.LogInModel;

import java.util.List;

public interface LogInInterface {

    void loggedIn(LogInModel logInData);
    void error(String errorMessage);
}
