package com.regin.reginald.vehicleanddrivers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.regin.reginald.model.CheckList;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OrderTypes;
import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.Routes;
import com.regin.reginald.model.SettingsModel;
import com.regin.reginald.model.WareHouses;

import java.util.ArrayList;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by Reginald on 31/08/2018.
 */

public class MyRawQueryHelper extends SQLiteOpenHelper {

    //public static String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/";
    public static final String DATABASE_NAME = "LinxDriversOrders.db";
    public static final int DATABASE_VERSION = 4;
    public static final String TIMESYNC_TABLE_NAME = "TimeSync";

    public MyRawQueryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //you need to create all the table in the oncreate  method so all the table you need to create when the database helper class is acceesssed
       /* db.execSQL("CREATE TABLE IF NOT EXISTS dealsHeaders (ID TEXT, OrderDate Text , DeliveryDate TEXT,OrderNumber TEXT,CustomerCode TEXT,CustomerDesc TEXT,Notes TEXT, UserID INT,DeliveryAddressID INT,Complete BOOLEAN,Uploaded BOOLEAN,LinesUploaded BOOLEAN,contact TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dealsLines  (ID TEXT, strPartNumber TEXT ,strDesc TEXT, Quantity DECIMAL," +
                "Price DECIMAL, Vat DECIMAL, Authorised BOOLEAN,Uploaded BOOLEAN,PriceInclusive DECIMAL,LineTotalPriceInclusive DECIMAL," +
                "DateFrom TEXT,DateTo TEXT,Margin DECIMAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Users  (UserID INT , UserTypeID INT,UserName TEXT,UserIdentity TEXT,UserStartDate TEXT,UserActive," +
                "PinCode TEXT,AddCustomers BIT,AddPrices BIT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS OrderHeaders (ID TEXT, OrderDate Text , DeliveryDate TEXT,OrderNumber TEXT,CustomerCode TEXT,CustomerDesc TEXT, Notes TEXT, UserID INT,DeliveryAddressID INT,Complete BOOLEAN,Uploaded BOOLEAN,LinesUploaded BOOLEAN)");
        db.execSQL("CREATE TABLE IF NOT EXISTS OrderLines  (ID TEXT, strPartNumber TEXT ,strDesc TEXT, Quantity DECIMAL,Price DECIMAL, Vat DECIMAL, Authorised BOOLEAN,Uploaded BOOLEAN,PriceInclusive DECIMAL,LineTotalPriceInclusive DECIMAL)");

*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
       /* db.execSQL("DROP TABLE IF EXISTS " + DEALS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEALSLINES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ORDERLINES_TABLE_NAME);*/
        Log.e("db-vers", "Updating table from " + oldVersion + " to " + newVersion);
        onCreate(db);
    }

    public void updateDeals(String update) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }
   /* public void getVersionDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }*/


    public ArrayList<Orders> getOrderHeaders() {
        ArrayList<Orders> header = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT * from OrderHeaders where InvoiceNo ='" + invoiceNo + "' and Loyalty !=0 limit 1", null);


        return cursor2.getCount();
    }

    public ArrayList<OrderLines> getOrderLines(int OrderId) {
        ArrayList<OrderLines> lines = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

    public ArrayList<Routes> getRoutes() {
        ArrayList<Routes> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From Routes", null);

        Routes routes = null;
        if (cursor.moveToFirst()) {
            do {
                routes = new Routes();

                routes.setRouteName(cursor.getString(2));
                labels.add(routes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Routes> multiId(String SQL) {
        ArrayList<Routes> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

    public ArrayList<Orders> returnOrderHeaders() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
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
                orders.setThreshold(cursor.getString(cursor.getColumnIndex("Threshold")));
                labels.add(orders);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<Orders> returnOrderHeadersNotUploaded() {
        ArrayList<Orders> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "'", null);

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

    public int returnOrderLinesCrateCount(String InvoiceNo) {

        int cratesCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select Qty from OrderLines where OrderId ='" + InvoiceNo + "'", null);

        OrderLines orders = null;
        if (cursor.moveToFirst()) {
            do {
                cratesCount = cratesCount + Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty")));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cratesCount;
    }

    public ArrayList<OrderLines> returnCreditRequisition(String InvoiceNo) {
        ArrayList<OrderLines> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "'", null);

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

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (warehouse.equals("ALL")) {
            cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "'", null);
        } else {
            cursor = db.rawQuery("Select * from OrderLines where OrderId ='" + InvoiceNo + "' and WareHouseName='" + warehouse + "'", null);
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderLines where blnoffloaded=0 and OrderID='" + invoice + "'", null);

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
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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

    public String OrdersNotPostedToTheOffice() {
        //X/Y
        SQLiteDatabase db = this.getWritableDatabase();
        int getString = 0;
        int totalOrders = 0;
        try {
            Cursor cursor = db.rawQuery("Select * from OrderHeaders where Uploaded=0 ", null);
            Cursor cursorUploaded = db.rawQuery("Select * from OrderHeaders where Uploaded=1 ", null);

            String status = "";

            //cursor.moveToFirst();
            // cursorUploaded.moveToFirst();
            getString = cursor.getCount();
            totalOrders = cursorUploaded.getCount();
            Log.e("count", "result*************************************************countResult" + getString);
        } catch (Exception e) {

        }
        return getString + "/" + totalOrders + " NOT UPLOADED";
    }

    public int checkiflinesuploaded() {
        //X/Y
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TabletId", TabletId);
        values.put("Messages", Messages);

// Inserting Row
        db.insert("Notifications", null, values);
    }

    public void insertDeliveryNoteLines(String ref, String custname, String notes, String qty, String weights, String delDate, String ProdName) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        //CustomerName,ProductName,Qty,Weights,DeliveryDate,Notes,ReferenceNumber
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", custname);
        contentValues.put("DeliveryDate", delDate);
        contentValues.put("ReferenceNumber", ref);
        contentValues.put("username", username);
        db.insert("tblDeliveryNotesHeader", null, contentValues);

    }

    public void insertCreditNoteLines(String ref, String custname, String notes, String qty, String weights, String delDate, String ProdName) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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

        SQLiteDatabase db = this.getWritableDatabase();
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
}
