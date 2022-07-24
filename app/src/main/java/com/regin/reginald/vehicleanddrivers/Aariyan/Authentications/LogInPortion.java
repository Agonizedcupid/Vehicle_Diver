package com.regin.reginald.vehicleanddrivers.Aariyan.Authentications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.vehicleanddrivers.LandingPage;
import com.regin.reginald.vehicleanddrivers.R;

public class LogInPortion extends AppCompatActivity {


    private TextView logInBtn;
    private EditText enterName, enterPassword;

    private ConstraintLayout snackBarLayout;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_portion);

        initUI();
    }

    private void initUI() {
        enterName = findViewById(R.id.enterName);
        enterPassword = findViewById(R.id.enterPassword);
        logInBtn = findViewById(R.id.logInBtn);

        snackBarLayout = findViewById(R.id.sBar);

        progressBar = findViewById(R.id.progressbar);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogIn();
            }
        });
    }

    private void startLogIn() {
        if (TextUtils.isEmpty(enterName.getText().toString().trim())) {
            enterName.setError("Name Can't be empty!");
            enterName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(enterPassword.getText().toString().trim())) {
            enterPassword.setError("Password Can't be empty!");
            enterPassword.requestFocus();
            return;
        }
        //
        progressBar.setVisibility(View.VISIBLE);

        Snackbar.make(snackBarLayout, "Log-In Success", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LogInPortion.this, LandingPage.class));
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }
}