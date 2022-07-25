package com.regin.reginald.vehicleanddrivers.Aariyan.Database;

import static com.loopj.android.http.AsyncHttpClient.log;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.regin.reginald.model.CheckList;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OrderTypes;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.Routes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.model.WareHouses;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.LogInModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderLinesModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.WareHousesModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    DatabaseHelper helper;


    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }


    /**
     * INSERT Database
     */
    //Insert into Profile
    //Insert Server:
    public long insertLogIn(LogInModel model) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.id, model.getId());
        contentValues.put(DatabaseHelper.companyName, model.getCompanyName());
        contentValues.put(DatabaseHelper.tabletRegId, model.getTabletRegId());
        contentValues.put(DatabaseHelper.driverNames, model.getDriverName());
        contentValues.put(DatabaseHelper.driverEmails, model.getDriverEmail());
        contentValues.put(DatabaseHelper.driverPasswords, model.getDriverPassword());
        contentValues.put(DatabaseHelper.IP, model.getIP());

        long id = database.insert(DatabaseHelper.LOG_IN_TABLE, null, contentValues);
        return id;
    }


    //Insert Server:
    public long insertServer(String serverIp, String emailAddress, String companyName, String deviceId, String regKey) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.serverIP, serverIp);
        contentValues.put(DatabaseHelper.emailId, emailAddress);
        contentValues.put(DatabaseHelper.companyName, companyName);
        contentValues.put(DatabaseHelper.deviceId, deviceId);
        contentValues.put(DatabaseHelper.regKey, regKey);

        long id = database.insert(DatabaseHelper.SERVER_IP_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert ROUTES:
    public long insertRoutes(int routeId, String routeName) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ROUTE_ID, routeId);
        contentValues.put(DatabaseHelper.ROUTE_NAME, routeName);

        long id = database.insert(DatabaseHelper.ROUTE_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert ORDER_TYPE:
    public long insertOrderTypes(int orderID, String orderType) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ORDER_TYPE_ID, orderID);
        contentValues.put(DatabaseHelper.ORDER_TYPE, orderType);

        long id = database.insert(DatabaseHelper.ORDER_TYPE_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert WAREHOUSE:
    public long insertWareHouses(WareHouses model) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.WareHouseId, model.getWareHouseId());
        contentValues.put(DatabaseHelper.WareHouse, model.getWareHouse());

        long id = database.insert(DatabaseHelper.WAREHOUSE_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert Orders
    public long insertOrders(Orders model) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.orderId, model.getOrderId());
        contentValues.put(DatabaseHelper.invoiceNo, model.getInvoiceNo());
        contentValues.put(DatabaseHelper.customerPastelCode, model.getCustomerPastelCode());
        contentValues.put(DatabaseHelper.storeName, model.getStoreName());
        contentValues.put(DatabaseHelper.deliveryDate, model.getDeliveryDate());
        contentValues.put(DatabaseHelper.latitude, model.getLatitude());
        contentValues.put(DatabaseHelper.longitude, model.getLongitude());
        contentValues.put(DatabaseHelper.orderValueExc, model.getOrderValueExc());
        contentValues.put(DatabaseHelper.orderValueInc, model.getOrderValueInc());
        contentValues.put(DatabaseHelper.deliveryAddress, model.getDeliveryAddress());
        contentValues.put(DatabaseHelper.user, model.getUser());
        contentValues.put(DatabaseHelper.orderMass, model.getOrderMass());
        contentValues.put(DatabaseHelper.uploaded, 1);
        contentValues.put(DatabaseHelper.cashPaid, model.getCashPaid());
        contentValues.put(DatabaseHelper.offloaded, model.getoffloaded());
        contentValues.put(DatabaseHelper.strEmailCustomer, model.getstrEmailCustomer());
        contentValues.put(DatabaseHelper.strCashsignature, model.getstrCashsignature());
        contentValues.put(DatabaseHelper.invTotIncl, model.getInvTotIncl());
        contentValues.put(DatabaseHelper.startTripTime, model.getStartTripTime());
        contentValues.put(DatabaseHelper.endTripTime, model.getEndTripTime());
        contentValues.put(DatabaseHelper.deliverySeq, model.getDeliverySeq());
        contentValues.put(DatabaseHelper.strCoordinateStart, model.getstrCoordinateStart());
        contentValues.put(DatabaseHelper.driverName, model.getDriverName());
        contentValues.put(DatabaseHelper.driverEmail, model.getDriverEmail());
        contentValues.put(DatabaseHelper.driverPassword, model.getDriverPassword());
        contentValues.put(DatabaseHelper.strCustomerSignedBy, model.getstrCustomerSignedBy());
        //contentValues.put(DatabaseHelper.threshold, model.getThreshold()); // Original Value
        contentValues.put(DatabaseHelper.threshold, 0); // Using "0" for testing purpose

        contentValues.put(DatabaseHelper.imagestring, model.getimagestring());
        contentValues.put(DatabaseHelper.strNotesDrivers, model.getstrNotesDrivers());
        contentValues.put(DatabaseHelper.PaymentType, model.getPaymentType());
        contentValues.put(DatabaseHelper.Loyalty, model.getLoyalty());

        long id = database.insert(DatabaseHelper.ORDERS_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert OrdersLines
    public long insertOrderLines(OrderLines model) {
        SQLiteDatabase database = helper.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.orderId, model.getOrderId());
        contentValues.put(DatabaseHelper.productId, model.getProductId());
        //contentValues.put(DatabaseHelper.invoiceNo, model.getInvoiceNo());
        //contentValues.put(DatabaseHelper.invoiceNo, "");// "" -> Means -> No data found in the API
        //contentValues.put(DatabaseHelper.customerPastelCode, model.getPastelCode());
        //contentValues.put(DatabaseHelper.storeName, "");
        contentValues.put(DatabaseHelper.pastelDescription, model.getPastelDescription());
        contentValues.put(DatabaseHelper.pastelCode, model.getPastelCode());
        contentValues.put(DatabaseHelper.qty, model.getQty());
        contentValues.put(DatabaseHelper.price, model.getPrice());
        contentValues.put(DatabaseHelper.orderDetailId, model.getOrderDetailId());
        contentValues.put(DatabaseHelper.comment, model.getComment());
//        contentValues.put(DatabaseHelper.deliveryDate, "");
//        contentValues.put(DatabaseHelper.latitude, "");
//        contentValues.put(DatabaseHelper.longitude, "");
//        contentValues.put(DatabaseHelper.orderValueExc, "");
//        contentValues.put(DatabaseHelper.orderValueInc, "");
//        contentValues.put(DatabaseHelper.deliveryAddress, "");
//        contentValues.put(DatabaseHelper.user, "");
//        contentValues.put(DatabaseHelper.orderMass, "");
        contentValues.put(DatabaseHelper.offLoadComment, model.getoffLoadComment());
        contentValues.put(DatabaseHelper.returnQty, model.getreturnQty());
        contentValues.put(DatabaseHelper.Uploaded, 1);


        contentValues.put(DatabaseHelper.blnoffloaded, model.getblnoffloaded());
        contentValues.put(DatabaseHelper.strCustomerReason, model.getstrCustomerReason());

        //contentValues.put(DatabaseHelper.intWareHouseId, "");
        contentValues.put(DatabaseHelper.blnTruckchecked, "" + model.getblnTruckchecked());
        contentValues.put(DatabaseHelper.wareHouseName, model.getstrWareHouse());


        long id = database.insert(DatabaseHelper.ORDERS_LINES_TABLE_NAME, null, contentValues);
        return id;
    }


    /**
     * READ Database
     */

    //get Personalized IP saved on local storage:
    public List<IpModel> getServerIpModel() {

        List<IpModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.serverIP, DatabaseHelper.emailId,
                DatabaseHelper.companyName, DatabaseHelper.deviceId, DatabaseHelper.regKey
        };
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.SERVER_IP_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            IpModel model = new IpModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            list.add(model);
        }
        return list;
    }

    //get WareHouses:
    public List<WareHousesModel> getWareHouses() {

        List<WareHousesModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.WareHouseId, DatabaseHelper.WareHouse};
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.WAREHOUSE_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WareHousesModel model = new WareHousesModel(
                    cursor.getString(1),
                    cursor.getString(2)
            );
            list.add(model);
        }
        return list;
    }


    //get Routes saved on local storage:
    public List<RouteModel> getRoutes() {

        List<RouteModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.ROUTE_ID, DatabaseHelper.ROUTE_NAME};
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.ROUTE_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            RouteModel model = new RouteModel(
                    cursor.getInt(1),
                    cursor.getString(2)
            );
            list.add(model);
        }
        return list;
    }

    //get Order Types saved on local storage:
    public List<OrderTypeModel> getOrderTypes() {

        List<OrderTypeModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.ORDER_TYPE_ID, DatabaseHelper.ORDER_TYPE};
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.ORDER_TYPE_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            OrderTypeModel model = new OrderTypeModel(
                    cursor.getInt(1),
                    cursor.getString(2)
            );
            list.add(model);
        }
        return list;
    }

    //get Orders:
    public List<OrderModel> getOrders() {

        List<OrderModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID,
                DatabaseHelper.orderId,
                DatabaseHelper.invoiceNo,
                DatabaseHelper.customerPastelCode,
                DatabaseHelper.storeName,
                DatabaseHelper.deliveryDate,
                DatabaseHelper.latitude,
                DatabaseHelper.longitude,
                DatabaseHelper.orderValueExc,
                DatabaseHelper.orderValueInc,
                DatabaseHelper.deliveryAddress,
                DatabaseHelper.user,
                DatabaseHelper.orderMass,
                DatabaseHelper.uploaded,
                DatabaseHelper.cashPaid,
                DatabaseHelper.offloaded,
                DatabaseHelper.strEmailCustomer,
                DatabaseHelper.strCashsignature,
                DatabaseHelper.invTotIncl,
                DatabaseHelper.startTripTime,
                DatabaseHelper.endTripTime,
                DatabaseHelper.deliverySeq,
                DatabaseHelper.strCoordinateStart,
                DatabaseHelper.driverName,
                DatabaseHelper.driverEmail,
                DatabaseHelper.driverPassword,
                DatabaseHelper.strCustomerSignedBy,
                DatabaseHelper.threshold,
        };
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.ORDERS_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            OrderModel model = new OrderModel(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getInt(13),
                    cursor.getString(14),
                    cursor.getInt(15),
                    cursor.getString(16),
                    cursor.getString(17),
                    cursor.getString(18),
                    cursor.getString(19),
                    cursor.getString(20),
                    cursor.getInt(21),
                    cursor.getString(22),
                    cursor.getString(23),
                    cursor.getString(24),
                    cursor.getString(25),
                    cursor.getString(26),
                    cursor.getString(27)
            );
            list.add(model);
        }
        return list;
    }

    //get Orders By Invoice:
    public List<OrderModel> getOrdersByInvoice(String invoice) {

        List<OrderModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID,
                DatabaseHelper.orderId,
                DatabaseHelper.invoiceNo,
                DatabaseHelper.customerPastelCode,
                DatabaseHelper.storeName,
                DatabaseHelper.deliveryDate,
                DatabaseHelper.latitude,
                DatabaseHelper.longitude,
                DatabaseHelper.orderValueExc,
                DatabaseHelper.orderValueInc,
                DatabaseHelper.deliveryAddress,
                DatabaseHelper.user,
                DatabaseHelper.orderMass,
                DatabaseHelper.uploaded,
                DatabaseHelper.cashPaid,
                DatabaseHelper.offloaded,
                DatabaseHelper.strEmailCustomer,
                DatabaseHelper.strCashsignature,
                DatabaseHelper.invTotIncl,
                DatabaseHelper.startTripTime,
                DatabaseHelper.endTripTime,
                DatabaseHelper.deliverySeq,
                DatabaseHelper.strCoordinateStart,
                DatabaseHelper.driverName,
                DatabaseHelper.driverEmail,
                DatabaseHelper.driverPassword,
                DatabaseHelper.strCustomerSignedBy,
                DatabaseHelper.threshold,
        };

        String selection = DatabaseHelper.invoiceNo + "=?";

        String[] args = {"" + invoice};
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.ORDERS_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            OrderModel model = new OrderModel(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getInt(13),
                    cursor.getString(14),
                    cursor.getInt(15),
                    cursor.getString(16),
                    cursor.getString(17),
                    cursor.getString(18),
                    cursor.getString(19),
                    cursor.getString(20),
                    cursor.getInt(21),
                    cursor.getString(22),
                    cursor.getString(23),
                    cursor.getString(24),
                    cursor.getString(25),
                    cursor.getString(26),
                    cursor.getString(27)
            );
            list.add(model);
        }
        return list;
    }

    public int checkIfLinesUploaded() {
        //X/Y
        SQLiteDatabase db = helper.getWritableDatabase();
        int returnNo = 0;
        int getString = 0;
        int countHeaders = 0;
        try {
            Cursor cursor = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_LINES_TABLE_NAME + "' where Uploaded=0 ", null);
            Cursor cursorHeader = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_TABLE_NAME + "' where uploaded=0 ", null);

            String status = "";

            //cursor.moveToFirst();
            // cursorUploaded.moveToFirst();
            getString = cursor.getCount();
            countHeaders = cursorHeader.getCount();

            if (getString > 0) {
                returnNo = 0;
            } else {
                if (countHeaders > 0) {
                    returnNo = 1;
                } else {
                    returnNo = 0;
                }
            }
        } catch (Exception e) {

        }
        return returnNo;
    }

