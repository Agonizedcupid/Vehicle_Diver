package com.regin.reginald.vehicleanddrivers.Aariyan.Abstraction;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Data;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    public IpModel getServerModel(Context context) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        List<IpModel> serverIp = databaseAdapter.getServerIpModel();
        return new IpModel(
                serverIp.get(0).getServerIp(), serverIp.get(0).getEmailAddress(), serverIp.get(0).getCompanyName(),
                serverIp.get(0).getDeviceId(), serverIp.get(0).getRegKey()
        );
    }

    public boolean isServerExist(Context context) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        return databaseAdapter.getServerIpModel().size() > 0;
    }
}
