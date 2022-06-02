package com.regin.reginald.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class GsonHelper {
    private static Gson gsonObj;
    private static Gson gson = new Gson();

    public static synchronized Gson getGson() {
        return gson == null ? gson = new Gson() : gson;
    }

    public static String toString(Object modal) {

        try {
            String str = new Gson().toJson(modal);
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    public static Gson getInstance() {
        if (gsonObj == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            gsonObj = builder.create();
        }

        return gsonObj;
    }
}
