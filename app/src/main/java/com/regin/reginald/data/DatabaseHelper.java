package com.regin.reginald.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.regin.reginald.model.*;
import com.regin.reginald.model.Orders;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.support.ConnectionSource;

import java.util.ArrayList;
import java.util.List;

//import nl.elastique.poetry.database.DatabaseConfiguration;

import com.regin.reginald.data.*;



public class DatabaseHelper
        //extends OrmLiteSqliteOpenHelper
{
    public static String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/";
    public static final String DATABASE_NAME = "LinxDriversOrders.db";

//    public final static DatabaseConfiguration sConfiguration = new DatabaseConfiguration
//            (4, new Class<?>[]{Orders.class}, DATABASE_NAME);
//
////    public DatabaseHelper(Context context) {
////        super(context, sConfiguration);
////    }
//
//    public DatabaseHelper(Context context)
//    {
//        super(context, sConfiguration.getDatabaseName(), null, sConfiguration.getModelVersion());
//    }
//
//    public static DatabaseHelper getHelper(Context context) {
//        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//
//        // When calling the parent class, the whole database is deleted and re-created.
//        // Custom upgrade code goes here to override that behavior.
//    }


}