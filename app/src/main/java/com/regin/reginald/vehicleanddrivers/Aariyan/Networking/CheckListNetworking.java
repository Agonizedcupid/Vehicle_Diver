package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.ApiClient;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.DriverInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.PostCheckListInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.RestApi;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.VehicleInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DriverModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostCheckListModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CheckListNetworking {

    private CompositeDisposable driverDisposable = new CompositeDisposable();
    private CompositeDisposable vehicleDisposable = new CompositeDisposable();
    private CompositeDisposable postDisposable = new CompositeDisposable();
    private DatabaseAdapter databaseAdapter;
    RestApi api;

    public CheckListNetworking(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
        ArrayList<SettingsModel> settIP = databaseAdapter.getSettings();
        api = ApiClient.getClient(settIP.get(0).getstrServerIp()).create(RestApi.class);
    }


    public void getDriversList(DriverInterface driverInterface) {
        List<DriverModel> list = new ArrayList<>();
        driverDisposable.add(api.getDrivers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        try {

                            JSONArray rootArray = new JSONArray(responseBody.string());
                            if (rootArray.length() > 0) {
                                list.clear();
                                for (int i = 0; i < rootArray.length(); i++) {
                                    JSONObject single = rootArray.getJSONObject(i);
                                    int DriverId = single.getInt("DriverId");
                                    String driverName = single.getString("DriverName");

                                    DriverModel model = new DriverModel("" + DriverId, "" + driverName);
                                    list.add(model);
                                }
                                driverInterface.listOfDrivers(list);
                            } else {
                                driverInterface.error("No Driver Found!");
                            }

                        } catch (Exception e) {
                            driverInterface.error("" + e.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        driverInterface.error("" + throwable.getMessage());
                    }
                }));
    }

    public void getVehiclesList(VehicleInterface vehicleInterface) {
        List<VehicleModel> list = new ArrayList<>();
        vehicleDisposable.add(api.getVehicles().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        try {

                            JSONArray rootArray = new JSONArray(responseBody.string());
                            if (rootArray.length() > 0) {
                                list.clear();
                                for (int i = 0; i < rootArray.length(); i++) {
                                    JSONObject single = rootArray.getJSONObject(i);
                                    int TruckId = single.getInt("TruckId");
                                    String TruckName = single.getString("TruckName");

                                    VehicleModel model = new VehicleModel("" + TruckId, "" + TruckName);
                                    list.add(model);
                                }
                                vehicleInterface.listOfVehicle(list);
                            } else {
                                vehicleInterface.error("No Driver Found!");
                            }

                        } catch (Exception e) {
                            vehicleInterface.error("" + e.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        vehicleInterface.error("" + throwable.getMessage());
                    }
                }));
    }

    public void postCheckList(PostCheckListInterface postCheckListInterface, PostCheckListModel model) {
        postDisposable.add(api.postCheckList(
                        "" + model.getDriverName(),
                        "" + model.getDate(),
                        "" + model.getRadiatorAnswer(),
                        "" + model.getEngineOilAnswer(),
                        "" + model.getTyreConditionAnswer(),
                        "" + model.getSpareTyreAnswer(),
                        "" + model.getJackAnswer(),
                        "" + model.getSpannerAnswer(),
                        "" + model.getFireAnswer(),
                        "" + model.getWaterCapsAnswer(),
                        "" + model.getFuelCapsAnswer(),
                        "" + model.getDipstickAnswer(),
                        "" + model.getWindScreenAnswer(),
                        "" + model.getSideMirrorAnswer(),
                        "" + model.getMainLightsAnswer(),
                        "" + model.getIndicatorLightAnswer(),
                        "" + model.getBrakesAnswer(),
                        "" + model.getCabBodyAnswer(),
                        "" + model.getFridgeWaterAnswer(),
                        "" + model.getFridgeOilAnswer(),
                        "" + model.getFridgeBeltsAnswer(),
                        "" + model.getFridgeDeepStickAnswer(),
                        "" + model.getFridgeBodyAnswer(),
                        "" + model.getRadiatorWaterComment(),
                        "" + model.getEngineOilComment(),
                        "" + model.getTyreConditionComment(),
                        "" + model.getSpareTyreComment(),
                        "" + model.getJackComment(),
                        "" + model.getSpannerComment(),
                        "" + model.getFireExitComment(),
                        "" + model.getWaterCapsComment(),
                        "" + model.getFuelCapsComment(),
                        "" + model.getDipstickComment(),
                        "" + model.getWindScreenComment(),
                        "" + model.getSideMirrorsComment(),
                        "" + model.getMainLightsComment(),
                        "" + model.getIndicatorsLightsComment(),
                        "" + model.getBrakesComment(),
                        "" + model.getCabBodyComment(),
                        "" + model.getFridgeWaterComment(),
                        "" + model.getFridgeOilComment(),
                        "" + model.getFridgeBeltsComment(),
                        "" + model.getFridgeDeepStickComment(),
                        "" + model.getFridgeBodyComment(),
                        "" + model.getUserId(),
                        "" + model.getVehicleName()
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        try {
                            postCheckListInterface.success(""+responseBody.string());
                        }catch (Exception e) {
                            postCheckListInterface.error(""+e.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        postCheckListInterface.error(""+throwable.getMessage());
                    }
                }));
    }
}
