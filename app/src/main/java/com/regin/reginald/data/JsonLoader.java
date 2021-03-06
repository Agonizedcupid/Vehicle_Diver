package com.regin.reginald.data;

import android.content.Context;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class JsonLoader
{
    public static JSONArray loadObject(Context context, int rawResId) throws IOException, JSONException
    {
        InputStream input_stream = context.getResources().openRawResource(rawResId);
        StringWriter writer = new StringWriter();

        IOUtils.copy(input_stream, writer);
        String input_string = writer.toString();

        JSONTokener tokener = new JSONTokener(input_string);

        return new JSONArray(tokener);
    }
}
