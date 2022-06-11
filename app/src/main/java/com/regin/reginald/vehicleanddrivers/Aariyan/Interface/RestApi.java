package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("Routes.php")
    Observable<ResponseBody> getRoutes();

    @GET("OrderTypesTest.php?")
    Observable<ResponseBody> getOrderTypes(@Query("key") String key);
}
