package com.regin.reginald.vehicleanddrivers.Aariyan.Service;

import android.util.Log;

import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.SuccessInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostLinesModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.PostNetworking;
import com.regin.reginald.vehicleanddrivers.AppApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monitoring implements Runnable {
    private static final String TAG = "Monitoring";
    int howManyPosted = 0;
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    private DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    private String IP, deviceId;

    //List from database:
    private ArrayList<SettingsModel> settings = new ArrayList<>();
    private ArrayList<OrderLines> dealLineToUpload = new ArrayList<>();

    public Monitoring() {

        //Setting part:
//        settings = dbH.getSettings();
//        for (SettingsModel model : settings) {
//            IP = model.getstrServerIp();
//            deviceId = model.getDeviceID();
//        }
    }

    @Override
    public void run() {
        // getting the lines that not uploaded yet:
        dealLineToUpload = dbH.returnOrderLinesInfoUploaded();
        preparingForPosting(dealLineToUpload);
    }

    private void preparingForPosting(ArrayList<OrderLines> dealLineToUpload) {
        List<PostLinesModel> listToBeUploadedOrderLines = new ArrayList<>();
        if (dealLineToUpload.size() > 0) {
            settings = dbH.getSettings();
            for (SettingsModel model : settings) {
                IP = model.getstrServerIp();
                deviceId = model.getDeviceID();
            }
        } else {
            return;
        }
        for (OrderLines orderAttributes : dealLineToUpload) {
            JSONObject json = new JSONObject();
            //String orderDID, int offloaded, float returnQty,String offLoadComment,int blnoffloaded

            String returning = "NULL";
            String offcomment = "NULL";
            String reasons = "NULL";

            if (orderAttributes.getreturnQty() != null && !orderAttributes.getreturnQty().isEmpty()) {
                returning = orderAttributes.getreturnQty();
            }
            if (orderAttributes.getoffLoadComment() != null && !orderAttributes.getoffLoadComment().isEmpty()) {
                offcomment = orderAttributes.getoffLoadComment();
            }
            if (orderAttributes.getstrCustomerReason() != null && !orderAttributes.getstrCustomerReason().isEmpty()) {
                reasons = orderAttributes.getstrCustomerReason();
            }

            String c = offcomment;
            Pattern pt = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
            Matcher match = pt.matcher(c);
            if (!match.matches()) {
                c = c.replaceAll(pt.pattern(), " ");
            }
            offcomment = c;

            String r = reasons;
            Pattern ptr = Pattern.compile("[^a-zA-Z0-9/?:().,'+/-]");
            Matcher matchr = ptr.matcher(r);
            if (!matchr.matches()) {
                r = r.replaceAll(ptr.pattern(), " ");
            }
            reasons = r;

//            json.put("orderDID", orderAttributes.getOrderDetailId());
//            json.put("returnQty", returning);
//            json.put("offLoadComment", offcomment);
//            json.put("blnoffloaded", orderAttributes.getblnoffloaded());
//            json.put("reasons", reasons);

            //Making the model of that code:
            PostLinesModel postLinesModel = new PostLinesModel(
                    orderAttributes.getOrderDetailId(), returning, offcomment, orderAttributes.getblnoffloaded(), reasons
            );
            listToBeUploadedOrderLines.add(postLinesModel);
            new PostNetworking(IP).uploadNewOrderLinesDetails(listToBeUploadedOrderLines, new SuccessInterface() {
                @Override
                public void onSuccess(String successMessage) {
                    howManyPosted++;
                    if (howManyPosted == dealLineToUpload.size()) {
                        Log.d(TAG, "onSuccess: Post Completed");
                    } else {
                        Log.d(TAG, "onSuccess: Left : " + (Math.abs(dealLineToUpload.size() - howManyPosted)));
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.d(TAG, "onError:(Line: 113) : " + errorMessage);
                }
            },dbH);
//            Log.e("blnoffloaded", "*************+" + "****" + orderAttributes.getblnoffloaded() + "******" + returning);
//            Log.e("offcomment", "*************+" + "****" + offcomment);
//            jsonArray.put(json);
//            count++;

        }
    }

}