//    public boolean hasData() {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_LINES_TABLE_NAME + "' ", null);
//
//        boolean isDataSave = false;
//        if (cursor.moveToFirst()) {
//            isDataSave = true;
//            while (cursor.moveToNext()) {
//                isDataSave = true;   //Not too sure why is doing this
//            }
//        } else {
//            isDataSave = false;
//        }
//        return isDataSave;
//    }

//    public String countSigned() {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor notsigned = db.rawQuery("Select Count(*) from OrderHeaders ", null);
//        //Cursor signed = db.rawQuery("Select Count(*) from OrderHeaders where   imagestring IS NOT NULL AND imagestring != '' ", null);
//        Cursor signed = db.rawQuery("Select Count(*) from OrderHeaders where   strCashsignature IS NOT NULL AND strCashsignature != '' ", null);
//
//        int intSignedCount = 0;
//        int intNotSignedCount = 0;
//        notsigned.moveToFirst();
//        signed.moveToFirst();
//        intNotSignedCount = notsigned.getInt(0);
//        intSignedCount = signed.getInt(0);
//
//        String xofy = Integer.toString(intSignedCount) + "/" + Integer.toString(intNotSignedCount);
//        return xofy;
//    }

    //Get Order lines by INVOICE & WAREHOUSE
    public List<OrderLinesModel> returnOrderLinesOffloadedByCategory(String invoice, String warehouse) {

        List<OrderLinesModel> list = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID,
                DatabaseHelper.orderId,
                DatabaseHelper.invoiceNo,
                DatabaseHelper.customerPastelCode,
                DatabaseHelper.storeName,
                DatabaseHelper.deliveryDate,
                DatabaseHelper.latitude,
                DatabaseHelper.longitude,
                DatabaseHelper.orderValueExc,
                DatabaseHelper.orderValueInc,
                DatabaseHelper.deliveryAddress,
                DatabaseHelper.user,
                DatabaseHelper.orderMass,
                DatabaseHelper.productId,
                DatabaseHelper.qty,
                DatabaseHelper.price,
                DatabaseHelper.pastelDescription,
                DatabaseHelper.pastelCode,
                DatabaseHelper.orderDetailId,
                DatabaseHelper.comment,
                DatabaseHelper.returnQty,
                DatabaseHelper.offLoadComment,
                DatabaseHelper.blnoffloaded,
                DatabaseHelper.strCustomerReason,
                DatabaseHelper.Uploaded,
                DatabaseHelper.intWareHouseId,
                DatabaseHelper.wareHouseName
        };
        String selection = null;
        String[] args;
        if (warehouse.equals("ALL")) {
            selection = DatabaseHelper.invoiceNo + "=?";
            args = new String[]{"" + invoice};
        } else {
            selection = DatabaseHelper.invoiceNo + "=? AND " + DatabaseHelper.wareHouseName + "=?";
            args = new String[]{"" + invoice, "" + warehouse};
        }
        //String selection = DatabaseHelper.invoiceNo + "=?";

        //String[] args = {"" + invoice, "" + warehouse};
        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.ORDERS_LINES_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            OrderLinesModel model = new OrderLinesModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getDouble(8),
                    cursor.getDouble(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getInt(12),
                    cursor.getInt(13),
                    cursor.getInt(14),
                    cursor.getInt(15),
                    cursor.getString(16),
                    cursor.getString(17),
                    cursor.getInt(18),
                    cursor.getString(19),
                    cursor.getString(20),
                    cursor.getString(21),
                    cursor.getInt(22),
                    cursor.getString(23),
                    cursor.getInt(24),// Extra for uploaded
                    cursor.getString(25),
                    cursor.getString(26)
            );
            list.add(model);
        }
        return list;
    }

