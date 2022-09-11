package com.regin.reginald.vehicleanddrivers.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.R;

import java.util.Calendar;

public class ClockScreenActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView clockInBtn, clockOutBtn;
    private EditText codeEditText;

    int hour, time;
    private static String amPm = "";
    String name = "", code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_screen);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initUI();
    }

    private void initUI() {
        clockInBtn = findViewById(R.id.clockInBtn);
        clockInBtn.setOnClickListener(this);
        clockOutBtn = findViewById(R.id.clockOutBtn);
        clockOutBtn.setOnClickListener(this);
        codeEditText = findViewById(R.id.codeEditText);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.clockInBtn:
                showTimer(true);
                break;
            case R.id.clockOutBtn:
                showTimer(false);
                break;
        }
    }

    private void showTimer(boolean flag) {
        TimePickerDialog dialog = new TimePickerDialog(ClockScreenActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                time = i1;

                if (hour >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, hour, time);

                String time = DateFormat.format("hh:mm", calendar).toString();

                String taskTime = time + " " + amPm;
                if (flag) {
                    clockInBtn.setText(taskTime);
                } else {
                    clockOutBtn.setText(taskTime);
                }

                Toast.makeText(ClockScreenActivity.this, "You've selected " + taskTime, Toast.LENGTH_SHORT).show();
            }
        }, 24, 0, false);

        dialog.updateTime(hour, time);
        dialog.show();
    }
}