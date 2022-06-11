package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import android.util.Log;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.ApiClient;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetOrderTypeInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetRouteInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.RestApi;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NetworkingFeedback {

    private CompositeDisposable routeDisposable;
    private DatabaseAdapter databaseAdapter;
    private RestApi apiService;

    public NetworkingFeedback(DatabaseAdapter databaseAdapter) {
        routeDisposable = new CompositeDisposable();
        this.databaseAdapter = databaseAdapter;
    }


    /**
     *  GET Route
     */


    public void getAvailableRoute(GetRouteInterface routeInterface) {
        //check the table is exist or not:
        List<RouteModel> routeList = databaseAdapter.getRoutes();
        List<IpModel> serverIpModel = databaseAdapter.getServerIpModel();
        if (routeList.size() > 0) { //Get it from Local storage if available:
            routeInterface.gotRoute(routeList);
            return;
        } else { //else run the Network Call:
            apiService = ApiClient.getClient(serverIpModel.get(0).getServerIp()).create(RestApi.class);
            routeInterface.error("No route is available!, We are syncing route for you.");
            routeDisposable.add(apiService.getRoutes()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Throwable {
                            JSONArray rootArray = new JSONArray(responseBody.string());
                            routeList.clear();
                            if (rootArray.length() > 0) {//If data found

                                for (int i = 0; i < rootArray.length(); i++) {
                                    JSONObject single = rootArray.getJSONObject(i);
                                    int RouteId = single.getInt("RouteId");
                                    String RouteName = single.getString("RouteName");

                                    RouteModel model = new RouteModel(RouteId, RouteName);
                                    routeList.add(model);
                                }
                                insertRouteIntoLocalStorage(routeList);
                                routeInterface.gotRoute(routeList);

                            } else {
                                routeInterface.error("No Route Found!");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            routeInterface.error(throwable.getMessage());
                        }
                    }));
        }
    }

    //Insert Route into SQLite database:
    private void insertRouteIntoLocalStorage(List<RouteModel> routeList) {
        Observable<RouteModel> observable = Observable.fromIterable(routeList)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if(d.isDisposed()) {
                    routeDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                RouteModel routeModel = (RouteModel) o;
                databaseAdapter.insertRoutes(routeModel.getRouteId(),routeModel.getRouteName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_ROUTE", "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_ROUTE", "onComplete: Insert completed!");
            }
        };

        observable.subscribe(observer);
    }

    /**
     *  GET Order types
     */

    public void getAvailableOrder(GetOrderTypeInterface orderInterface, String subscriberId) {
        //check the table is exist or not:
        List<OrderTypeModel> orderList = databaseAdapter.getOrderTypes();
        List<IpModel> serverIpModel = databaseAdapter.getServerIpModel();
        if (orderList.size() > 0) { //Get it from Local storage if available:
            orderInterface.gotOrderType(orderList);
            return;
        } else { //else run the Network Call:
            apiService = ApiClient.getClient(serverIpModel.get(0).getServerIp()).create(RestApi.class);
            orderInterface.error("No order is available!, We are syncing order for you.");
            routeDisposable.add(apiService.getOrderTypes(subscriberId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Throwable {
                            JSONArray rootArray = new JSONArray(responseBody.string());
                            orderList.clear();
                            if (rootArray.length() > 0) {//If data found

                                for (int i = 0; i < rootArray.length(); i++) {
                                    JSONObject single = rootArray.getJSONObject(i);
                                    int OrderTypeId = single.getInt("OrderTypeId");
                                    String OrderType = single.getString("OrderType");

                                    OrderTypeModel model = new OrderTypeModel(OrderTypeId, OrderType);
                                    orderList.add(model);
                                }
                                insertOrderIntoLocalStorage(orderList);
                                orderInterface.gotOrderType(orderList);

                            } else {
                                orderInterface.error("No Order Found!");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            orderInterface.error(throwable.getMessage());
                        }
                    }));
        }
    }

    private void insertOrderIntoLocalStorage(List<OrderTypeModel> orderList) {

        Observable<OrderTypeModel> observable = Observable.fromIterable(orderList)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if(d.isDisposed()) {
                    routeDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                OrderTypeModel orderModel = (OrderTypeModel) o;
                databaseAdapter.insertOrderTypes(orderModel.getOrderTypeId(),orderModel.getOrderType());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_ORDER", "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_ORDER", "onComplete: Insert completed!");
            }
        };

        observable.subscribe(observer);
    }
}
