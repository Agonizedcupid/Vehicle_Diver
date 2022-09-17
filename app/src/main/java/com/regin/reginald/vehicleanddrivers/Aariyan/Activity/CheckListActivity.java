package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.DriverInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.PostCheckListInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.VehicleInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DriverModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.PostCheckListModel;
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

    private RadioButton radiatorYesBtn, radiatorNoBtn;
    private String radiatorString = "Yes";
    private EditText radiatorEditText;
    private RadioButton engineOilYesBtn, engineOilNoBtn;
    private String engineOilString = "Yes";
    private EditText engineOilEditText;
    private RadioButton breakFluidYesBtn, breakFluidNoBtn;
    private String breakFluidString = "Yes";
    private EditText breakFluidEditText;
    private RadioButton tyreConditionYesBtn, tyreConditionNoBtn;
    private String tyreConditionString = "Yes";
    private EditText tyreConditionEditText;
    private RadioButton tyreSpareYesBtn, tyreSpareNoBtn;
    private String tyreSpareString = "Yes";
    private EditText tyreSpareEditText;
    private RadioButton jackYesBtn, jackNoBtn;
    private String jacString = "Yes";
    private EditText jackEditText;
    private RadioButton spannerYesBtn, spannerNoBtn;
    private String spannerString = "Yes";
    private EditText spannerEditText;
    private RadioButton fireYesBtn, fireNoBtn;
    private String fireString = "Yes";
    private EditText fireEditText;
    private RadioButton capsWaterBtn, capsWaterNoBtn;
    private String capsWaterString = "Yes";
    private EditText capsWaterEditText;
    private RadioButton capsFuelYesBtn, capsFuelNoBtn;
    private String capsFuelString = "Yes";
    private EditText capsFuelEditText;
    private RadioButton dipstickYesBtn, dipstickNoBtn;
    private String dipstickString = "Yes";
    private EditText dipstickEditText;
    private RadioButton windScreenYesBtn, windScreenNoBtn;
    private String windScreeString = "Yes";
    private EditText windScreenEditText;
    private RadioButton sideMirrorsYesBtn, sideMirrorsNoBtn;
    private String sideMirrorString = "Yes";
    private EditText sideMirrorEditText;
    private RadioButton lightMainYesBtn, lightMainNoBtn;
    private String lightMainString = "Yes";
    private EditText lightMainEditText;
    private RadioButton lightIndicatorsYesBtn, lightIndicatorsNoBtn;
    private String lightIndicatorsString = "Yes";
    private EditText lightIndicatorEditText;
    private RadioButton brakesYesBtn, brakesNoBtn;
    private String brakesString = "Yes";
    private EditText brakesEditText;
    private RadioButton cabBodyYesBtn, cabBodyNoBtn;
    private String cabBodyString = "Yes";
    private EditText cabBodyEditText;
    private RadioButton fridgeWaterYesBtn, fridgeWaterNoBtn;
    private String fridgeWaterString = "Yes";
    private EditText fridgeWaterEditText;
    private RadioButton fridgeOilYesBtn, fridgeOilNoBtn;
    private String fridgeOilString = "Yes";
    private EditText fridgeOilEditText;
    private RadioButton fridgeBeltsYesBtn, fridgeBeltsNoBtn;
    private String fridgeBeltsString = "Yes";
    private EditText fridgeBeltsEditText;
    private RadioButton fridgeDipstickYesBtn, fridgeDipstickNoBtn;
    private String fridgeDipstickString = "Yes";
    private EditText fridgeDipstickEditText;
    private RadioButton fridgeBodyYesBtn, fridgeBodyNoBtn;
    private String fridgeBodyString = "Yes";
    private EditText fridgeBodyEditText;

    private DatabaseAdapter adapter;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    String date = "";
    private int year;
    private int month;
    private int day;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        sharedPref = getSharedPreferences(
                "LL", Context.MODE_PRIVATE);
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

        //Edit text:
        radiatorEditText = findViewById(R.id.radiatorComment);
        engineOilEditText = findViewById(R.id.engineOilComment);
        breakFluidEditText = findViewById(R.id.breakFluidComment);
        tyreConditionEditText = findViewById(R.id.tyreComment);
        tyreSpareEditText = findViewById(R.id.tyreSpareComment);
        jackEditText = findViewById(R.id.jackComment);
        spannerEditText = findViewById(R.id.spannerComment);
        fireEditText = findViewById(R.id.fireComment);
        capsWaterEditText = findViewById(R.id.capsWaterComment);
        capsFuelEditText = findViewById(R.id.capsFuelComment);
        dipstickEditText = findViewById(R.id.dipstickComment);
        windScreenEditText = findViewById(R.id.windscreenComment);
        sideMirrorEditText = findViewById(R.id.sideMirrorsComment);
        lightMainEditText = findViewById(R.id.lightMainComment);
        lightIndicatorEditText = findViewById(R.id.lightIndicatorsComment);
        brakesEditText = findViewById(R.id.brakesComment);
        cabBodyEditText = findViewById(R.id.cabBodyComment);
        fridgeWaterEditText = findViewById(R.id.fridgeWaterComment);
        fridgeOilEditText = findViewById(R.id.fridgeOilComment);
        fridgeBeltsEditText = findViewById(R.id.fridgBeltsComment);
        fridgeDipstickEditText = findViewById(R.id.fridgeDipstickComment);
        fridgeBodyEditText = findViewById(R.id.fridgeBodyComment);


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
            case R.id.submitCheckList:
                submitCheckList();
                break;
            case R.id.selectDate:
                openDatePicker();
                break;
            case R.id.radiatorYesBtn:
                radiatorString = "Yes";
                break;
            case R.id.radiatorNoBtn:
                radiatorString = "No";
                break;
            case R.id.engineOilYesBtn:
                engineOilString = "Yes";
                break;
            case R.id.engineOilNoBtn:
                engineOilString = "No";
                break;
            case R.id.breakFluidYesBtn:
                breakFluidString = "Yes";
                break;
            case R.id.breakFluidNoBtn:
                breakFluidString = "No";
                break;
            case R.id.tyreYesBtn:
                tyreConditionString = "Yes";
                break;
            case R.id.tyreNoBtn:
                tyreConditionString = "No";
                break;

            case R.id.tyreSpareYesBtn:
                tyreSpareString = "Yes";
                break;
            case R.id.tyreSpareNoBtn:
                tyreSpareString = "No";
                break;
            case R.id.jackYesBtn:
                jacString = "Yes";
                break;
            case R.id.jackNoBtn:
                jacString = "No";
                break;
            case R.id.spannerYesBtn:
                spannerString = "Yes";
                break;
            case R.id.spannerNoBtn:
                spannerString = "No";
                break;
            case R.id.fireYesBtn:
                fireString = "Yes";
                break;
            case R.id.fireNoBtn:
                fireString = "No";
                break;
            case R.id.capsWaterYesBtn:
                capsWaterString = "Yes";
                break;
            case R.id.capsWaterNoBtn:
                capsWaterString = "No";
                break;

            case R.id.capsFuelYesBtn:
                capsFuelString = "Yes";
                break;
            case R.id.capsFuelNoBtn:
                capsFuelString = "No";
                break;
            case R.id.dipstickYesBtn:
                dipstickString = "Yes";
                break;
            case R.id.dipstickNoBtn:
                dipstickString = "No";
                break;
            case R.id.windscreenYesBtn:
                windScreeString = "Yes";
                break;
            case R.id.windscreenNoBtn:
                windScreeString = "No";
                break;
            case R.id.sideMirrorsYesBtn:
                sideMirrorString = "Yes";
                break;
            case R.id.sideMirrorsNoBtn:
                sideMirrorString = "No";
                break;
            case R.id.lightMainYesBtn:
                lightMainString = "Yes";
                break;
            case R.id.lightMainNoBtn:
                lightMainString = "No";
                break;
            case R.id.lightIndicatorsYesBtn:
                lightIndicatorsString = "Yes";
                break;
            case R.id.lightIndicatorsNoBtn:
                lightIndicatorsString = "No";
                break;
            case R.id.brakesYesBtn:
                brakesString = "Yes";
                break;
            case R.id.brakesNoBtn:
                brakesString = "No";
                break;
            case R.id.cabBodyYesBtn:
                cabBodyString = "Yes";
                break;
            case R.id.cabBodyNoBtn:
                cabBodyString = "No";
                break;
            case R.id.fridgeWaterYesBtn:
                fridgeWaterString = "Yes";
                break;
            case R.id.fridgeWaterNoBtn:
                fridgeWaterString = "No";
                break;
            case R.id.fridgeOilYesBtn:
                fridgeOilString = "Yes";
                break;
            case R.id.fridgeOilNoBtn:
                fridgeOilString = "No";
                break;

            case R.id.fridgeBeltsYesBtn:
                fridgeBeltsString = "Yes";
                break;

            case R.id.fridgeBeltsNoBtn:
                fridgeBeltsString = "No";
                break;
            case R.id.fridgeDipstickYesBtn:
                fridgeDipstickString = "Yes";
                break;
            case R.id.fridgeDipstickNoBtn:
                fridgeDipstickString = "No";
                break;

            case R.id.fridgeBodyYesBtn:
                fridgeBodyString = "Yes";
                break;
            case R.id.fridgeBodyNoBtn:
                fridgeBodyString = "No";
                break;
        }
    }

    private void submitCheckList() {
        PostCheckListModel model = new PostCheckListModel(
                "" + driverSpinnerString,
                "" + date,
                "" + radiatorString,
                "" + engineOilString,
                "" + tyreConditionString,
                "" + tyreSpareString,
                "" + jacString,
                "" + spannerString,
                "" + fireString,
                "" + capsWaterString,
                "" + capsFuelString,
                "" + dipstickString,
                "" + windScreeString,
                "" + sideMirrorString,
                "" + lightMainString,
                "" + lightIndicatorsString,
                "" + brakesString,
                "" + cabBodyString,
                "" + fridgeWaterString,
                "" + fridgeOilString,
                "" + fridgeBeltsString,
                "" + fridgeDipstickString,
                "" + fridgeBodyString,
                "" + radiatorEditText.getText().toString(),
                "" + engineOilEditText.getText().toString(),
                "" + tyreConditionEditText.getText().toString(),
                "" + tyreSpareEditText.getText().toString(),
                "" + jackEditText.getText().toString(),
                "" + spannerEditText.getText().toString(),
                "" + fireEditText.getText().toString(),
                "" + capsWaterEditText.getText().toString(),
                "" + capsFuelEditText.getText().toString(),
                "" + dipstickEditText.getText().toString(),
                "" + windScreenEditText,
                "" + sideMirrorEditText.getText().toString(),
                "" + lightMainEditText.getText().toString(),
                "" + lightIndicatorEditText.getText().toString(),
                "" + brakesEditText.getText().toString(),
                "" + cabBodyEditText.getText().toString(),
                "" + fridgeWaterEditText.getText().toString(),
                "" + fridgeOilEditText.getText().toString(),
                "" + fridgeBeltsEditText.getText().toString(),
                "" + fridgeDipstickEditText.getText().toString(),
                "" + fridgeBodyEditText,
                "" + sharedPref.getString("id",""+1),
                "" + vehicleSpinnerString
        );
        CheckListNetworking networking = new CheckListNetworking(adapter);
        networking.postCheckList(new PostCheckListInterface() {
            @Override
            public void success(String message) {
                Toast.makeText(CheckListActivity.this, ""+message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(String errorMessage) {
                Toast.makeText(CheckListActivity.this, ""+errorMessage, Toast.LENGTH_SHORT).show();
            }
        }, model);
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