//    public List<OrderModel> returnOrderHeaders() {
//        List<OrderModel> labels = new ArrayList();
//
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_TABLE_NAME
//                + "' order by deliverySeq,storeName", null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                OrderModel model = new OrderModel(
//                        cursor.getInt(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getString(5),
//                        cursor.getInt(6),
//                        cursor.getInt(7),
//                        cursor.getString(8),
//                        cursor.getString(9),
//                        cursor.getString(10),
//                        cursor.getString(11),
//                        cursor.getString(12),
//                        cursor.getInt(13),
//                        cursor.getString(14),
//                        cursor.getInt(15),
//                        cursor.getString(16),
//                        cursor.getString(17),
//                        cursor.getString(18),
//                        cursor.getString(19),
//                        cursor.getString(20),
//                        cursor.getInt(21),
//                        cursor.getString(22),
//                        cursor.getString(23),
//                        cursor.getString(24),
//                        cursor.getString(25),
//                        cursor.getString(26),
//                        cursor.getString(27)
//                );
//                labels.add(model);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return labels;
//    }

    @SuppressLint("Range")
    public int returnOrderLinesCrateCount(String InvoiceNo) {

        int cratesCount = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select qty from '" + DatabaseHelper.ORDERS_LINES_TABLE_NAME + "' where orderId ='" + InvoiceNo + "'", null);

        if (cursor.moveToFirst()) {
            do {
                cratesCount = cratesCount + Integer.parseInt(cursor.getString(cursor.getColumnIndex("qty")));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cratesCount;
    }

//    @SuppressLint("Range")
//    public List<OrderLinesModel> returnOrderLines(String InvoiceNo) {
//
//        List<OrderLinesModel> listOfOrdersLines = new ArrayList<>();
//        int cratesCount = 0;
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_LINES_TABLE_NAME + "' where orderId ='" + InvoiceNo + "'", null);
//
//        while (cursor.moveToNext()) {
//            OrderLinesModel model = new OrderLinesModel(
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(3),
//                    cursor.getString(4),
//                    cursor.getString(5),
//                    cursor.getInt(6),
//                    cursor.getInt(7),
//                    cursor.getDouble(8),
//                    cursor.getDouble(9),
//                    cursor.getString(10),
//                    cursor.getString(11),
//                    cursor.getInt(12),
//                    cursor.getInt(13),
//                    cursor.getInt(14),
//                    cursor.getInt(15),
//                    cursor.getString(16),
//                    cursor.getString(17),
//                    cursor.getInt(18),
//                    cursor.getString(19),
//                    cursor.getString(20),
//                    cursor.getString(21),
//                    cursor.getInt(22),
//                    cursor.getString(23),
//                    cursor.getInt(24), //Extra for uploaded
//                    cursor.getString(25),
//                    cursor.getString(26)
//            );
//            listOfOrdersLines.add(model);
//        }
//
//        return listOfOrdersLines;
//    }

    public String OrdersNotPostedToTheOffice() {
        //X/Y
        SQLiteDatabase db = helper.getWritableDatabase();
        int getString = 0;
        int totalOrders = 0;
        try {
            Cursor cursor = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_TABLE_NAME
                    + "' where uploaded=0 ", null);
            Cursor cursorUploaded = db.rawQuery("Select * from '" + DatabaseHelper.ORDERS_TABLE_NAME
                    + "' where uploaded=1 ", null);
            getString = cursor.getCount();
            totalOrders = cursorUploaded.getCount();
        } catch (Exception e) {

        }
        return getString + "/" + totalOrders + " NOT UPLOADED";
    }

    /**
     * UPDATE Database
     */

    // Removing the Item from Right List to the Left List:
    public long listSwappingUpdate(String orderDetailedId, int offloaded) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.orderDetailId + " LIKE ? ";
        String[] args = {"" + orderDetailedId};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.blnoffloaded, offloaded);
        contentValues.put(DatabaseHelper.Uploaded, 0);


        long ids = database.update(DatabaseHelper.ORDERS_LINES_TABLE_NAME, contentValues, selection, args);

        return ids;
    }

    public long updateOrdersHeaderByInvoice(String invoice) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.invoiceNo + " LIKE ? ";
        String[] args = {"" + invoice};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.offloaded, 1);
        contentValues.put(DatabaseHelper.uploaded, 1);


        long ids = database.update(DatabaseHelper.ORDERS_TABLE_NAME, contentValues, selection, args);

        return ids;
    }

    public long updateOrdersHeaderDeliverySequenceByInvoice(int sequence, String invoice) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.invoiceNo + " LIKE ? ";
        String[] args = {"" + invoice};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.deliverySeq, sequence);

        long ids = database.update(DatabaseHelper.ORDERS_TABLE_NAME, contentValues, selection, args);

        return ids;
    }


    /**
     * DELETE Database
     */

    //get Reference
//    public List<RefModel> getRefById(int userID) {
//
//        refList.clear();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        //select * from tableName where name = ? and customerName = ?:
//        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
//        String selection = DatabaseHelper.userId + "=?";
//
//        String[] args = {""+userID};
//        String[] columns = {DatabaseHelper.UID, DatabaseHelper.intAutoPickingHeader,DatabaseHelper.userId,
//                DatabaseHelper.strUnickReference,DatabaseHelper.strPickingNickname
//        };
//
//        // Cursor cursor = database.query(DatabaseHelper.PLAN_TABLE_NAME, columns, selection, args, null, null, null);
//        Cursor cursor = database.query(DatabaseHelper.REFERENCE_TABLE_NAME, columns, selection, args, null, null, null);
//        while (cursor.moveToNext()) {
//            RefModel model = new RefModel(
//                    cursor.getInt(1),
//                    cursor.getString(3),
//                    cursor.getString(4)
//            );
//            refList.add(model);
//        }
//        return refList;
//    }


    /**
     * Dropping the table
     */
    //Drop IP Table:
    public void dropIPTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_SERVER_IP_TABLE);
        database.execSQL(DatabaseHelper.CREATE_SERVER_IP_TABLE);
    }

    //Drop ROUTE Table:
    public void dropRouteTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_ROUTE_TABLE);
        database.execSQL(DatabaseHelper.CREATE_ROUTE_TABLE);
    }

    //Drop ORDER TYPE Table:
    public void dropOrderTypeTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_ORDER_TYPE_TABLE);
        database.execSQL(DatabaseHelper.CREATE_ORDER_TYPE_TABLE);
    }

    //Drop ORDER Table:
    public void dropOrderTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_ORDERS_TABLE);
        database.execSQL(DatabaseHelper.CREATE_ORDERS_TABLE);
    }

    //Drop ORDER TYPE Table:
    public void dropOrderLinesTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_ORDERS_LINES_TABLE);
        database.execSQL(DatabaseHelper.CREATE_ORDERS_LINES_TABLE);
    }

    //Update Flag by Line No.
    // Update Quantity by name and reference code:
