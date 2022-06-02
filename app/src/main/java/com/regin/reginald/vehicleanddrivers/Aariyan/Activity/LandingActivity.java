package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.CreditRequestFragment;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.HomeFragment;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.SetupFragment;
import com.regin.reginald.vehicleanddrivers.R;

public class LandingActivity extends AppCompatActivity {

    private ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        initUI();
    }

    private void initUI() {
        navigationBar = findViewById(R.id.menu);
        navigationBar.setItemSelected(R.id.home_menu, true);

        //setting the default Fragment:
        //set default fragment:
        replaceFragment(new HomeFragment(),0);

        triggerNavigationBar();
    }

    private void replaceFragment(Fragment fragment, int position) {
        setTitle(Constant.title[position]);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void triggerNavigationBar() {
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home_menu:
                        replaceFragment(new HomeFragment(),0);
                        break;
                    case R.id.credit_request_menu:
                        replaceFragment(new CreditRequestFragment(),1);
                        break;
                    case R.id.set_up_menu:
                        replaceFragment(new SetupFragment(),2);
                        break;
                }
            }
        });

    }
}