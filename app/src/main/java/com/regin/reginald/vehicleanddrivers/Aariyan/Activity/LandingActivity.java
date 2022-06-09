package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.CreditRequestFragment;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.HomeFragment;
import com.regin.reginald.vehicleanddrivers.Aariyan.Fragment.SetupFragment;
import com.regin.reginald.vehicleanddrivers.R;

public class LandingActivity extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
    private TextView toolbarTitle;
    public static ImageView resetDatabaseIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        initUI();
    }

    private void initUI() {

        toolbarTitle = findViewById(R.id.toolbarTitle);
        resetDatabaseIcon = findViewById(R.id.resetDatabaseIcon);

        navigationBar = findViewById(R.id.menu);
        navigationBar.setItemSelected(R.id.home_menu, true);

        //setting the default Fragment:
        //set default fragment:
        replaceFragment(new HomeFragment(), 0);

        triggerNavigationBar();
    }

    private void replaceFragment(Fragment fragment, int position) {
        setTitles(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void setTitles(int position) {
//        if (position == 2) {
//            resetDatabaseIcon.setVisibility(View.VISIBLE);
//        } else {
//            resetDatabaseIcon.setVisibility(View.GONE);
//        }
        resetDatabaseIcon.setVisibility(View.GONE);
        toolbarTitle.setText(Constant.title[position]);
    }

    private void triggerNavigationBar() {
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home_menu:
                        replaceFragment(new HomeFragment(), 0);
                        break;
                    case R.id.credit_request_menu:
                        replaceFragment(new CreditRequestFragment(), 1);
                        break;
                    case R.id.set_up_menu:
                        replaceFragment(new SetupFragment(), 2);
                        break;
                }
            }
        });

    }
}