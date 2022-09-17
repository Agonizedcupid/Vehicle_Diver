package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.DriverInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.VehicleInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DriverModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.VehicleModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.CheckListNetworking;
import com.regin.reginald.vehicleanddrivers.R;

import org.w3c.dom.Text;

import java.sql.Driver;
import java.util.Calendar;
import java.util.List;

public class CheckListActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView dateSelect, submitBtn;
    private Spinner vehicleSpinner, driverSpinner;

    private String driverSpinnerString, vehicleSpinnerString;

    private RadioButton radiatorYesBtn,radiatorNoBtn;
    private String radiatorString;
    private RadioButton engineOilYesBtn,engineOilNoBtn;
    private String engineOilString;
    private RadioButton breakFluidYesBtn,breakFluidNoBtn;
    private String breakFluidString;
    private RadioButton tyreConditionYesBtn,tyreConditionNoBtn;
    private String tyreConditionString;
    private RadioButton tyreSpareYesBtn,tyreSpareNoBtn;
    private String tyreSpareString;
    private RadioButton jackYesBtn,jackNoBtn;
    private String jacString;
    private RadioButton spannerYesBtn,spannerNoBtn;
    private String spannerString;
    private RadioButton fireYesBtn,fireNoBtn;
    private String fireString;
    private RadioButton capsWaterBtn,capsWaterNoBtn;
    private String capsWaterString;
    private RadioButton capsFuelYesBtn,capsFuelNoBtn;
    private String capsFuelString;
    private RadioButton dipstickYesBtn,dipstickNoBtn;
    private String dipstickString;
    private RadioButton windScreenYesBtn,windScreenNoBtn;
    private String windScreeString;
    private RadioButton sideMirrorsYesBtn,sideMirrorsNoBtn;
    private String sideMirrorString;
    private RadioButton lightMainYesBtn,lightMainNoBtn;
    private String lightMainString;
    private RadioButton lightIndicatorsYesBtn,lightIndicatorsNoBtn;
    private String lightIndicatorsString;
    private RadioButton brakesYesBtn,brakesNoBtn;
    private String brakesString;
    private RadioButton cabBodyYesBtn,cabBodyNoBtn;
    private String cabBodyString;
    private RadioButton fridgeWaterYesBtn,fridgeWaterNoBtn;
    private String fridgeWaterString;
    private RadioButton fridgeOilYesBtn,fridgeOilNoBtn;
    private String fridgeOilString;
    private RadioButton fridgeBeltsYesBtn,fridgeBeltsNoBtn;
    private String fridgeBeltsString;
    private RadioButton fridgeDipstickYesBtn,fridgeDipstickNoBtn;
    private String fridgeDipstickString;
    private RadioButton fridgeBodyYesBtn,fridgeBodyNoBtn;
    private String fridgeBodyString;

    private DatabaseAdapter adapter;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    String date = "";
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        adapter = new DatabaseAdapter(this);

        initUI();
    }

    private void initUI() {
        dateSelect = findViewById(R.id.selectDate);
        dateSelect.setOnClickListener(this);
        submitBtn = findViewById(R.id.submitCheckList);
        submitBtn.setOnClickListener(this);

        vehicleSpinner = findViewById(R.id.vehicleSpinner);
        driverSpinner = findViewById(R.id.driverSpinner);

        radiatorYesBtn = findViewById(R.id.radiatorYesBtn);
        radiatorYesBtn.setOnClickListener(this);

        radiatorNoBtn = findViewById(R.id.radiatorNoBtn);
        radiatorNoBtn.setOnClickListener(this);

        engineOilYesBtn = findViewById(R.id.engineOilYesBtn);
        engineOilYesBtn.setOnClickListener(this);

        engineOilNoBtn = findViewById(R.id.engineOilNoBtn);
        engineOilNoBtn.setOnClickListener(this);

        breakFluidYesBtn = findViewById(R.id.breakFluidYesBtn);
        breakFluidYesBtn.setOnClickListener(this);

        breakFluidNoBtn = findViewById(R.id.breakFluidNoBtn);
        breakFluidNoBtn.setOnClickListener(this);

        tyreConditionYesBtn = findViewById(R.id.tyreYesBtn);
        tyreConditionYesBtn.setOnClickListener(this);

        tyreConditionNoBtn = findViewById(R.id.tyreNoBtn);
        tyreConditionNoBtn.setOnClickListener(this);

        tyreSpareYesBtn = findViewById(R.id.tyreSpareYesBtn);
        tyreSpareYesBtn.setOnClickListener(this);

        tyreSpareNoBtn = findViewById(R.id.tyreSpareNoBtn);
        tyreSpareNoBtn.setOnClickListener(this);

        jackYesBtn = findViewById(R.id.jackYesBtn);
        jackYesBtn.setOnClickListener(this);

        jackNoBtn = findViewById(R.id.jackNoBtn);
        jackNoBtn.setOnClickListener(this);

        spannerYesBtn = findViewById(R.id.spannerYesBtn);
        spannerYesBtn.setOnClickListener(this);

        spannerNoBtn = findViewById(R.id.spannerNoBtn);
        spannerNoBtn.setOnClickListener(this);

        fireYesBtn = findViewById(R.id.fireYesBtn);
        fireYesBtn.setOnClickListener(this);

        fireNoBtn = findViewById(R.id.fireNoBtn);
        fireNoBtn.setOnClickListener(this);

        capsWaterBtn = findViewById(R.id.capsWaterYesBtn);
        capsWaterBtn.setOnClickListener(this);

        capsWaterNoBtn = findViewById(R.id.capsWaterNoBtn);
        capsWaterNoBtn.setOnClickListener(this);

        capsFuelYesBtn = findViewById(R.id.capsFuelYesBtn);
        capsFuelYesBtn.setOnClickListener(this);

        capsFuelNoBtn = findViewById(R.id.capsFuelNoBtn);
        capsFuelNoBtn.setOnClickListener(this);


        dipstickYesBtn = findViewById(R.id.dipstickYesBtn);
        dipstickYesBtn.setOnClickListener(this);

        dipstickNoBtn = findViewById(R.id.dipstickNoBtn);
        dipstickNoBtn.setOnClickListener(this);

        windScreenYesBtn = findViewById(R.id.windscreenYesBtn);
        windScreenYesBtn.setOnClickListener(this);

        windScreenNoBtn = findViewById(R.id.windscreenNoBtn);
        windScreenNoBtn.setOnClickListener(this);

        sideMirrorsYesBtn = findViewById(R.id.sideMirrorsYesBtn);
        sideMirrorsYesBtn.setOnClickListener(this);

        sideMirrorsNoBtn = findViewById(R.id.sideMirrorsNoBtn);
        sideMirrorsNoBtn.setOnClickListener(this);

        lightMainYesBtn = findViewById(R.id.lightMainYesBtn);
        lightMainYesBtn.setOnClickListener(this);

        lightMainNoBtn = findViewById(R.id.lightMainNoBtn);
        lightMainNoBtn.setOnClickListener(this);

        lightIndicatorsYesBtn = findViewById(R.id.lightIndicatorsYesBtn);
        lightIndicatorsYesBtn.setOnClickListener(this);

        lightIndicatorsNoBtn = findViewById(R.id.lightIndicatorsNoBtn);
        lightIndicatorsNoBtn.setOnClickListener(this);

        brakesYesBtn = findViewById(R.id.brakesYesBtn);
        brakesYesBtn.setOnClickListener(this);

        brakesNoBtn = findViewById(R.id.brakesNoBtn);
        brakesNoBtn.setOnClickListener(this);

        cabBodyYesBtn = findViewById(R.id.cabBodyYesBtn);
        cabBodyYesBtn.setOnClickListener(this);

        cabBodyNoBtn = findViewById(R.id.cabBodyNoBtn);
        cabBodyNoBtn.setOnClickListener(this);

        fridgeWaterYesBtn = findViewById(R.id.fridgeWaterYesBtn);
        fridgeWaterYesBtn.setOnClickListener(this);

        fridgeWaterNoBtn = findViewById(R.id.fridgeWaterNoBtn);
        fridgeWaterNoBtn.setOnClickListener(this);

        fridgeOilYesBtn = findViewById(R.id.fridgeOilYesBtn);
        fridgeOilYesBtn.setOnClickListener(this);

        fridgeOilNoBtn = findViewById(R.id.fridgeOilNoBtn);
        fridgeOilNoBtn.setOnClickListener(this);

        fridgeBeltsYesBtn = findViewById(R.id.fridgeBeltsYesBtn);
        fridgeBeltsYesBtn.setOnClickListener(this);

        fridgeBeltsNoBtn = findViewById(R.id.fridgeBeltsNoBtn);
        fridgeBeltsNoBtn.setOnClickListener(this);

        fridgeDipstickYesBtn = findViewById(R.id.fridgeDipstickYesBtn);
        fridgeDipstickYesBtn.setOnClickListener(this);

        fridgeDipstickNoBtn = findViewById(R.id.fridgeDipstickNoBtn);
        fridgeDipstickNoBtn.setOnClickListener(this);

        fridgeBodyYesBtn = findViewById(R.id.fridgeBodyYesBtn);
        fridgeBodyYesBtn.setOnClickListener(this);

        fridgeBodyNoBtn = findViewById(R.id.fridgeBodyNoBtn);
        fridgeBodyNoBtn.setOnClickListener(this);



        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadDrivers_N_Vehicle();
    }

    private void loadDrivers_N_Vehicle() {
        //Spinner items
        CheckListNetworking networking = new CheckListNetworking(adapter);
        networking.getDriversList(new DriverInterface() {
            @Override
            public void listOfDrivers(List<DriverModel> listOfDrivers) {
                ArrayAdapter<DriverModel> dataAdapter = new ArrayAdapter<DriverModel>(CheckListActivity.this,
                        android.R.layout.simple_spinner_item, listOfDrivers);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                driverSpinner.setAdapter(dataAdapter);
                driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //VariationModel model = product.getVariationList().get(position);
                        DriverModel model = listOfDrivers.get(position);
                        driverSpinnerString = model.getDriverName();
                        Toast.makeText(CheckListActivity.this, "" + model.getDriverName() + " Selected!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        });

        networking.getVehiclesList(new VehicleInterface() {
            @Override
            public void listOfVehicle(List<VehicleModel> listOfVehicles) {
                ArrayAdapter<VehicleModel> dataAdapter = new ArrayAdapter<VehicleModel>(CheckListActivity.this,
                        android.R.layout.simple_spinner_item, listOfVehicles);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehicleSpinner.setAdapter(dataAdapter);
                vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //VariationModel model = product.getVariationList().get(position);
                        VehicleModel model = listOfVehicles.get(position);
                        vehicleSpinnerString = model.getTruckName();
                        Toast.makeText(CheckListActivity.this, "" + model.getTruckName() + " Selected!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.selectDate:
                openDatePicker();
                break;
            case R.id.radiatorYesBtn:
                break;
            case R.id.radiatorNoBtn:
                break;
            case R.id.engineOilYesBtn:
                break;
            case R.id.engineOilNoBtn:
                break;
            case R.id.breakFluidYesBtn:
                break;
            case R.id.breakFluidNoBtn:
                break;
            case R.id.tyreYesBtn:
                break;
            case R.id.tyreNoBtn:
                break;

            case R.id.tyreSpareYesBtn:
                break;
            case R.id.tyreSpareNoBtn:
                break;
            case R.id.jackYesBtn:
                break;
            case R.id.jackNoBtn:
                break;
            case R.id.spannerYesBtn:
                break;
            case R.id.spannerNoBtn:
                break;
            case R.id.fireYesBtn:
                break;
            case R.id.fireNoBtn:
                break;
            case R.id.capsWaterYesBtn:
                break;
            case R.id.capsWaterNoBtn:
                break;

            case R.id.capsFuelYesBtn:
                break;
            case R.id.capsFuelNoBtn:
                break;
            case R.id.dipstickYesBtn:
                break;
            case R.id.dipstickNoBtn:
                break;
            case R.id.windscreenYesBtn:
                break;
            case R.id.windscreenNoBtn:
                break;
            case R.id.sideMirrorsYesBtn:
                break;
            case R.id.sideMirrorsNoBtn:
                break;
            case R.id.lightMainYesBtn:
                break;
            case R.id.lightMainNoBtn:
                break;
            case R.id.lightIndicatorsYesBtn:
                break;
            case R.id.lightIndicatorsNoBtn:
                break;
            case R.id.brakesYesBtn:
                break;
            case R.id.brakesNoBtn:
                break;
            case R.id.cabBodyYesBtn:
                break;
            case R.id.cabBodyNoBtn:
                break;
            case R.id.fridgeWaterYesBtn:
                break;
            case R.id.fridgeWaterNoBtn:
                break;
            case R.id.fridgeOilYesBtn:
                break;
            case R.id.fridgeOilNoBtn:
                break;

            case R.id.fridgeBeltsYesBtn:
                break;

            case R.id.fridgeBeltsNoBtn:
                break;
            case R.id.fridgeDipstickYesBtn:
                break;
            case R.id.fridgeDipstickNoBtn:
                break;

            case R.id.fridgeBodyYesBtn:
                break;
            case R.id.fridgeBodyNoBtn:
                break;
        }
    }

    private void openDatePicker() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                //Month
                int j = i1 + 1;

                //date = i + "-" + j + "-" + i2;
                //date = i2 + "-" + j + "-" + i;
                date = i + "-" + j + "-" + i2;
                //2022-1-15
                dateSelect.setText(date);

            }
            //}, day, month, year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        new DatePickerDialog(AddTimeActivity.this, null, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        datePickerDialog.show();
    }
}