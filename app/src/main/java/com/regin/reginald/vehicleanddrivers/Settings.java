package com.regin.reginald.vehicleanddrivers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.data.DatabaseHelper;
import com.regin.reginald.model.SettingsModel;

import java.util.ArrayList;
import java.util.Calendar;

//import com.example.reginald.model.SettingsModel;

/**
 * Created by Reginald on 2017-01-05.
 */
public class Settings extends AppCompatActivity {
    public static final String SERVERIP = "ServerIp";
    private SQLiteDatabase db;
    private Cursor c;
    Button submitSettings,test;
    String theIp,trucksheet;

    EditText serverIps;
    ImageView withoutcompany,withcompany,nofooter,noprev;
    SharedPreferences sharedpreferences;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    //private DatabaseHelper mDatabaseHelper;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    EditText deviceId,CompanyName,regKey,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

       // mDatabaseHelper = DatabaseHelper.getHelper(this);
        deviceId = (EditText) findViewById(R.id.device_id);
        CompanyName = (EditText) findViewById(R.id.company_name);
        regKey = (EditText) findViewById(R.id.reg_key);
        serverIps = (EditText) findViewById(R.id.serverIps);
        email = (EditText) findViewById(R.id.email);
        submitSettings = (Button) findViewById(R.id.submit_settings);

        ArrayList<SettingsModel> oD= dbH.getSettings();
       // int nameIndex2 = c.getColumnIndex("strTruckSheet");
        final String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        deviceId.setText(subscriberId);
        for (SettingsModel orderAttributes: oD){

            CompanyName.setText(orderAttributes.getCompany());
            regKey.setText(orderAttributes.getregKey());
            serverIps.setText(orderAttributes.getstrServerIp());
            email.setText(orderAttributes.getEmail());
        }
        submitSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbH.updateDeals("Delete from tblSettings");
                // strServerIp VARCHAR,regKey TEXT,Company VARCHAR,DeviceID TEXT,Email TEXT
                dbH.updateDeals("Insert into tblSettings (strServerIp,regKey,Company,DeviceID,Email) values('"+serverIps.getText().toString()+"','"+regKey.getText().toString()+"','"+CompanyName.getText().toString()+"','"+subscriberId+"','"+email.getText().toString()+"')");
                  Intent o = new Intent(Settings.this,LandingPage.class);
                  startActivity(o);
            }
        });
        //c.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent refresh = new Intent(Settings.this, Settings.class);
                    startActivity(refresh);

                } else {

                     AlertDialog.Builder builder = new  AlertDialog.Builder(Settings.this);
                    builder.setMessage("In Order for this App to work it is highly recommended to allow Location ")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent refresh = new Intent(Settings.this, Settings.class);
                                    startActivity(refresh);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return;
            }

        }
    }
    public void createSettings(String serverIp){
        String dateCreated = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        db.execSQL("INSERT INTO tblSettings (strServerIp,strDateCreated,UserName,Company,UserId)VALUES('"+serverIp+"','"+ dateCreated +"','reg','DIMS','0')");
    }
}
