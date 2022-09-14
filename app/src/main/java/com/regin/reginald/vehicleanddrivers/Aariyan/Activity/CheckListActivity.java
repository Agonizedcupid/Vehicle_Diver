package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.regin.reginald.vehicleanddrivers.R;

import org.w3c.dom.Text;

public class CheckListActivity extends AppCompatActivity {

    private TextView dateSelect, submitBtn;
    private Spinner vehicleSpinner, driverSpinner;

    private RadioButton radiatorYesBtn,radiatorNoBtn;
    private RadioButton engineOilYesBtn,engineOilNoBtn;
    private RadioButton breakFluidYesBtn,breakFluidNoBtn;
    private RadioButton tyreConditionYesBtn,tyreConditionNoBtn;
    private RadioButton tyreSpareYesBtn,tyreSpareNoBtn;
    private RadioButton jackYesBtn,jackNoBtn;
    private RadioButton spannerYesBtn,spannerNoBtn;
    private RadioButton fireYesBtn,fireNoBtn;
    private RadioButton capsWaterBtn,capsWaterNoBtn;
    private RadioButton capsFuelYesBtn,capsFuelNoBtn;
    private RadioButton dipstickYesBtn,dipstickNoBtn;
    private RadioButton windScreenYesBtn,windScreenNoBtn;
    private RadioButton sideMirrorsYesBtn,sideMirrorsNoBtn;
    private RadioButton lightMainYesBtn,lightMainNoBtn;
    private RadioButton lightIndicatorsYesBtn,lightIndicatorsNoBtn;
    private RadioButton brakesYesBtn,brakesNoBtn;
    private RadioButton cabBodyYesBtn,cabBodyNoBtn;
    private RadioButton fridgeWaterYesBtn,fridgeWaterNoBtn;
    private RadioButton fridgeOilYesBtn,fridgeOilNoBtn;
    private RadioButton fridgeBeltsYesBtn,fridgeBeltsNoBtn;
    private RadioButton fridgeDipstickYesBtn,fridgeDipstickNoBtn;
    private RadioButton fridgeBodyYesBtn,fridgeBodyNoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}