package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import static com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity.resetDatabaseIcon;
import static com.regin.reginald.vehicleanddrivers.PrinterFunctionActivity.GET;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bxl.config.util.BXLBluetoothLE;
import com.bxl.mupdf.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.BluetoothListAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.LandingPage;
import com.regin.reginald.vehicleanddrivers.PrinterFunctionActivity;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jpos.JposException;

public class SetupFragment extends Fragment implements View.OnClickListener {

    private View root;

    private ProgressBar progressBar;
    private RadioButton bluetoothRadioBtn, bluetoothLowEnergyBtn;
    private Spinner printerModelSpinner;
    private RecyclerView bluetoothList;
    private CardView setUpIpCard, registerDeviceCard, openPrinterCard, groupSyncCard;
    private List<String> listOfBluetoothDevices = new ArrayList<>();

    private String serverIp, sId;

    BluetoothListAdapter adapter;

    //Local database:
    private DatabaseAdapter databaseAdapter;

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
        databaseAdapter = new DatabaseAdapter(requireContext());
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

        progressBar = root.findViewById(R.id.progressbar);

        loadValueFromLocalStorage();

        initAction();
    }

    private void loadValueFromLocalStorage() {
        List<IpModel> list = databaseAdapter.getServerIpModel();
        if (list.size() > 0) {
            serverIp = list.get(0).getServerIp();
            sId = list.get(0).getDeviceId();
        } else {

        }

    }

    private void initAction() {
        adapter = new BluetoothListAdapter(requireContext(), listOfBluetoothDevices);
        bluetoothList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

            case R.id.bluetoothRadioBtn:
                searchForBluetoothDevices();
                break;

            case R.id.registerDeviceCard:
                progressBar.setVisibility(View.VISIBLE);
                registerDevice();
                break;
        }
    }

    private void registerDevice() {
        new RegisterTheDevice().execute(serverIp + "RegisterDevice.php?key=" + sId);
    }

    private void searchForBluetoothDevices() {
        BXLBluetoothLE.setBLEDeviceSearchOption(5, 1);
        //Search on Background thread:
        new SearchBluetoothDevices().execute();

    }


    //For Registering the device:
    private class RegisterTheDevice extends AsyncTask<String, Void, String> {

        private String customerOrders;
        private int len;

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(requireContext(), "Received!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            progressBar.setVisibility(View.GONE);
            if (len > 0) {
                //lastmess
                //progressDoalog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Please talk to your manager about this ID.")
                        .setMessage("ID :" + customerOrders)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                Intent main = new Intent( PrinterFunctionActivity.this, LandingPage.class);
//                                startActivity(main);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    //For searching the Bluetooth printer available nearby
    public class SearchBluetoothDevices extends AsyncTask<Integer, Void, Set<BluetoothDevice>> {

        private String message;

        @Override
        protected void onPostExecute(Set<BluetoothDevice> bluetoothDevices) {
            listOfBluetoothDevices.clear();

            Log.d("BLUETOOTH_TEST", "onPostExecute: " + listOfBluetoothDevices.size());

            if (bluetoothDevices.size() > 0) {
                for (BluetoothDevice device : bluetoothDevices) {
                    //bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    listOfBluetoothDevices.add(device.getName() + " - " + device.getAddress());
                }
                Toast.makeText(requireContext(), "" + listOfBluetoothDevices.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Can't found BLE devices. ", Toast.LENGTH_SHORT).show();
            }

            if (listOfBluetoothDevices != null) {
                adapter.notifyDataSetChanged();
            }

            if (message != null) {
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Set<BluetoothDevice> doInBackground(Integer... params) {

            Log.d("BLUETOOTH_TEST", "onPostExecute: " + listOfBluetoothDevices.size());
            try {
                return BXLBluetoothLE.getBLEPrinters(requireActivity(), BXLBluetoothLE.SEARCH_BLE_ALWAYS);
            } catch (NumberFormatException | JposException e) {
                message = e.getMessage();
                return new HashSet<>();
            }
        }
    }
}