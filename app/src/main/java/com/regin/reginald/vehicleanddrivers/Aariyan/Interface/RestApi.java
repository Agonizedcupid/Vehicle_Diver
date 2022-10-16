package com.regin.reginald.vehicleanddrivers.Aariyan.Interface;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostHeadersModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostLinesModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

//    @FormUrlEncoded
//    @POST("PostLinesNew.php")
//    Observable<ResponseBody> postNewLines(@Body List<TempModelOfOrderLinesDetails> listOfOrders);

    @FormUrlEncoded
    @POST("PostLinesV2")
    Observable<ResponseBody> postNewLines(@Body List<PostLinesModel> listOfOrders);

//    @FormUrlEncoded
//    @POST("PostHeadersLoyalty")
//    Observable<ResponseBody> postRoyalty(@Body List<TempModelOfOrderLines> listOfRoyalty);

    @FormUrlEncoded
    @POST("PostHeadersV2.php")
    Observable<ResponseBody> postHeaders(@Body List<PostHeadersModel> listOfPostHeaders);

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

    @GET("Drivers")
    Observable<ResponseBody> getDrivers();

    @GET("Vehicles")
    Observable<ResponseBody> getVehicles();

    @FormUrlEncoded
    @POST("PostDriversAppCheckList")
    Observable<ResponseBody> postCheckList(@Field("strDriver") String driverName,
                                           @Field("dteDateChecked") String date,
                                           @Field("HasRadiatorWater") String radiatorAnswer,
                                           @Field("HasEngineOil") String engineOilAnswer,
                                           @Field("isTyreConditionFine") String tyreConditionAnswer,
                                           @Field("HasSpareTyre") String spareTyreAnswer,
                                           @Field("HasJack") String jackAnswer,
                                           @Field("HasSpanner") String spannerAnswer,
                                           @Field("HasFireExt") String fireAnswer,
                                           @Field("HasWaterCaps") String waterCapsAnswer,
                                           @Field("HasFuelCaps") String fuelCapsAnswer,
                                           @Field("HasDipStick") String dipstickAnswer,
                                           @Field("CheckedWindScreen") String windScreenAnswer,
                                           @Field("CheckedSideMirrors") String sideMirrorAnswer,
                                           @Field("CheckedMainLights") String mainLightsAnswer,
                                           @Field("CheckedIndicatorLights") String indicatorLightAnswer,
                                           @Field("CheckedBrakes") String brakesAnswer,
                                           @Field("CheckedCabBody") String cabBodyAnswer,
                                           @Field("CheckedFridgeWater") String fridgeWaterAnswer,
                                           @Field("CheckedFridgeOil") String fridgeOilAnswer,
                                           @Field("CheckedFridgeBelts") String fridgeBeltsAnswer,
                                           @Field("CheckedFridgeDipStick") String fridgeDeepStickAnswer,
                                           @Field("CheckedFridgeBody") String fridgeBodyAnswer,
                                           @Field("RadiatorWaterComment") String radiatorWaterComment,
                                           @Field("EngineOilComment") String engineOilComment,
                                           @Field("TyreConditionFineComment") String tyreConditionComment,
                                           @Field("SpareTyreComment") String spareTyreComment,
                                           @Field("JackComment") String jackComment,
                                           @Field("SpannerComment") String spannerComment,
                                           @Field("FireExtComment") String fireExitComment,
                                           @Field("WaterCapsComment") String waterCapsComment,
                                           @Field("FuelCapsComment") String fuelCapsComment,
                                           @Field("DipStickComment") String dipstickComment,
                                           @Field("CheckedWindScreenComment") String windScreenComment,
                                           @Field("CheckedSideMirrorsComment") String sideMirrorsComment,
                                           @Field("CheckedMainLightsComment") String mainLightsComment,
                                           @Field("CheckedIndicatorLightsComment") String indicatorsLightsComment,
                                           @Field("CheckedBrakesComment") String brakesComment,
                                           @Field("CheckedCabBodyComment") String cabBodyComment,
                                           @Field("CheckedFridgeWaterComment") String fridgeWaterComment,
                                           @Field("CheckedFridgeOilComment") String fridgeOilComment,
                                           @Field("CheckedFridgeBeltsComment") String fridgeBeltsComment,
                                           @Field("CheckedFridgeDipStickComment") String fridgeDeepStickComment,
                                           @Field("CheckedFridgeBodyComment") String fridgeBodyComment,
                                           @Field("intUserIdLoggedIn") String userId,
                                           @Field("vehicle") String vehicleName
                                           );

}