//    public long updatePlanByLine(int lineNo, int flag) {
//        SQLiteDatabase database = helper.getWritableDatabase();
//        String selection = DatabaseHelper.LineNos + " LIKE ? ";
//        String[] args = {"" + lineNo};
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.FLAG, flag);
//
//
//        long ids = database.update(DatabaseHelper.PLAN_TABLE_NAME, contentValues, selection, args);
//
//        return ids;
//    }

    /**
     * Older Code:
     */

    public void updateDeals(String update) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(update);
    }
   /* public void getVersionDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }*/


    public ArrayList<Orders> getOrderHeaders() {
        ArrayList<Orders> header = new ArrayList();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From OrderHeaders ", null);

        // parse all results
        Orders orderAttributes = null;
        if (cursor.moveToFirst()) {
            do {
                orderAttributes = new Orders();
                orderAttributes.setOrderId(cursor.getString(1));
                orderAttributes.setInvoiceNo(cursor.getString(0));
                orderAttributes.setCustomerPastelCode(cursor.getString(2));
                orderAttributes.setStoreName(cursor.getString(3));
                orderAttributes.setDeliveryDate(cursor.getString(4));
                orderAttributes.setDeliverySequence(cursor.getString(5));
                orderAttributes.setLatitude(cursor.getString(6));
                orderAttributes.setLongitude(cursor.getString(7));
                orderAttributes.setOrderValueExc(cursor.getString(8));
                orderAttributes.setOrderValueInc(cursor.getString(9));
                orderAttributes.setUser(cursor.getString(10));

                header.add(orderAttributes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return header;
    }

    public ArrayList<OtherAttributes> getPrinterInfo() {
        ArrayList<OtherAttributes> header = new ArrayList();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From PrinterInfo ", null);

        // parse all results
        OtherAttributes orderAttributes = null;
        if (cursor.moveToFirst()) {
            do {
                orderAttributes = new OtherAttributes();
                orderAttributes.setProtoType(cursor.getString(1));
                orderAttributes.setprintermodelLogicalName(cursor.getString(2));
                orderAttributes.setaddress(cursor.getString(3));

                header.add(orderAttributes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return header;
    }

    public ArrayList<Orders> getOrderHeadersNotUploaded() {
        ArrayList<Orders> header = new ArrayList();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From OrderHeaders where Uploaded = 0 and offloaded = 1 and (select count(Uploaded) from OrderLines where Uploaded = 0) = 0", null);

        // parse all results
        Orders orderAttributes = null;
        if (cursor.moveToFirst()) {
            do {
                orderAttributes = new Orders();
                orderAttributes.setOrderId(cursor.getString(2));
                orderAttributes.setInvoiceNo(cursor.getString(5));
                orderAttributes.setCustomerPastelCode(cursor.getString(3));
                orderAttributes.setStoreName(cursor.getString(1));
                orderAttributes.setDeliveryDate(cursor.getString(4));

                orderAttributes.setLatitude(cursor.getString(7));
                orderAttributes.setLongitude(cursor.getString(8));
                orderAttributes.setOrderValueExc(cursor.getString(9));
                orderAttributes.setOrderValueInc(cursor.getString(10));
                orderAttributes.setUser(cursor.getString(11));
                orderAttributes.setOffloaded(cursor.getString(13));
                orderAttributes.setImagestring(cursor.getString(15));
                orderAttributes.setCashPaid(cursor.getString(16));
                orderAttributes.setstrNotesDrivers(cursor.getString(17));
                orderAttributes.setstrEmailCustomer(cursor.getString(18));
                orderAttributes.setstrCashsignature(cursor.getString(19));
                orderAttributes.setStartTripTime(cursor.getString(21));
                orderAttributes.setEndTripTime(cursor.getString(22));
                orderAttributes.setDeliverySequence(cursor.getString(23));
                orderAttributes.setstrCoordinateStart(cursor.getString(24));
                orderAttributes.setstrCustomerSignedBy(cursor.getString(cursor.getColumnIndex("strCustomerSignedBy")));
                orderAttributes.setLoyalty(cursor.getString(cursor.getColumnIndex("Loyalty")));

                header.add(orderAttributes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return header;
    }

    public int returnLoyaltyPoints(String invoiceNo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT * from OrderHeaders where InvoiceNo ='" + invoiceNo + "' and Loyalty !=0 limit 1", null);


        return cursor2.getCount();
    }

    public ArrayList<OrderLines> getOrderLines(int OrderId) {
        ArrayList<OrderLines> lines = new ArrayList();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From OrderLines where OrderId = " + OrderId, null);

        // parse all results
        OrderLines orderAttributes = null;
        if (cursor.moveToFirst()) {
            do {
                orderAttributes = new OrderLines();
                orderAttributes.setProductId(cursor.getString(1));
                orderAttributes.setPastelCode(cursor.getString(0));
                orderAttributes.setPastelDescription(cursor.getString(2));
                orderAttributes.setQty(cursor.getString(3));
                orderAttributes.setPrice(cursor.getString(4));

                lines.add(orderAttributes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lines;
    }

    public ArrayList<OrderTypes> getOrderType() {
        ArrayList<OrderTypes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From OrderTypes", null);

        OrderTypes orderbtypes = null;
        if (cursor.moveToFirst()) {
            do {
                orderbtypes = new OrderTypes();
                log.e("type", "***********" + cursor.getString(2));
                orderbtypes.setOrderType(cursor.getString(2));
                labels.add(orderbtypes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<CheckList> getCheckList() {
        ArrayList<CheckList> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From CheckLists", null);

        CheckList getcheck = null;
        if (cursor.moveToFirst()) {
            do {
                getcheck = new CheckList();
                getcheck.setcheckListId(cursor.getInt(cursor.getColumnIndex("checkListId")));
                getcheck.setcheckListMessage(cursor.getString(cursor.getColumnIndex("checkListMessage")));
                labels.add(getcheck);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

//    public ArrayList<Routes> getRoutes() {
//        ArrayList<Routes> labels = new ArrayList();
//
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * From Routes", null);
//
//        Routes routes = null;
//        if (cursor.moveToFirst()) {
//            do {
//                routes = new Routes();
//
//                routes.setRouteName(cursor.getString(2));
//                labels.add(routes);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return labels;
//    }

    public ArrayList<Routes> multiId(String SQL) {
        ArrayList<Routes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);

        Routes routes = null;
        if (cursor.moveToFirst()) {
            do {
                routes = new Routes();

                routes.setRouteName(cursor.getString(1));
                labels.add(routes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> returnLastStop() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Latitude,Longitude from OrderHeaders order by id desc ", null);

        Orders coord = null;
        if (cursor.moveToFirst()) {
            do {
                coord = new Orders();

                coord.setLatitude(cursor.getString(0));
                coord.setLongitude(cursor.getString(1));
                labels.add(coord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> returnFilters() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Filters ", null);

        OtherAttributes filt = null;
        if (cursor.moveToFirst()) {
            do {
                filt = new OtherAttributes();

                filt.setroute(cursor.getString(1));
                filt.setordertype(cursor.getString(2));
                filt.setdeliverydate(cursor.getString(3));
                labels.add(filt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> returnExpenses() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from tblExpenses ", null);

        OtherAttributes filt = null;
        if (cursor.moveToFirst()) {
            do {
                filt = new OtherAttributes();

                filt.setExpenseId(cursor.getInt(cursor.getColumnIndex("ExpenseId")));
                filt.setExpenseName(cursor.getString(cursor.getColumnIndex("ExpenseName")));
                filt.setGlCode(cursor.getString(cursor.getColumnIndex("GlCode")));

                labels.add(filt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnExtraProducts() {

        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from ExtraProducts ", null);

        OrderLines filt = null;
        if (cursor.moveToFirst()) {
            do {
                filt = new OrderLines();

                filt.setProductId(cursor.getString(cursor.getColumnIndex("ProductId")));
                filt.setPastelCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                filt.setPastelDescription(cursor.getString(cursor.getColumnIndex("ProductName")));

                labels.add(filt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;

    }

    public ArrayList<OrderLines> returnExtraProductsToPost() {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from ExtraProductsToPost where Uploaded = 0 ", null);

        OrderLines filt = null;
        if (cursor.moveToFirst()) {
            do {
                filt = new OrderLines();

                filt.setProductId(cursor.getString(cursor.getColumnIndex("ProductId")));
                filt.setOrderId(cursor.getString(cursor.getColumnIndex("InvoiceNo")));//InvoiceNo
                filt.setQty(cursor.getString(cursor.getColumnIndex("Qty")));
                filt.setId(cursor.getInt(cursor.getColumnIndex("id")));

                labels.add(filt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> returnSavedExpenses() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from postExpenses ", null);

        OtherAttributes filt = null;
        if (cursor.moveToFirst()) {
            do {
                filt = new OtherAttributes();

                filt.setExpenseId(cursor.getInt(cursor.getColumnIndex("ExpenseId")));
                filt.setExpenseName(cursor.getString(cursor.getColumnIndex("ExpenseName")));
                filt.setId(cursor.getInt(cursor.getColumnIndex("id")));


                labels.add(filt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public List<Orders> returnOrderHeaders() {
        List<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHeaders order by DeliverySeq,StoreName", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new Orders();
                orders.setStoreName(cursor.getString(1));
                orders.setOrderId(cursor.getString(2));
                orders.setCustomerPastelCode(cursor.getString(3));
                orders.setDeliveryDate(cursor.getString(4));
                orders.setInvoiceNo(cursor.getString(5));
                orders.setDeliveryAddress(cursor.getString(6));
                orders.setLatitude(cursor.getString(7));
                orders.setLongitude(cursor.getString(8));
                orders.setUser(cursor.getString(11));
                orders.setOrderMass(cursor.getString(12));
                orders.setOffloaded(cursor.getString(13));
                orders.setCashPaid(cursor.getString(16));
                orders.setstrEmailCustomer(cursor.getString(18));
                orders.setDeliverySeq(cursor.getInt(23));
                orders.setDriverName(cursor.getString(25));
                orders.setDriverEmail(cursor.getString(26));
                orders.setDriverPassword(cursor.getString(27));
                orders.setPaymentType(cursor.getString(cursor.getColumnIndex("PaymentType")));
                orders.setThreshold(cursor.getString(cursor.getColumnIndex("threshold")));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> returnOrderHeadersNotUploaded() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHeaders where Uploaded !=1 order by DeliverySeq,StoreName", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new Orders();
                orders.setStoreName(cursor.getString(1));
                orders.setOrderId(cursor.getString(2));
                orders.setCustomerPastelCode(cursor.getString(3));
                orders.setDeliveryDate(cursor.getString(4));
                orders.setInvoiceNo(cursor.getString(5));
                orders.setDeliveryAddress(cursor.getString(6));
                orders.setLatitude(cursor.getString(7));
                orders.setLongitude(cursor.getString(8));
                orders.setUser(cursor.getString(11));
                orders.setOrderMass(cursor.getString(12));
                orders.setOffloaded(cursor.getString(13));
                orders.setCashPaid(cursor.getString(16));
                orders.setstrEmailCustomer(cursor.getString(18));
                orders.setDeliverySeq(cursor.getInt(23));
                orders.setDriverName(cursor.getString(25));
                orders.setDriverEmail(cursor.getString(26));
                orders.setDriverPassword(cursor.getString(27));
                orders.setPaymentType(cursor.getString(cursor.getColumnIndex("PaymentType")));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> returnOrderHeadersNotUploadedByOrderIds(String Orderids) {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHeaders where OrderID IN (Orderids) order by DeliverySeq,StoreName", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new Orders();
                orders.setStoreName(cursor.getString(1));
                orders.setOrderId(cursor.getString(2));
                orders.setCustomerPastelCode(cursor.getString(3));
                orders.setDeliveryDate(cursor.getString(4));
                orders.setInvoiceNo(cursor.getString(5));
                orders.setDeliveryAddress(cursor.getString(6));
                orders.setLatitude(cursor.getString(7));
                orders.setLongitude(cursor.getString(8));
                orders.setUser(cursor.getString(11));
                orders.setOrderMass(cursor.getString(12));
                orders.setOffloaded(cursor.getString(13));
                orders.setCashPaid(cursor.getString(16));
                orders.setstrEmailCustomer(cursor.getString(18));
                orders.setDeliverySeq(cursor.getInt(23));
                orders.setDriverName(cursor.getString(25));
                orders.setDriverEmail(cursor.getString(26));
                orders.setDriverPassword(cursor.getString(27));
                orders.setPaymentType(cursor.getString(cursor.getColumnIndex("PaymentType")));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<SettingsModel> getSettings() {
        ArrayList<SettingsModel> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From tblSettings", null);

        SettingsModel routes = null;
        if (cursor.moveToFirst()) {
            do {
                routes = new SettingsModel();
                routes.setstrServerIp(cursor.getString(1));
                routes.setregKey(cursor.getString(2));
                routes.setCompany(cursor.getString(3));
                routes.setDeviceID(cursor.getString(4));
                routes.setEmail(cursor.getString(5));
                labels.add(routes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public int countOffloaded() {

        int offloaded = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(OrderId) X From OrderHeaders where offloaded = 0", null);

        if (cursor.moveToFirst()) {
            do {
                offloaded = cursor.getInt(cursor.getColumnIndex("X"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("countOffloaded", "++++++++++++++++++++++++++++++++++++" + offloaded);
        return offloaded;
    }

    public int countFilters() {

        int offloaded = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) X From Filters ", null);

        if (cursor.moveToFirst()) {
            do {
                offloaded = cursor.getInt(cursor.getColumnIndex("X"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("countOffloaded", "++++++++++++++++++++++++++++++++++++" + offloaded);
        return offloaded;
    }

    public int countCompletedTips() {

        int offloaded = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(Completed) X From TripHeader where Completed = 1", null);

        if (cursor.moveToFirst()) {
            do {
                offloaded = cursor.getInt(cursor.getColumnIndex("X"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("countOffloaded", "++++++++++++++++++++++++++++++++++++" + offloaded);
        return offloaded;
    }

    public int getThings(String rulename) {
        int result = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From tblGroupsetup where OptionDesc='" + rulename + "'", null);

        if (cursor.moveToFirst()) {
            do {
                result = 1;

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;

    }

    public int checkIfUserLoggedIn() {
        int resultloggedin = 0;

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From tblSettings where  regKey IS NOT NULL AND TRIM(regKey,'') != ''", null);

        if (cursor.moveToFirst()) {
            do {
                resultloggedin = 1;

            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultloggedin;
    }

    public ArrayList<Orders> returnOrderHeadersInfoByInvoice(String invoice) {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHeaders where InvoiceNo='" + invoice + "' ", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new Orders();
                orders.setStoreName(cursor.getString(1));
                orders.setOrderId(cursor.getString(2));
                orders.setCustomerPastelCode(cursor.getString(3));
                orders.setDeliveryDate(cursor.getString(4));
                orders.setInvoiceNo(cursor.getString(5));
                orders.setDeliveryAddress(cursor.getString(6));
                orders.setLatitude(cursor.getString(7));
                orders.setLongitude(cursor.getString(8));
                orders.setUser(cursor.getString(11));
                orders.setOrderMass(cursor.getString(12));
                orders.setOffloaded(cursor.getString(13));
                orders.setCashPaid(cursor.getString(16));
                orders.setstrEmailCustomer(cursor.getString(18));
                orders.setInvTotIncl(cursor.getString(20));
                orders.setPaymentType(cursor.getString(cursor.getColumnIndex("PaymentType")));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLines(String InvoiceNo) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where orderId ='" + InvoiceNo + "'", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }
//
//    public int returnOrderLinesCrateCount(String InvoiceNo) {
//
//        int cratesCount = 0;
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select Qty from OrderLines where OrderId ='" + InvoiceNo + "'", null);
//
//        OrderLines orders = null;
//        if (cursor.moveToFirst()) {
//            do {
//                cratesCount = cratesCount + Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty")));
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return cratesCount;
//    }

    public ArrayList<OrderLines> returnCreditRequisition(String InvoiceNo) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "' and returnQty > 0 ", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));

                orders.setblnoffloaded(cursor.getString(12));
                orders.setstrCustomerReason(cursor.getString(13));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesoffloaded(String InvoiceNo) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where orderId ='" + InvoiceNo + "'", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));

                Log.e("******", "***************************************************linesssssssssssssssss" + cursor.getString(4));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesoffloadedByCategory(String InvoiceNo, String warehouse) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        if (warehouse.equals("ALL")) {
            //cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "'", null);
            cursor = db.rawQuery("Select * from OrderLines where orderId ='" + InvoiceNo + "'", null);
        } else {
            //cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "' and WareHouseName='" + warehouse + "'", null);
            cursor = db.rawQuery("Select * from OrderLines where orderId ='" + InvoiceNo + "' and WareHouseName='" + warehouse + "'", null);
        }


        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));

                // Log.e("******","***************************************************linesssssssssssssssss"+cursor.getString(4));
                Log.e("******", "***************************************************linesssssssssssssssss" + cursor.getString(12));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesoffloadedByCategoryNotChecked(String InvoiceNo, String warehouse) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        if (warehouse.equals("ALL")) {
            cursor = db.rawQuery("Select * from OrderLines where blnoffloaded=0 and  OrderId ='" + InvoiceNo + "'", null);
        } else {
            cursor = db.rawQuery("Select * from OrderLines where blnoffloaded=0 and OrderId ='" + InvoiceNo + "' and WareHouseName='" + warehouse + "'", null);
        }


        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));

                // Log.e("******","***************************************************linesssssssssssssssss"+cursor.getString(4));
                Log.e("******", "***************************************************linesssssssssssssssss" + cursor.getString(12));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesDetailID(String ODID) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where OrderDetailId ='" + ODID + "'", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesInfo(String orderdetailId) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where OrderDetailId ='" + orderdetailId + "'", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));
                orders.setstrCustomerReason(cursor.getString(13));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesInfoUploaded() {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where Uploaded=0 order by id desc limit 30  ", null);//limit was 7

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));
                orders.setstrCustomerReason(cursor.getString(13));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> returnOrderLinesInfoUploadedByInvoice(String invoice) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where Uploaded=0 and OrderID= '" + invoice + "'", null);//limit was 7

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(3));
                orders.setPastelDescription(cursor.getString(4));
                orders.setQty(cursor.getString(5));
                orders.setPrice(cursor.getString(6));
                orders.setOrderDetailId(cursor.getString(7));
                orders.setComment(cursor.getString(8));
                orders.setoffLoadComment(cursor.getString(9));
                orders.setreturnQty(cursor.getString(10));
                orders.setblnoffloaded(cursor.getString(12));
                orders.setstrCustomerReason(cursor.getString(13));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> managementConsole() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from ManagementConsole limit 1 ", null);

        OtherAttributes orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OtherAttributes();
                orders.setId(cursor.getInt(0));
                orders.setconMess(cursor.getString(1));
                orders.setconDocId(cursor.getString(2));
                orders.setcondteTm(cursor.getString(3));


                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> thereIsData() {
        ArrayList<OtherAttributes> labels = new ArrayList();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Filters", null);


        OtherAttributes orders = null;
        if (cursor.moveToFirst()) {
            do {
                orders = new OtherAttributes();
                orders.setdeliverydate(cursor.getString(3));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public boolean hasData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines ", null);

        boolean isDataSave = false;
        if (cursor.moveToFirst()) {
            isDataSave = true;
            while (cursor.moveToNext()) {
                isDataSave = true;   //Not too sure why is doing this
            }
        } else {
            isDataSave = false;
        }
        return isDataSave;
    }

    public boolean isUploaded() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select Count(*) from OrderHeaders where Uploaded=0", null);

        boolean isDataSave = false;
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        Log.e("icount", "***********************" + icount);
        if (icount > 0) {
            isDataSave = true;
        } else {
            isDataSave = false;
        }
        return isDataSave;
    }

    public String countSigned() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor notsigned = db.rawQuery("Select Count(*) from OrderHeaders ", null);
        Cursor signed = db.rawQuery("Select Count(*) from OrderHeaders where   imagestring IS NOT NULL AND imagestring != '' ", null);

        int intSignedCount = 0;
        int intNotSignedCount = 0;
        notsigned.moveToFirst();
        signed.moveToFirst();
        intNotSignedCount = notsigned.getInt(0);
        intSignedCount = signed.getInt(0);
        Log.e("icount", "***********************" + intNotSignedCount);
        Log.e("icount", "***********************" + intSignedCount);

        String xofy = Integer.toString(intSignedCount) + "/" + Integer.toString(intNotSignedCount);
        return xofy;
    }

    public boolean isoffladedline(String invoice) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where blnoffloaded=0 and orderId='" + invoice + "'", null);

        Log.e("try offlo", "Select Count(*) from OrderLines where blnoffloaded=0 and OrderID='" + invoice + "'");

        boolean nothingtoreturn = true;
        if (cursor.moveToFirst()) {
            do {
                nothingtoreturn = false;
            } while (cursor.moveToNext());
        } else {
            nothingtoreturn = true;
        }
        return nothingtoreturn;
    }

    public String islineUploaded(String invoice) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String status = "";
        try {
            Cursor cursor = db.rawQuery("Select count(*) from OrderHeaders where Uploaded=1 and OrderID='" + invoice + "'", null);


            int countResult = 0;
            cursor.moveToFirst();
            countResult = cursor.getInt(0);
            Log.e("count", "result*************************************************countResult" + countResult);

            if (countResult > 0) {
                status = "Order Posted to the office";
            } else {
                status = "It will get posted soon ,please carry on with other stops.";
            }
        } catch (Exception e) {

        }
        return status;
    }

    public int NotificationTableHasData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int countResult = 0;
        try {
            Cursor cursor = db.rawQuery("Select count(*) from Notifications where Uploaded=0 ", null);

            String status = "";

            cursor.moveToFirst();
            countResult = cursor.getInt(0);
            Log.e("count", "result*************************************************countResult" + countResult);
        } catch (Exception e) {

        }
        return countResult;
    }

//    public String OrdersNotPostedToTheOffice() {
//        //X/Y
//        SQLiteDatabase db = helper.getWritableDatabase();
//        int getString = 0;
//        int totalOrders = 0;
//        try {
//            Cursor cursor = db.rawQuery("Select * from OrderHeaders where Uploaded=0 ", null);
//            Cursor cursorUploaded = db.rawQuery("Select * from OrderHeaders where Uploaded=1 ", null);
//
//            String status = "";
//
//            //cursor.moveToFirst();
//            // cursorUploaded.moveToFirst();
//            getString = cursor.getCount();
//            totalOrders = cursorUploaded.getCount();
//            Log.e("count", "result*************************************************countResult" + getString);
//        } catch (Exception e) {
//
//        }
//        return getString + "/" + totalOrders + " NOT UPLOADED";
//    }

    public int checkiflinesuploaded() {
        //X/Y
        SQLiteDatabase db = helper.getWritableDatabase();
        int returnNo = 0;
        int getString = 0;
        int countHeaders = 0;
        try {
            Cursor cursor = db.rawQuery("Select * from OrderLines where Uploaded=0 ", null);
            Cursor cursorHeader = db.rawQuery("Select * from OrderHeaders where Uploaded=0 ", null);

            String status = "";

            //cursor.moveToFirst();
            // cursorUploaded.moveToFirst();
            getString = cursor.getCount();
            countHeaders = cursorHeader.getCount();

            if (getString > 0) {
                returnNo = 0;
            } else {
                if (countHeaders > 0) {
                    returnNo = 1;
                } else {
                    returnNo = 0;
                }
            }

            Log.e("count", "result*************************************************countResult" + getString);
        } catch (Exception e) {

        }
        return returnNo;
    }

    public int selectCountNotUploaded() {
        //X/Y
        SQLiteDatabase db = helper.getWritableDatabase();
        int getString = 0;
        int totalOrders = 0;
        try {
            Cursor cursor = db.rawQuery("Select * from OrderHeaders where Uploaded=0 ", null);
            totalOrders = cursor.getCount();
            Log.e("count", "result*************************************************countResult" + getString);
        } catch (Exception e) {

        }
        return totalOrders;
    }

    public ArrayList<Orders> isSaved(String invoiceNo) {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT offloaded,CashPaid from OrderHeaders where InvoiceNo ='" + invoiceNo + "'", null);

        Orders hearderinfo = null;
        if (cursor.moveToFirst()) {
            do {
                hearderinfo = new Orders();

                hearderinfo.setOffloaded(cursor.getString(0));
                hearderinfo.setCashPaid(cursor.getString(1));
                labels.add(hearderinfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> getOrderHeadersPosted() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT OrderID from OrderHeaders", null);

        Orders hearderinfo = null;
        if (cursor.moveToFirst()) {
            do {
                hearderinfo = new Orders();
                hearderinfo.setOrderId(cursor.getString(cursor.getColumnIndex("OrderID")));
                labels.add(hearderinfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> sendANotification() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Notifications where Uploaded = 0 limit 1 ", null);

        OtherAttributes hearderinfo = null;
        if (cursor.moveToFirst()) {
            do {

                hearderinfo = new OtherAttributes();
                hearderinfo.setMessages(cursor.getString(2));
                hearderinfo.setconDocId(cursor.getString(1));

                labels.add(hearderinfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public void InserMessage(String TabletId, String Messages) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TabletId", TabletId);
        values.put("Messages", Messages);

// Inserting Row
        db.insert("Notifications", null, values);
    }

    public void insertDeliveryNoteLines(String ref, String custname, String notes, String qty, String weights, String delDate, String ProdName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //CustomerName,ProductName,Qty,Weights,DeliveryDate,Notes,ReferenceNumber
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", custname);
        contentValues.put("ProductName", ProdName);
        contentValues.put("Qty", qty);
        contentValues.put("Weights", weights);
        contentValues.put("DeliveryDate", delDate);
        contentValues.put("Notes", notes);
        contentValues.put("ReferenceNumber", ref);
        db.insert("tblDeliveryNotesLines", null, contentValues);

    }

    public void insertDeliveryNoteHeaders(String ref, String custname, String delDate, String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //CustomerName,ProductName,Qty,Weights,DeliveryDate,Notes,ReferenceNumber
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", custname);
        contentValues.put("DeliveryDate", delDate);
        contentValues.put("ReferenceNumber", ref);
        contentValues.put("username", username);
        db.insert("tblDeliveryNotesHeader", null, contentValues);

    }

    public void insertCreditNoteLines(String ref, String custname, String notes, String qty, String weights, String delDate, String ProdName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //CustomerName,ProductName,Qty,Weights,DeliveryDate,Notes,ReferenceNumber
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", custname);
        contentValues.put("ProductName", ProdName);
        contentValues.put("Qty", qty);
        contentValues.put("Weights", weights);
        contentValues.put("DeliveryDate", delDate);
        contentValues.put("Notes", notes);
        contentValues.put("ReferenceNumber", ref);
        db.insert("tblCreditNotesLines", null, contentValues);

    }

    public void insertCreditNoteHeaders(String ref, String custname, String delDate, String username, String UniqueString) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //CustomerName,ProductName,Qty,Weights,DeliveryDate,Notes,ReferenceNumber
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", custname);
        contentValues.put("DeliveryDate", delDate);
        contentValues.put("ReferenceNumber", ref);
        contentValues.put("username", username);
        contentValues.put("UniqueString", UniqueString);
        db.insert("tblCreditNotesHeader", null, contentValues);

    }

    public ArrayList<Orders> ReturnCustomerHeader(String invoice) {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderHeaders where InvoiceNo='" + invoice + "'", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {

                orders = new Orders();
                orders.setStoreName(cursor.getString(1));
                orders.setOrderId(cursor.getString(2));
                orders.setCustomerPastelCode(cursor.getString(3));
                orders.setDeliveryAddress(cursor.getString(6));
                orders.setUser(cursor.getString(11));
                orders.setInvTotIncl(cursor.getString(20));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OrderLines> ReturnUnPostedCreditLines() {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from tblCreditNotesLines where Uploaded = 0 and Completed = 1", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {

                orders = new OrderLines();
                orders.setPastelCode(cursor.getString(cursor.getColumnIndex("ProductName")));
                orders.setQty(cursor.getString(cursor.getColumnIndex("Qty")));
                orders.setreturnQty(cursor.getString(cursor.getColumnIndex("Weights")));
                orders.setPastelDescription(cursor.getString(cursor.getColumnIndex("CustomerName")));
                orders.setId(cursor.getInt(cursor.getColumnIndex("id")));
                orders.setComment(cursor.getString(cursor.getColumnIndex("ReferenceNumber")));
                orders.setoffLoadComment(cursor.getString(cursor.getColumnIndex("Notes")));
                orders.setstrCustomerReason(cursor.getString(cursor.getColumnIndex("DeliveryDate")));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> ReturnUnPostedCreditHeaders() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from tblCreditNotesHeader where Uploaded = 0 and Completed = 1", null);

        Orders orders = null;
        if (cursor.moveToFirst()) {
            do {

                orders = new Orders();
                orders.setStoreName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                orders.setCustomerPastelCode(cursor.getString(cursor.getColumnIndex("ReferenceNumber")));
                orders.setImagestring(cursor.getString(cursor.getColumnIndex("Signature")));
                orders.setstrCustomerSignedBy(cursor.getString(cursor.getColumnIndex("SignedBy")));
                orders.setDeliveryDate(cursor.getString(cursor.getColumnIndex("DeliveryDate")));
                orders.setstrEmailCustomer(cursor.getString(cursor.getColumnIndex("strEmail")));
                orders.setDriverName(cursor.getString(cursor.getColumnIndex("Drivername")));
                orders.setId(cursor.getInt(cursor.getColumnIndex("id")));


                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<OtherAttributes> CompleteTrip() {
        ArrayList<OtherAttributes> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from TripHeader where Uploaded = 0 and Completed = 1", null);

        OtherAttributes orders = null;
        if (cursor.moveToFirst()) {
            do {

                orders = new OtherAttributes();
                orders.setroute(cursor.getString(cursor.getColumnIndex("RouteName")));
                orders.setordertype(cursor.getString(cursor.getColumnIndex("OrderType")));
                orders.setdeliverydate(cursor.getString(cursor.getColumnIndex("DelDate")));
                orders.setSealNumber(cursor.getString(cursor.getColumnIndex("SealNumber")));
                orders.setDriverName(cursor.getString(cursor.getColumnIndex("DriverName")));
                orders.setsignature(cursor.getString(cursor.getColumnIndex("DriverCompleteSignature")));
                orders.setkmout(cursor.getString(cursor.getColumnIndex("KmStart")));
                orders.setkmdone(cursor.getString(cursor.getColumnIndex("KmEnd")));
                orders.setId(cursor.getInt(cursor.getColumnIndex("id")));

                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<WareHouses> warehouse() {
        ArrayList<WareHouses> labels = new ArrayList();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from WareHouses", null);

        WareHouses routes = null;
        if (cursor.moveToFirst()) {
            do {
                routes = new WareHouses();

                routes.setWareHouse(cursor.getString(1));
                routes.setWareHouseId(cursor.getInt(2));
                labels.add(routes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }


    class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        private static final String DATABASE_NAME = "drivers_app.db";
        private static final int VERSION_NUMBER = Constant.DB_VERSION;

        private static final String UID = "id";

        /**
         * Server IP table
         */

        private static final String SERVER_IP_TABLE_NAME = "server_ip";
        private static final String serverIP = "serverIP";
        private static final String emailId = "emailId";
        private static final String companyName = "companyName";
        private static final String deviceId = "deviceId";
        private static final String regKey = "regKey";

        //Creating the table:
        private static final String CREATE_SERVER_IP_TABLE = "CREATE TABLE " + SERVER_IP_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + serverIP + " VARCHAR(255),"
                + emailId + " VARCHAR(255),"
                + companyName + " VARCHAR(255),"
                + deviceId + " VARCHAR(255),"
                + regKey + " VARCHAR(255));";
        private static final String DROP_SERVER_IP_TABLE = "DROP TABLE IF EXISTS " + SERVER_IP_TABLE_NAME;


        /**
         * Log-In Data update:
         */

        private static final String LOG_IN_TABLE = "log_in_table";
        private static final String id = "id";
        private static final String tabletRegId = "tabletRegId";
        private static final String driverNames = "driverName";
        private static final String driverEmails = "driverEmail";
        private static final String driverPasswords = "driverPassword";
        private static final String groupId = "groupId";
        private static final String IP = "IP";

        //Creating the table:
        private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOG_IN_TABLE
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + id + " INTEGER,"
                + companyName + " VARCHAR(255),"
                + tabletRegId + " VARCHAR(255),"
                + driverNames + " VARCHAR(255),"
                + driverEmails + " VARCHAR(255),"
                + driverPasswords + " VARCHAR(255),"
                + groupId + " INTEGER,"
                + IP + " VARCHAR(255));";
        private static final String DROP_LOGIN_TABLE = "DROP TABLE IF EXISTS " + LOG_IN_TABLE;


        /**
         * Route Table
         */

        private static final String ROUTE_TABLE_NAME = "Routes";
        private static final String ROUTE_ID = "routeID";
        private static final String ROUTE_NAME = "routeName";

        //Creating the table:
        private static final String CREATE_ROUTE_TABLE = "CREATE TABLE " + ROUTE_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROUTE_ID + " INTEGER,"
                + ROUTE_NAME + " VARCHAR(255));";
        private static final String DROP_ROUTE_TABLE = "DROP TABLE IF EXISTS " + ROUTE_TABLE_NAME;

        /**
         * Order Type Table
         */

        private static final String ORDER_TYPE_TABLE_NAME = "OrderTypes";
        private static final String ORDER_TYPE_ID = "orderTypeId";
        private static final String ORDER_TYPE = "orderType";

        //Creating the table:
        private static final String CREATE_ORDER_TYPE_TABLE = "CREATE TABLE " + ORDER_TYPE_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_TYPE_ID + " INTEGER,"
                + ORDER_TYPE + " VARCHAR(255));";
        private static final String DROP_ORDER_TYPE_TABLE = "DROP TABLE IF EXISTS " + ORDER_TYPE_TABLE_NAME;

        /**
         * Order Table (Order Headers)
         */


        private static final String ORDERS_TABLE_NAME = "OrderHeaders";
        private static final String storeName = "storeName";
        private static final String orderId = "orderId";
        private static final String customerPastelCode = "customerPastelCode";
        private static final String deliveryDate = "deliveryDate";
        private static final String invoiceNo = "invoiceNo";
        private static final String deliveryAddress = "deliveryAddress";
        private static final String latitude = "latitude";
        private static final String longitude = "longitude";
        private static final String orderValueExc = "orderValueExc";
        private static final String orderValueInc = "orderValueInc";
        private static final String user = "user";
        private static final String orderMass = "orderMass";
        private static final String offloaded = "offloaded";
        private static final String uploaded = "uploaded";
        private static final String imagestring = "imagestring"; //Not sure yet
        private static final String cashPaid = "cashPaid";
        private static final String strNotesDrivers = "strNotesDrivers";
        private static final String strEmailCustomer = "strEmailCustomer";
        private static final String strCashsignature = "strCashsignature";
        private static final String invTotIncl = "invTotIncl";
        private static final String startTripTime = "startTripTime";
        private static final String endTripTime = "endTripTime";
        private static final String deliverySeq = "deliverySeq";
        private static final String strCoordinateStart = "strCoordinateStart";
        private static final String driverName = "driverName";
        private static final String driverEmail = "driverEmail";
        private static final String driverPassword = "driverPassword";
        private static final String strCustomerSignedBy = "strCustomerSignedBy";
        private static final String PaymentType = "PaymentType";
        private static final String Loyalty = "Loyalty";
        private static final String threshold = "threshold";
        //Creating the Order Table:

        private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + ORDERS_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + storeName + " VARCHAR(255),"
                + orderId + " INTEGER,"
                + customerPastelCode + " VARCHAR(255),"
                + deliveryDate + " VARCHAR(255),"
                + invoiceNo + " VARCHAR(255),"
                + deliveryAddress + " VARCHAR(255),"
                + latitude + " VARCHAR(255),"
                + longitude + " VARCHAR(255),"
                + orderValueExc + " VARCHAR(255),"
                + orderValueInc + " VARCHAR(255),"
                + user + " VARCHAR(255),"
                + orderMass + " VARCHAR(255),"
                + offloaded + " INTEGER,"
                + uploaded + " INTEGER,"
                + imagestring + " VARCHAR(255),"
                + cashPaid + " VARCHAR(255),"
                + strNotesDrivers + " VARCHAR(255),"
                + strEmailCustomer + " VARCHAR(255),"
                + strCashsignature + " VARCHAR(255),"
                + invTotIncl + " VARCHAR(255),"
                + startTripTime + " VARCHAR(255),"
                + endTripTime + " VARCHAR(255),"
                + deliverySeq + " INTEGER,"
                + strCoordinateStart + " VARCHAR(255),"
                + driverName + " VARCHAR(255),"
                + driverEmail + " VARCHAR(255),"
                + driverPassword + " VARCHAR(255),"
                + strCustomerSignedBy + " VARCHAR(255),"
                + PaymentType + " VARCHAR(255),"
                + Loyalty + " VARCHAR(255),"
                + threshold + " VARCHAR(255));";
        private static final String DROP_ORDERS_TABLE = "DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME;


        /**
         * OrderLines Table
         */
        private static final String ORDERS_LINES_TABLE_NAME = "OrderLines";
        private static final String productId = "productId";
        private static final String qty = "qty";
        private static final String price = "price";
        private static final String pastelDescription = "pastelDescription";
        private static final String pastelCode = "pastelCode";
        private static final String orderDetailId = "orderDetailId";
        private static final String comment = "comment";
        private static final String returnQty = "returnQty";
        private static final String offLoadComment = "offLoadComment";
        private static final String blnoffloaded = "blnoffloaded";
        private static final String strCustomerReason = "strCustomerReason";
        private static final String intWareHouseId = "intWareHouseId";
        private static final String blnTruckchecked = "blnTruckchecked";
        private static final String wareHouseName = "wareHouseName";
        //This is an extra Quantity (Not present in the API orderLines.php) taken by the reference of Older code:
        private static final String Uploaded = "Uploaded";
        //Creating the Order Table:
        private static final String CREATE_ORDERS_LINES_TABLE = "CREATE TABLE " + ORDERS_LINES_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + orderId + " VARCHAR(255),"
                + productId + " VARCHAR(255),"
                + pastelCode + " VARCHAR(255),"
                + pastelDescription + " VARCHAR(255),"
                + qty + " INTEGER,"
                + price + " VARCHAR(255),"
                + orderDetailId + " INTEGER,"
                + comment + " VARCHAR(255),"
                + offLoadComment + " VARCHAR(255),"
                + returnQty + " VARCHAR(255),"
                + Uploaded + " INTEGER,"
                + blnoffloaded + " INTEGER,"
                + strCustomerReason + " VARCHAR(255),"
                + blnTruckchecked + " INTEGER,"


//                + invoiceNo + " VARCHAR(255),"
//                + customerPastelCode + " VARCHAR(255),"
//                + storeName + " VARCHAR(255),"
//                + deliveryDate + " VARCHAR(255),"
//                + latitude + " VARCHAR(255),"
//                + longitude + " VARCHAR(255),"
//                + orderValueExc + " VARCHAR(255),"
//                + orderValueInc + " VARCHAR(255),"
//                + deliveryAddress + " VARCHAR(255),"
//                + user + " VARCHAR(255),"
//                + orderMass + " VARCHAR(255),"
//                + productId + " INTEGER,"
//                + intWareHouseId + " VARCHAR(255),"
                + wareHouseName + " VARCHAR(255));";
        private static final String DROP_ORDERS_LINES_TABLE = "DROP TABLE IF EXISTS " + ORDERS_LINES_TABLE_NAME;


        /**
         * WareHouses Table
         */
        /**
         * Order Type Table
         */

        private static final String WAREHOUSE_TABLE_NAME = "WareHouses";
        private static final String WareHouseId = "WareHouseId";
        private static final String WareHouse = "WareHouse";

        //Creating the table:
        private static final String CREATE_WAREHOUSE_TABLE = "CREATE TABLE " + WAREHOUSE_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WareHouseId + " VARCHAR(255),"
                + WareHouse + " VARCHAR(255));";
        private static final String DROP_WAREHOUSE_TABLE = "DROP TABLE IF EXISTS " + WAREHOUSE_TABLE_NAME;


        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUMBER);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table:
            try {
                db.execSQL(CREATE_SERVER_IP_TABLE);
                db.execSQL(CREATE_ROUTE_TABLE);
                db.execSQL(CREATE_ORDER_TYPE_TABLE);
                db.execSQL(CREATE_ORDERS_TABLE);
                db.execSQL(CREATE_ORDERS_LINES_TABLE);
                db.execSQL(CREATE_WAREHOUSE_TABLE);
                db.execSQL(CREATE_LOGIN_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_SERVER_IP_TABLE);
                db.execSQL(DROP_ROUTE_TABLE);
                db.execSQL(DROP_ORDER_TYPE_TABLE);
                db.execSQL(DROP_ORDERS_TABLE);
                db.execSQL(DROP_ORDERS_LINES_TABLE);
                db.execSQL(DROP_WAREHOUSE_TABLE);
                db.execSQL(DROP_LOGIN_TABLE);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * Older Code:
         */

    }
}