package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import android.util.Log;

import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.ApiClient;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.RestApi;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.SuccessInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.FridgeTempModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostHeadersModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostLinesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PostNetworking {
    public static final String TAG = "PostNetworking";

    private CompositeDisposable newOrderLinesDetailsDisposable = new CompositeDisposable();
    private CompositeDisposable royaltyDisposable = new CompositeDisposable();

    RestApi restApi;

    public PostNetworking(String baseURl) {
        Log.d(TAG, "IP: "+baseURl);
        restApi = ApiClient.getClient(baseURl).create(RestApi.class);
    }
    //TODO: POSTING ORDER LINES
    public void uploadNewOrderLinesDetails(List<PostLinesModel> list, SuccessInterface successInterface, DatabaseAdapter databaseAdapter) {
        newOrderLinesDetailsDisposable.add(restApi.postNewLines(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {
                        try {
                            //Log.d(TAG, "SUCCESS: " + responseBody.string());
                            JSONArray root = new JSONArray(responseBody.string());
                            Log.d(TAG, "response: "+responseBody.string() + " Root: "+root.length());
                            Log.d(TAG, "SUCCESS: " + root);
                            for (int i=0; i<root.length(); i++) {
                                JSONObject single = root.getJSONObject(i);
                                int OrderDetailId = single.getInt("OrderDetailId");
                                databaseAdapter.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + OrderDetailId + ")");
                                successInterface.onSuccess("Posted Successfully");
                                //After a successful posting just update the Local database:
                                updateOrderLinesLocalDatabase(String.valueOf(OrderDetailId), databaseAdapter);
                                Log.d(TAG, "accept: "+OrderDetailId);
                                //TODO : After success, need to work on that:
                                orderHeaderPost(databaseAdapter);
                            }

                        }catch (Exception e) {
                            Log.d(TAG, "Exception: "+e.getMessage());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "ERROR: " + throwable.getMessage());
                        orderHeaderPost(databaseAdapter);
                        successInterface.onError("ERROR(PostNetworking)" + throwable.getMessage());
                    }
                }));
    }

    public void orderHeaderPost(DatabaseAdapter dbH) {
        ArrayList<Orders> dealLineToUpload = dbH.getOrderHeadersNotUploaded();
        Log.d(TAG, "orderHeaderPost: "+dealLineToUpload.size());
        for (Orders orderAttributes : dealLineToUpload) {

            String strNotesDrivers = "NULL";
            String strEmailAddress = "NULL";
            String strCashSig = "NULL";
            String strStartTime = "NULL";
            String strEndTime = "NULL";
            String strTheImage = "NoImage";
            String signedBy = "NULL";
            if (orderAttributes.getstrNotesDrivers() != null && !orderAttributes.getstrNotesDrivers().isEmpty()) {
                strNotesDrivers = orderAttributes.getstrNotesDrivers();
            }
            if (orderAttributes.getstrEmailCustomer() != null && !orderAttributes.getstrEmailCustomer().isEmpty()) {
                strEmailAddress = orderAttributes.getstrEmailCustomer();
            }
            if (orderAttributes.getstrCashsignature() != null && !orderAttributes.getstrCashsignature().isEmpty()) {
                strCashSig = orderAttributes.getstrCashsignature();
            }

            if (orderAttributes.getStartTripTime() != null && !orderAttributes.getStartTripTime().isEmpty()) {
                strStartTime = orderAttributes.getStartTripTime();
            }
            if (orderAttributes.getEndTripTime() != null && !orderAttributes.getEndTripTime().isEmpty()) {
                strEndTime = orderAttributes.getEndTripTime();
            }
            if (orderAttributes.getimagestring() != null && !orderAttributes.getimagestring().isEmpty()) {
                strTheImage = orderAttributes.getimagestring();
            }
            if (orderAttributes.getstrCustomerSignedBy() != null && !orderAttributes.getstrCustomerSignedBy().isEmpty()) {
                signedBy = orderAttributes.getstrCustomerSignedBy();
            }
            String fridgeTemp = "0.0";
            List<FridgeTempModel> oneTempNeeded = dbH.getFridgeTempByInvoice(orderAttributes.getInvoiceNo());
            if (oneTempNeeded.size() > 0) {
                fridgeTemp = oneTempNeeded.get(0).getFridgeTemp();
            }
            PostHeadersModel postHeadersModel = new PostHeadersModel(
                    orderAttributes.getInvoiceNo(),
                    orderAttributes.getLatitude(),
                    orderAttributes.getLongitude(),
                    strTheImage,
                    orderAttributes.getCashPaid(),
                    strNotesDrivers,
                    orderAttributes.getoffloaded(),
                    strEmailAddress,
                    strCashSig,
                    strStartTime,
                    strEndTime,
                    orderAttributes.getDeliverySequence(),
                    orderAttributes.getstrCoordinateStart(),
                    signedBy,
                    ""+fridgeTemp
            );

            uploadHeaders(postHeadersModel, dbH);
        }
    }

    //TODO: POSTING ORDER HEADERS
    private void uploadHeaders(PostHeadersModel postHeadersModel, DatabaseAdapter databaseAdapter) {
        List<PostHeadersModel> list = new ArrayList<>();
        list.add(postHeadersModel);

        royaltyDisposable.add(restApi.postHeaders(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray root = new JSONArray(responseBody.string());
                        Log.d(TAG, "SUCCESS: (LINE 149:) : "+root);
                        for (int j = 0; j < root.length(); ++j) {

                            JSONObject BoardDetails = root.getJSONObject(j);
                            String ID, strPartNumber;
                            ID = BoardDetails.getString("InvoiceNo");

                            Log.d(TAG, "SUCCESS: (LINE 156:) : "+root);
                            updateOrderHeadersInLocalDatabase(databaseAdapter, ID);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "ERROR: (LINE 163:) : "+throwable.getMessage());
                    }
                }));

    }

    private void updateOrderHeadersInLocalDatabase(DatabaseAdapter databaseAdapter, String ID) {
        databaseAdapter.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + ID + "'");
    }

    /**
     * Local database part:
     */

    private void updateOrderLinesLocalDatabase(String responseBody, DatabaseAdapter databaseAdapter) {
        databaseAdapter.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");
    }
}
