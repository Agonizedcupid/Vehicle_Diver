package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import android.util.Log;

import com.google.gson.Gson;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.ApiClient;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetOrderTypeInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetRouteInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.OrderListInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.RestApi;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderLinesModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.OrderLines;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NetworkingFeedback {

    private CompositeDisposable routeDisposable, orderDisposable, orderLinesDisposable;
    private DatabaseAdapter databaseAdapter;
    private RestApi apiService;

    public NetworkingFeedback(DatabaseAdapter databaseAdapter) {
        routeDisposable = new CompositeDisposable();
        orderDisposable = new CompositeDisposable();
        orderLinesDisposable = new CompositeDisposable();
        this.databaseAdapter = databaseAdapter;
    }


    /**
     * GET Route
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
                if (d.isDisposed()) {
                    routeDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                RouteModel routeModel = (RouteModel) o;
                databaseAdapter.insertRoutes(routeModel.getRouteId(), routeModel.getRouteName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_ROUTE", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_ROUTE", "onComplete: Insert completed!");
            }
        };

        observable.subscribe(observer);
    }

    /**
     * GET Order types
     */

    public void getAvailableOrder(GetOrderTypeInterface orderInterface, String subscriberId) {
        //check the table is exist or not:
        List<OrderTypeModel> orderList = databaseAdapter.getOrderTypes();
        List<IpModel> serverIpModel = databaseAdapter.getServerIpModel();
        if (orderList.size() > 2) { //Get it from Local storage if available:
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
                if (d.isDisposed()) {
                    routeDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                OrderTypeModel orderModel = (OrderTypeModel) o;
                databaseAdapter.insertOrderTypes(orderModel.getOrderTypeId(), orderModel.getOrderType());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_ORDER", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_ORDER", "onComplete: Insert completed!");
            }
        };

        observable.subscribe(observer);
    }

    /**
     * GET Available Orders:
     */

    public void getOrdersHeaders(OrderListInterface orderListInterface, String serverIp, String orderDate, int routeId, int orderId) {
        List<OrderModel> listOfOrders = new ArrayList<>();

        apiService = ApiClient.getClient(serverIp).create(RestApi.class);
        //orderListInterface.onError("No order is available!");
        orderDisposable.add(apiService.getOrderHeaders(orderId, routeId, orderDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray rootArray = new JSONArray(responseBody.string());
                        Log.d("RESPONSE_TESTING", "accept: server: " + serverIp +
                                "\n date: " + orderDate + "\n route: " + routeId + "\n order: " + orderId +
                                "\n Length: " + rootArray.length());
                        listOfOrders.clear();
                        if (rootArray.length() > 0) {//If data found
//                            Gson gson = new Gson();
//                            OrderModel model = gson.fromJson(responseBody.string(), OrderModel.class);
//                            listOfOrders.add(model);
//                            insertOrderListIntoLocalStorage(listOfOrders);
                            for (int i = 0; i < rootArray.length(); i++) {
                                JSONObject single = rootArray.getJSONObject(i);

                                int OrderId;
                                if (single.has("OrderId")) {
                                    OrderId = single.getInt("OrderId");
                                } else {
                                    OrderId = -1;
                                }

                                String InvoiceNo;
                                if (single.has("InvoiceNo")) {
                                    InvoiceNo = single.getString("InvoiceNo");
                                } else {
                                    InvoiceNo = "No Invoice Found";
                                }

                                String CustomerPastelCode;
                                if (single.has("CustomerPastelCode")) {
                                    CustomerPastelCode = single.getString("CustomerPastelCode");
                                } else {
                                    CustomerPastelCode = "Customer Pastel Code Missing";
                                }

                                String StoreName;
                                if (single.has("StoreName")) {
                                    StoreName = single.getString("StoreName");
                                } else {
                                    StoreName = "Store Name Missing";
                                }

                                String DeliveryDate;
                                if (single.has("DeliveryDate")) {
                                    DeliveryDate = single.getString("DeliveryDate");
                                } else {
                                    DeliveryDate = "Date Missing";
                                }

                                double Latitude;
                                if (single.has("Latitude")) {
                                    Latitude = single.getDouble("Latitude");
                                } else {
                                    Latitude = -777;
                                }

                                double Longitude;
                                if (single.has("Longitude")) {
                                    Longitude = single.getDouble("Longitude");
                                } else {
                                    Longitude = -777;
                                }

                                String OrderValueExc;
                                if (single.has("OrderValueExc")) {
                                    OrderValueExc = single.getString("OrderValueExc");
                                } else {
                                    OrderValueExc = "Value Missing";
                                }

                                String OrderValueInc;
                                if (single.has("OrderValueInc")) {
                                    OrderValueInc = single.getString("OrderValueInc");
                                } else {
                                    OrderValueInc = "Value Missing";
                                }

                                String DeliveryAddress;
                                if (single.has("DeliveryAddress")) {
                                    DeliveryAddress = single.getString("DeliveryAddress");
                                } else {
                                    DeliveryAddress = "Value Missing";
                                }

                                String User;
                                if (single.has("User")) {
                                    User = single.getString("User");
                                } else {
                                    User = "Value Missing";
                                }

                                String OrderMass;
                                if (single.has("OrderMass")) {
                                    OrderMass = single.getString("OrderMass");
                                } else {
                                    OrderMass = "Value Missing";
                                }

                                int Uploaded;
                                if (single.has("Uploaded")) {
                                    Uploaded = single.getInt("Uploaded");
                                } else {
                                    Uploaded = -777;
                                }

                                String CashPaid;
                                if (single.has("CashPaid")) {
                                    CashPaid = single.getString("CashPaid");
                                } else {
                                    CashPaid = "Value Missing";
                                }

                                int offloaded;
                                if (single.has("offloaded")) {
                                    offloaded = single.getInt("offloaded");
                                } else {
                                    offloaded = -777;
                                }

                                String strEmailCustomer;
                                if (single.has("strEmailCustomer")) {
                                    strEmailCustomer = single.getString("strEmailCustomer");
                                } else {
                                    strEmailCustomer = "Value Missing";
                                }

                                String strCashsignature;
                                if (single.has("strCashsignature")) {
                                    strCashsignature = single.getString("strCashsignature");
                                } else {
                                    strCashsignature = "Value Missing";
                                }

                                String InvTotIncl;
                                if (single.has("InvTotIncl")) {
                                    InvTotIncl = single.getString("InvTotIncl");
                                } else {
                                    InvTotIncl = "Value Missing";
                                }

                                String StartTripTime;
                                if (single.has("StartTripTime")) {
                                    StartTripTime = single.getString("StartTripTime");
                                } else {
                                    StartTripTime = "Value Missing";
                                }

                                String EndTripTime;
                                if (single.has("EndTripTime")) {
                                    EndTripTime = single.getString("EndTripTime");
                                } else {
                                    EndTripTime = "Value Missing";
                                }

                                int DeliverySeq;
                                if (single.has("DeliverySeq")) {
                                    DeliverySeq = single.getInt("DeliverySeq");
                                } else {
                                    DeliverySeq = -777;
                                }

                                String strCoordinateStart;
                                if (single.has("strCoordinateStart")) {
                                    strCoordinateStart = single.getString("strCoordinateStart");
                                } else {
                                    strCoordinateStart = "Value Missing";
                                }

                                String DriverName;
                                if (single.has("DriverName")) {
                                    DriverName = single.getString("DriverName");
                                } else {
                                    DriverName = "Value Missing";
                                }

                                String DriverEmail;
                                if (single.has("DriverEmail")) {
                                    DriverEmail = single.getString("DriverEmail");
                                } else {
                                    DriverEmail = "Value Missing";
                                }

                                String DriverPassword;
                                if (single.has("DriverPassword")) {
                                    DriverPassword = single.getString("DriverPassword");
                                } else {
                                    DriverPassword = "Value Missing";
                                }

                                String strCustomerSignedBy;
                                if (single.has("strCustomerSignedBy")) {
                                    strCustomerSignedBy = single.getString("strCustomerSignedBy");
                                } else {
                                    strCustomerSignedBy = "Value Missing";
                                }

                                String Threshold;
                                if (single.has("Threshold")) {
                                    Threshold = single.getString("Threshold");
                                } else {
                                    Threshold = "Value Missing";
                                }

                                listOfOrders.add(new OrderModel(
                                        OrderId,
                                        InvoiceNo,
                                        CustomerPastelCode,
                                        StoreName,
                                        DeliveryDate,
                                        Latitude,
                                        Longitude,
                                        OrderValueExc,
                                        OrderValueInc,
                                        DeliveryAddress,
                                        User,
                                        OrderMass,
                                        Uploaded,
                                        CashPaid,
                                        offloaded,
                                        strEmailCustomer,
                                        strCashsignature,
                                        InvTotIncl,
                                        StartTripTime,
                                        EndTripTime,
                                        DeliverySeq,
                                        strCoordinateStart,
                                        DriverName,
                                        DriverEmail,
                                        DriverPassword,
                                        strCustomerSignedBy,
                                        Threshold
                                ));


                                //Log.d("RESPONSE_TESTING", "accept: " + single.getInt("OrderId"));
                            }
                            orderListInterface.gotOrders(listOfOrders);
                            insertOrdersIntoSQLiteDatabase(listOfOrders);

                            //Fetching the Orders Lines:
                            getOrderLines(serverIp, orderDate, routeId, orderId);

                            Log.d("RESPONSE_TESTING", "Inner: " + rootArray);

                        } else {
                            orderListInterface.onError("No Order Found!");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        orderListInterface.onError(throwable.getMessage());
                        Log.d("RESPONSE_TESTING", "exception: " + throwable.getMessage());
                    }
                }));

    }


    /**
     * GET Order lines
     */

    public void getOrderLines(String serverIp, String orderDate, int routeId, int orderId) {
        List<OrderLinesModel> orderLinesList = new ArrayList<>();

        apiService = ApiClient.getClient(serverIp).create(RestApi.class);
        //orderListInterface.onError("No order is available!");
        orderLinesDisposable.add(apiService.getOrderLines(orderId, routeId, orderDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray rootArray = new JSONArray(responseBody.string());
                        Log.d("RESPONSE_TESTING", "accept: server: " + serverIp +
                                "\n date: " + orderDate + "\n route: " + routeId + "\n order: " + orderId +
                                "\n Length: " + rootArray.length());
                        orderLinesList.clear();
                        if (rootArray.length() > 0) {//If data found
//                            Gson gson = new Gson();
//                            OrderModel model = gson.fromJson(responseBody.string(), OrderModel.class);
//                            listOfOrders.add(model);
//                            insertOrderListIntoLocalStorage(listOfOrders);
                            for (int i = 0; i < rootArray.length(); i++) {
                                JSONObject single = rootArray.getJSONObject(i);

                                String OrderId;
                                if (single.has("OrderId")) {
                                    OrderId = single.getString("OrderId");
                                } else {
                                    OrderId = "Value Missing";
                                }

                                String InvoiceNo;
                                if (single.has("InvoiceNo")) {
                                    InvoiceNo = single.getString("InvoiceNo");
                                } else {
                                    InvoiceNo = "No Invoice Found";
                                }

                                String CustomerPastelCode;
                                if (single.has("CustomerPastelCode")) {
                                    CustomerPastelCode = single.getString("CustomerPastelCode");
                                } else {
                                    CustomerPastelCode = "Customer Pastel Code Missing";
                                }

                                String StoreName;
                                if (single.has("StoreName")) {
                                    StoreName = single.getString("StoreName");
                                } else {
                                    StoreName = "Store Name Missing";
                                }

                                String DeliveryDate;
                                if (single.has("DeliveryDate")) {
                                    DeliveryDate = single.getString("DeliveryDate");
                                } else {
                                    DeliveryDate = "Date Missing";
                                }

                                double Latitude;
                                if (single.has("Latitude")) {
                                    Latitude = single.getDouble("Latitude");
                                } else {
                                    Latitude = -777;
                                }

                                double Longitude;
                                if (single.has("Longitude")) {
                                    Longitude = single.getDouble("Longitude");
                                } else {
                                    Longitude = -777;
                                }

                                double OrderValueExc;
                                if (single.has("OrderValueExc")) {
                                    OrderValueExc = single.getDouble("OrderValueExc");
                                } else {
                                    OrderValueExc = -0.0;
                                }

                                double OrderValueInc;
                                if (single.has("OrderValueInc")) {
                                    OrderValueInc = single.getDouble("OrderValueInc");
                                } else {
                                    OrderValueInc = -0.0;
                                }

                                String DeliveryAddress;
                                if (single.has("DeliveryAddress")) {
                                    DeliveryAddress = single.getString("DeliveryAddress");
                                } else {
                                    DeliveryAddress = "Value Missing";
                                }

                                String User;
                                if (single.has("User")) {
                                    User = single.getString("User");
                                } else {
                                    User = "Value Missing";
                                }

                                int OrderMass;
                                if (single.has("OrderMass")) {
                                    OrderMass = single.getInt("OrderMass");
                                } else {
                                    OrderMass = -777;
                                }

                                int ProductId;
                                if (single.has("ProductId")) {
                                    ProductId = single.getInt("ProductId");
                                } else {
                                    ProductId = -777;
                                }

                                int Qty;
                                if (single.has("Qty")) {
                                    Qty = single.getInt("Qty");
                                } else {
                                    Qty = -777;
                                }

                                double Price;
                                if (single.has("Price")) {
                                    Price = single.getDouble("Price");
                                } else {
                                    Price = -777;
                                }

                                String PastelDescription;
                                if (single.has("PastelDescription")) {
                                    PastelDescription = single.getString("PastelDescription");
                                } else {
                                    PastelDescription = "Value Missing";
                                }


                                String PastelCode;
                                if (single.has("PastelCode")) {
                                    PastelCode = single.getString("PastelCode");
                                } else {
                                    PastelCode = "Value Missing";
                                }

                                int OrderDetailId;
                                if (single.has("OrderDetailId")) {
                                    OrderDetailId = single.getInt("OrderDetailId");
                                } else {
                                    OrderDetailId = -777;
                                }

                                String Comment;
                                if (single.has("Comment")) {
                                    Comment = single.getString("Comment");
                                } else {
                                    Comment = "Value Missing";
                                }

                                String returnQty;
                                if (single.has("returnQty")) {
                                    returnQty = single.getString("returnQty");
                                } else {
                                    returnQty = "Value Missing";
                                }

                                String offLoadComment;
                                if (single.has("offLoadComment")) {
                                    offLoadComment = single.getString("offLoadComment");
                                } else {
                                    offLoadComment = "Value Missing";
                                }

                                int blnoffloaded;
                                if (single.has("blnoffloaded")) {
                                    blnoffloaded = single.getInt("blnoffloaded");
                                } else {
                                    blnoffloaded = -777;
                                }

                                String strCustomerReason;
                                if (single.has("strCustomerReason")) {
                                    strCustomerReason = single.getString("strCustomerReason");
                                } else {
                                    strCustomerReason = "Value Missing";
                                }


                                String intWareHouseId;
                                if (single.has("intWareHouseId")) {
                                    intWareHouseId = single.getString("intWareHouseId");
                                } else {
                                    intWareHouseId = "Value Missing";
                                }

                                String WareHouseName;
                                if (single.has("WareHouseName")) {
                                    WareHouseName = single.getString("WareHouseName");
                                } else {
                                    WareHouseName = "Value Missing";
                                }


                                orderLinesList.add(new OrderLinesModel(
                                        OrderId,
                                        InvoiceNo,
                                        CustomerPastelCode,
                                        StoreName,
                                        DeliveryDate,
                                        Latitude,
                                        Longitude,
                                        OrderValueExc,
                                        OrderValueInc,
                                        DeliveryAddress,
                                        User,
                                        OrderMass,
                                        ProductId,
                                        Qty,
                                        Price,
                                        PastelDescription,
                                        PastelCode,
                                        OrderDetailId,
                                        Comment,
                                        returnQty,
                                        offLoadComment,
                                        blnoffloaded,
                                        strCustomerReason,
                                        -1,
                                        intWareHouseId,
                                        WareHouseName
                                ));


                                //Log.d("RESPONSE_TESTING", "accept: " + single.getInt("OrderId"));
                            }
                            insertOrderLinesIntoSQLiteDatabase(orderLinesList);

                            //Fetching the Orders Lines:
                            //getOrderLines(serverIp, orderDate, routeId, orderId);

                            Log.d("RESPONSE_TESTING", "Inner: " + rootArray);

                        } else {
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("RESPONSE_TESTING", "exception: " + throwable.getMessage());
                    }
                }));

    }

    private void insertOrderLinesIntoSQLiteDatabase(List<OrderLinesModel> orderLinesList) {

        Observable<OrderLinesModel> observable = Observable.fromIterable(orderLinesList)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (d.isDisposed()) {
                    orderLinesDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                OrderLinesModel model = (OrderLinesModel) o;
                databaseAdapter.insertOrderLines(model);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("ORDER_LINES_ERROR", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("ORDER_LINES_COMPLETED", "onComplete: ");
            }
        };

        observable.subscribe(observer);
    }


    /**
     * Insert order into SQLite database:
     *
     * @param listOfOrders
     */
    private void insertOrdersIntoSQLiteDatabase(List<OrderModel> listOfOrders) {
        databaseAdapter.dropOrderTable();
        Observable<OrderModel> observable = Observable.fromIterable(listOfOrders)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (d.isDisposed()) {
                    orderDisposable.clear();
                }
            }

            @Override
            public void onNext(Object o) {
                OrderModel model = (OrderModel) o;
                databaseAdapter.insertOrders(model);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_ERROR", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_COMPLETED", "onComplete: ");
            }
        };
        observable.subscribe(observer);

    }
}
