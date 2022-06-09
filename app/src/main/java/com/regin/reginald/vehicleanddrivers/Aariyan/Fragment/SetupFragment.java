package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import static com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity.resetDatabaseIcon;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity;
import com.regin.reginald.vehicleanddrivers.R;

public class SetupFragment extends Fragment implements View.OnClickListener {

    private View root;

    private RadioButton bluetoothRadioBtn, bluetoothLowEnergyBtn;
    private Spinner printerModelSpinner;
    private RecyclerView bluetoothList;
    private CardView setUpIpCard, registerDeviceCard, openPrinterCard, groupSyncCard;

    public SetupFragment() {
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
        root = inflater.inflate(R.layout.fragment_setup, container, false);

        initUI();
        return root;
    }

    private void initUI() {
        bluetoothRadioBtn = root.findViewById(R.id.bluetoothRadioBtn);
        bluetoothRadioBtn.setOnClickListener(this);
        bluetoothLowEnergyBtn = root.findViewById(R.id.bluetoothLowEnergyRadioBtn);
        bluetoothLowEnergyBtn.setOnClickListener(this);
        printerModelSpinner = root.findViewById(R.id.printerModelSpinner);
        bluetoothList = root.findViewById(R.id.bluetoothListRecyclerView);
        bluetoothList.setLayoutManager(new LinearLayoutManager(requireContext()));
        setUpIpCard = root.findViewById(R.id.setUpIpCard);
        setUpIpCard.setOnClickListener(this);
        registerDeviceCard = root.findViewById(R.id.registerDeviceCard);
        registerDeviceCard.setOnClickListener(this);
        openPrinterCard = root.findViewById(R.id.printerOpenCard);
        openPrinterCard.setOnClickListener(this);
        groupSyncCard = root.findViewById(R.id.syncGroupCard);
        groupSyncCard.setOnClickListener(this);

        initAction();
    }

    private void initAction() {

    }

    @Override
    public void onResume() {
        resetDatabaseIcon.setVisibility(View.GONE);
        super.onResume();
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
            case R.id.setUpIpCard:
                requireActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.container, new ConnectionStringFragment())
                        .addToBackStack(null).commit();
                break;
        }
    }
}