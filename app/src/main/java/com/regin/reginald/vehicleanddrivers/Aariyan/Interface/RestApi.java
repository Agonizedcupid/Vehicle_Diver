package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @GET("Routes.php")
    Observable<ResponseBody> getRoutes();

    @GET("OrderTypesTest.php?")
    Observable<ResponseBody> getOrderTypes(@Query("key") String key);

    //It will insert on Local "OrderHeaders" Table
    @GET("OrderHeaders.php?")
    Observable<ResponseBody> getOrderHeaders(@Query("OrderType") int orderTypeId, @Query("Route") int routeId, @Query("DeliveryDate") String deliveryDate);

    //It will insert on Local "OrderLines" table
    @GET("OrderLines.php?")
    Observable<ResponseBody> getOrderLines(@Query("OrderType") int orderTypeId, @Query("Route") int routeId, @Query("DeliveryDate") String deliveryDate);

    @FormUrlEncoded
    @POST("Login.php")
    Observable<ResponseBody> getLogInData(@Field("UserName") String userName, @Field("Password") String password);

}
