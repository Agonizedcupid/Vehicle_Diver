package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import static com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity.resetDatabaseIcon;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionStringFragment extends Fragment implements View.OnClickListener {

    //UI element
    private View root;
    private ConstraintLayout snackBarLayout;
    private EditText ipEditText, emailAddress, companyName;
    private TextView deviceId, regKey, submitRegBtn;

    private ProgressBar progressBar;

    //Database instance:
    private DatabaseAdapter databaseAdapter;

    //Variables:
    private String deviceUniqueId = "";

    public ConnectionStringFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_connection_string, container, false);
        databaseAdapter = new DatabaseAdapter(requireContext());
        initUI();
        return root;
    }

    private void initUI() {
        progressBar = root.findViewById(R.id.progressbar);

        snackBarLayout = root.findViewById(R.id.snackBarLayout);
        resetDatabaseIcon.setVisibility(View.VISIBLE);
        resetDatabaseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(snackBarLayout, getResources().getString(R.string.resetDatabaseTitle), Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resetDatabase();
                            }
                        }).show();
            }
        });

        ipEditText = root.findViewById(R.id.ipEditText);
        emailAddress = root.findViewById(R.id.emailEditText);
        companyName = root.findViewById(R.id.companyNameEditText);
        deviceId = root.findViewById(R.id.deviceIdTextView);
        regKey = root.findViewById(R.id.registrationKeyTextView);
        submitRegBtn = root.findViewById(R.id.submitRegBtn);
        submitRegBtn.setOnClickListener(this);

        initAction();

    }

    //update the field:
    @Override
    public void onResume() {
        progressBar.setVisibility(View.VISIBLE);
        loadIp();
        super.onResume();
    }

    private void loadIp() {
        List<IpModel> list = databaseAdapter.getServerIpModel();
        IpModel model = list.get(0);
        ipEditText.setText(model.getServerIp(), TextView.BufferType.EDITABLE);
        emailAddress.setText(model.getEmailAddress(), TextView.BufferType.EDITABLE);
        companyName.setText(model.getCompanyName(), TextView.BufferType.EDITABLE);
        deviceId.setText(model.getDeviceId());
        regKey.setText(model.getRegKey());
        progressBar.setVisibility(View.GONE);
    }

    private void initAction() {
        deviceUniqueId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId.setText(deviceUniqueId);
    }

    //Reset the database
    private void resetDatabase() {
        Toast.makeText(requireContext(), "Database Reset", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submitRegBtn:
                progressBar.setVisibility(View.VISIBLE);
                submitInformation();
                break;
        }
    }

    private void submitInformation() {
        //First do the validation:
        String ip = ipEditText.getText().toString().trim();
        if (TextUtils.isEmpty(ip)) {
            ipEditText.setError(getResources().getString(R.string.ipInputError));
            ipEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!ip.endsWith("/")) {
            Toast.makeText(requireContext(), "Ip should end with a / (Forward slash)", Toast.LENGTH_SHORT).show();
            ipEditText.setError(getResources().getString(R.string.ipInputError));
            ipEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!isValid(emailAddress.getText().toString().trim())) {
            emailAddress.setError(getResources().getString(R.string.invalidEmail));
            emailAddress.requestFocus();
            return;
        }

        long id = databaseAdapter.insertServer(ip, emailAddress.getText().toString(), companyName.getText().toString(),
                deviceUniqueId, "" + regKey.getText().toString());

        if (id > 0) {
            Snackbar.make(snackBarLayout, getResources().getString(R.string.ipSaved), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(snackBarLayout, getResources().getString(R.string.unableToSave), Snackbar.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValid(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.find();
    }
}