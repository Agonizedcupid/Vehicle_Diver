package com.regin.reginald.vehicleanddrivers.Aariyan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderLinesModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;

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

    //Insert Orders
    public long insertOrders(OrderModel model) {
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
        contentValues.put(DatabaseHelper.uploaded, model.getUploaded());
        contentValues.put(DatabaseHelper.cashPaid, model.getCashPaid());
        contentValues.put(DatabaseHelper.offloaded, model.getOffloaded());
        contentValues.put(DatabaseHelper.strEmailCustomer, model.getStrEmailCustomer());
        contentValues.put(DatabaseHelper.strCashsignature, model.getStrCashsignature());
        contentValues.put(DatabaseHelper.invTotIncl, model.getInvTotIncl());
        contentValues.put(DatabaseHelper.startTripTime, model.getStartTripTime());
        contentValues.put(DatabaseHelper.endTripTime, model.getEndTripTime());
        contentValues.put(DatabaseHelper.deliverySeq, model.getDeliverySeq());
        contentValues.put(DatabaseHelper.strCoordinateStart, model.getStrCoordinateStart());
        contentValues.put(DatabaseHelper.driverName, model.getDriverName());
        contentValues.put(DatabaseHelper.driverEmail, model.getDriverEmail());
        contentValues.put(DatabaseHelper.driverPassword, model.getDriverPassword());
        contentValues.put(DatabaseHelper.strCustomerSignedBy, model.getStrCustomerSignedBy());
        contentValues.put(DatabaseHelper.threshold, model.getThreshold());

        long id = database.insert(DatabaseHelper.ORDERS_TABLE_NAME, null, contentValues);
        return id;
    }

    //Insert OrdersLines
    public long insertOrderLines(OrderLinesModel model) {
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
        contentValues.put(DatabaseHelper.productId, model.getProductId());
        contentValues.put(DatabaseHelper.qty, model.getQty());
        contentValues.put(DatabaseHelper.price, model.getPrice());
        contentValues.put(DatabaseHelper.pastelDescription, model.getPastelDescription());
        contentValues.put(DatabaseHelper.pastelCode, model.getPastelCode());
        contentValues.put(DatabaseHelper.orderDetailId, model.getOrderDetailId());
        contentValues.put(DatabaseHelper.comment, model.getComment());
        contentValues.put(DatabaseHelper.returnQty, model.getReturnQty());
        contentValues.put(DatabaseHelper.offLoadComment, model.getOffLoadComment());
        contentValues.put(DatabaseHelper.blnoffloaded, model.getBlnoffloaded());
        contentValues.put(DatabaseHelper.strCustomerReason, model.getStrCustomerReason());
        contentValues.put(DatabaseHelper.intWareHouseId, model.getIntWareHouseId());
        contentValues.put(DatabaseHelper.wareHouseName, model.getWareHouseName());


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

    /**
     * UPDATE Database
     */

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

    //Drop ORDER TYPE Table:
    public void dropOrderTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_ORDERS_TABLE);
        database.execSQL(DatabaseHelper.CREATE_ORDERS_TABLE);
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


    class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        private static final String DATABASE_NAME = "drivers_app.db";
        private static final int VERSION_NUMBER = 12;

        private static final String UID = "_id";

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
         * Route Table
         */

        private static final String ROUTE_TABLE_NAME = "routs";
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

        private static final String ORDER_TYPE_TABLE_NAME = "orders";
        private static final String ORDER_TYPE_ID = "orderTypeId";
        private static final String ORDER_TYPE = "orderType";

        //Creating the table:
        private static final String CREATE_ORDER_TYPE_TABLE = "CREATE TABLE " + ORDER_TYPE_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_TYPE_ID + " INTEGER,"
                + ORDER_TYPE + " VARCHAR(255));";
        private static final String DROP_ORDER_TYPE_TABLE = "DROP TABLE IF EXISTS " + ORDER_TYPE_TABLE_NAME;

        /**
         * Order Table
         */

        private static final String ORDERS_TABLE_NAME = "orders_table";
        private static final String orderId = "orderId";
        private static final String invoiceNo = "invoiceNo";
        private static final String customerPastelCode = "customerPastelCode";
        private static final String storeName = "storeName";
        private static final String deliveryDate = "deliveryDate";
        private static final String latitude = "latitude";
        private static final String longitude = "longitude";
        private static final String orderValueExc = "orderValueExc";
        private static final String orderValueInc = "orderValueInc";
        private static final String deliveryAddress = "deliveryAddress";
        private static final String user = "user";
        private static final String orderMass = "orderMass";
        private static final String uploaded = "uploaded";
        private static final String cashPaid = "cashPaid";
        private static final String offloaded = "offloaded";
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
        private static final String threshold = "threshold";
        //Creating the Order Table:
        private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + ORDERS_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + orderId + " INTEGER,"
                + invoiceNo + " VARCHAR(255),"
                + customerPastelCode + " VARCHAR(255),"
                + storeName + " VARCHAR(255),"
                + deliveryDate + " VARCHAR(255),"
                + latitude + " VARCHAR(255),"
                + longitude + " VARCHAR(255),"
                + orderValueExc + " VARCHAR(255),"
                + orderValueInc + " VARCHAR(255),"
                + deliveryAddress + " VARCHAR(255),"
                + user + " VARCHAR(255),"
                + orderMass + " VARCHAR(255),"
                + uploaded + " INTEGER,"
                + cashPaid + " VARCHAR(255),"
                + offloaded + " INTEGER,"
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
                + threshold + " VARCHAR(255));";
        private static final String DROP_ORDERS_TABLE = "DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME;


        /**
         * OrderLines Table
         */

        private static final String ORDERS_LINES_TABLE_NAME = "orders_lines";
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
        private static final String wareHouseName = "wareHouseName";
        //Creating the Order Table:
        private static final String CREATE_ORDERS_LINES_TABLE = "CREATE TABLE " + ORDERS_LINES_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + orderId + " VARCHAR(255),"
                + invoiceNo + " VARCHAR(255),"
                + customerPastelCode + " VARCHAR(255),"
                + storeName + " VARCHAR(255),"
                + deliveryDate + " VARCHAR(255),"
                + latitude + " VARCHAR(255),"
                + longitude + " VARCHAR(255),"
                + orderValueExc + " VARCHAR(255),"
                + orderValueInc + " VARCHAR(255),"
                + deliveryAddress + " VARCHAR(255),"
                + user + " VARCHAR(255),"
                + orderMass + " VARCHAR(255),"
                + productId + " INTEGER,"
                + qty + " INTEGER,"
                + price + " VARCHAR(255),"
                + pastelDescription + " VARCHAR(255),"
                + pastelCode + " VARCHAR(255),"
                + orderDetailId + " INTEGER,"
                + comment + " VARCHAR(255),"
                + returnQty + " VARCHAR(255),"
                + offLoadComment + " VARCHAR(255),"
                + blnoffloaded + " INTEGER,"
                + strCustomerReason + " VARCHAR(255),"
                + intWareHouseId + " VARCHAR(255),"
                + wareHouseName + " VARCHAR(255));";
        private static final String DROP_ORDERS_LINES_TABLE = "DROP TABLE IF EXISTS " + ORDERS_LINES_TABLE_NAME;


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
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}