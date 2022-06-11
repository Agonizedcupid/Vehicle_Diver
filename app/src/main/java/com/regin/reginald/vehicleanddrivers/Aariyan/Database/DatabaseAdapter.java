package com.regin.reginald.vehicleanddrivers.Aariyan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    DatabaseHelper helper;


    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
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

    //Insert ROUTES:
    public long insertOrderTypes(int orderID, String orderType) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ORDER_TYPE_ID, orderID);
        contentValues.put(DatabaseHelper.ORDER_TYPE, orderType);

        long id = database.insert(DatabaseHelper.ORDER_TYPE_TABLE_NAME, null, contentValues);
        return id;
    }

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
        private static final int VERSION_NUMBER = 3;

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
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}