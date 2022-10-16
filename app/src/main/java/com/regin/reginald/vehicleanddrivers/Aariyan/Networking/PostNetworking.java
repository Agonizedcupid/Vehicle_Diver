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
        restApi = ApiClient.getClient(baseURl).create(RestApi.class);
    }

    public void uploadNewOrderLinesDetails(List<PostLinesModel> list, SuccessInterface successInterface, DatabaseAdapter databaseAdapter) {
        newOrderLinesDetailsDisposable.add(restApi.postNewLines(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        Log.d(TAG, "SUCCESS: " + responseBody.string());
                        JSONArray root = new JSONArray(responseBody.string());
                        Log.d(TAG, "SUCCESS: " + root);
                        JSONObject single = root.getJSONObject(0);
                        String OrderDetailId = single.getString("OrderDetailId");
                        //After a successful posting just update the Local database:
                        updateOrderLinesLocalDatabase(OrderDetailId, databaseAdapter);

                        //TODO : After success, need to work on that:
                        orderHeaderPost(databaseAdapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "ERROR: " + throwable.getMessage());
                        successInterface.onError("ERROR(PostNetworking)" + throwable.getMessage());
                    }
                }));
    }

    private void orderHeaderPost(DatabaseAdapter dbH) {

        ArrayList<Orders> dealLineToUpload = dbH.getOrderHeadersNotUploaded();
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