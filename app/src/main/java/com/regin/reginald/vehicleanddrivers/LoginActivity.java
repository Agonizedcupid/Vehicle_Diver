package com.regin.reginald.vehicleanddrivers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.SettingsModel;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    //
    EditText et_email,et_password;
    TextView slogan,tabkey;
    Button btn_login,setupbtn;
    //private DatabaseHelper mDatabaseHelper;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    String registrations;
    int len = 0;
    String customerOrders,LINX = "http://102.37.0.48/driversapp/Login.php?key=";
     String subscriberId;
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //mDatabaseHelper = DatabaseHelper.getHelper(this);
        ArrayList<SettingsModel> oD= dbH.getSettings();
        for (SettingsModel orderAttributes: oD){

            registrations=   orderAttributes.getregKey();

        }
        if(registrations.length() > 4)
        {
            Intent i = new Intent(LoginActivity.this,LandingPage.class);
            startActivity(i);
        }
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        setupbtn = (Button) findViewById(R.id.setupbtn);
        slogan = (TextView) findViewById(R.id.slogan);
        tabkey = (TextView) findViewById(R.id.tabkey);
        tabkey.setText(subscriberId);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Login",""+LINX + subscriberId+"&username="+et_email.getText().toString()+"&password="+et_password.getText().toString());
                startProgress("...");
                new UserLogin().execute(LINX + subscriberId+"&username="+et_email.getText().toString()+"&password="+et_password.getText().toString());

            }
        });
        setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s =  new Intent(LoginActivity.this,PrinterFunctionActivity.class);
                startActivity(s);
            }
        });
    }
    private class UserLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Log.e("do in bckgrnd", "has been reached");
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            len = result.length();
            customerOrders = result.toString();
          //  Log.e("onpostexecute", "has been reached");
            Log.e("info","------------------"+customerOrders);

            if (len > 0) {

                try {
                    JSONArray BoardInfo = new JSONArray(customerOrders);
                    Log.e("info","------------------"+customerOrders);
                    progressDoalog.dismiss();
                    for (int j = 0; j < BoardInfo.length(); ++j) {

                        JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                        String results;
                        results = BoardDetails.getString("results");

                        Log.e("this is definitely", "not !"+results);

                        if (results.equals("NOT REGISTERED")) {
                           // get.setVisibility(View.INVISIBLE);
                            slogan.setText("Wrong credentials, please try again.");
                            slogan.setBackgroundColor(Color.RED);
                            //Log.e("this is definitely", "not registered!!!!!!!!");
                        } else {
                            Log.e("log","update tblSettings set regKey='"+et_email.getText().toString()+"' where DeviceID='"+subscriberId+"' ");
                            dbH.updateDeals("update tblSettings set regKey='"+et_email.getText().toString()+"' where DeviceID='"+subscriberId+"'");
                            Intent h = new Intent(LoginActivity.this,LandingPage.class);
                            startActivity(h);

                        }
                    }

                } catch (Exception e) {
                    Log.e("JSON", e.getMessage());
                }
            }
        }
    }
    public static String GET(String urlp) {

        String movieJsonStr = "";
        String result = "";
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        URL url;

        try{
            url = new URL(urlp);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Initialize a new string buffer object
            StringBuffer stringBuffer = new StringBuffer();

            String line ="";
            // Loop through the lines
            while((line= bufferedReader.readLine())!=null){
                // Append the current line to string buffer
                stringBuffer.append(line);
            }

            movieJsonStr =  stringBuffer.toString();

        } catch (Throwable e) {
            Log.e("backgroundtask", "EXCEPTION", e);
        } finally {
            connection.disconnect();
            try {


                if(bufferedReader != null)
                    bufferedReader.close();
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.e("READER.CLOSE()", e.toString());
            }
        }

        try {
            result = movieJsonStr;
        } catch (Throwable e) {
            Log.e("BACKGROUNDTASK", "EXCEPTION FROM jsonParse()", e);
        }
        return result;
    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Verifying your login credentials");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
}
