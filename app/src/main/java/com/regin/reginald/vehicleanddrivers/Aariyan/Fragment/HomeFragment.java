package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.regin.reginald.model.Routes;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.OrdersActivity;
import com.regin.reginald.vehicleanddrivers.Aariyan.Constant.Constant;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetOrderTypeInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.GetRouteInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.IpModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.OrderTypeModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.RouteModel;
import com.regin.reginald.vehicleanddrivers.Aariyan.Networking.NetworkingFeedback;
import com.regin.reginald.vehicleanddrivers.LandingPage;
import com.regin.reginald.vehicleanddrivers.MainActivity;
import com.regin.reginald.vehicleanddrivers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment{

    private TextView getBtn;
    private View root;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;
    String date = "";

    private CardView deliveryCard;
    private TextView dateTextView;

    private ConstraintLayout snackBarLayout;

    private ProgressBar progressBar;
    private DatabaseAdapter databaseAdapter;

    private Spinner routeSpinner, orderTypeSpinner;

    //Member Variable:
    private String subscriberId = "";
    private String serverIp = "";
    private int selectedRoute, selectedOrder;

    private String delivery = "", routeName = "";

    private boolean isRoutesAvailable;

    public HomeFragment() {
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
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        databaseAdapter = new DatabaseAdapter(requireContext());
//        List<IpModel> list = databaseAdapter.getServerIpModel();
//        if (list.size() > 0) {
//            isRoutesAvailable = true;
//            subscriberId = list.get(0).getDeviceId();
//            serverIp = list.get(0).getServerIp();
//        } else {
//            isRoutesAvailable = false;
//            //Snackbar.make(root.findViewById(R.id.snackBarLayouts), "Please complete the setup!", Snackbar.LENGTH_SHORT).show();
//        }
//
//        initUI();
        return root;
    }

//    private void initUI() {
//
//        calendar = Calendar.getInstance();
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        month = calendar.get(Calendar.MONTH);
//        year = calendar.get(Calendar.YEAR);
//        progressBar = root.findViewById(R.id.progressbar);
//        dateTextView = root.findViewById(R.id.dateTextView);
//        routeSpinner = root.findViewById(R.id.routeSpinner);
//        orderTypeSpinner = root.findViewById(R.id.orderTypeSpinner);
//        snackBarLayout = root.findViewById(R.id.snackBarLayouts);
//
//        getBtn = root.findViewById(R.id.getBtn);
//        getBtn.setOnClickListener(this);
//
//        deliveryCard = root.findViewById(R.id.deliverDateCard);
//        deliveryCard.setOnClickListener(this);
//
//        initAction();
//    }
//
//    private void initAction() {
//        checkTheUserIsRegisteredOrNot();
//        if (isRoutesAvailable) {
//            getOrderTypes();
//            getRoutes();
//        } else {
//            //Snackbar.make(getActivity().findViewById(R.id.snackBarLayouts), "Please complete the setup!", Snackbar.LENGTH_SHORT).show();
//        }
//
//        date = "2020 - 6 - 23";
//        //dateTextView.setText(Constant.getTodayDate());
//
//    }
//
//    private void checkTheUserIsRegisteredOrNot() {
//        //new CheckIfRegisteredOrNot().execute(Constant.CHECK_URL + "Registration.php?key=" + subscriberId);
//    }
//
//    private void getRoutes() {
//        progressBar.setVisibility(View.VISIBLE);
//        NetworkingFeedback networkingFeedback = new NetworkingFeedback(databaseAdapter);
//        networkingFeedback.getAvailableRoute(new GetRouteInterface() {
//            @Override
//            public void gotRoute(List<RouteModel> listOfRoutes) {
//                setDataToRouteSpinner(listOfRoutes);
//            }
//
//            @Override
//            public void error(String errorMessage) {
//                Toast.makeText(requireContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
//                //Snackbar.make(snackBarLayout, "Routes: " + errorMessage, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setDataToRouteSpinner(List<RouteModel> routeList) {
//        //Spinner items
//        ArrayAdapter<RouteModel> dataAdapter = new ArrayAdapter<RouteModel>(requireContext(),
//                android.R.layout.simple_spinner_item, routeList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        routeSpinner.setAdapter(dataAdapter);
//        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                //selectedRoute = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
//                selectedRoute = routeList.get(position).getRouteId();
//                routeName = routeList.get(position).getRouteName();
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void setDataToOrderSpinner(List<OrderTypeModel> listOfOrders) {
//        //Spinner items
//        ArrayAdapter<OrderTypeModel> dataAdapter = new ArrayAdapter<OrderTypeModel>(requireContext(),
//                android.R.layout.simple_spinner_item, listOfOrders);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        orderTypeSpinner.setAdapter(dataAdapter);
//        orderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                //selectedRoute = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
//                selectedOrder = listOfOrders.get(position).getOrderTypeId();
//                delivery = listOfOrders.get(position).getOrderType();
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void getOrderTypes() {
//        progressBar.setVisibility(View.VISIBLE);
//        NetworkingFeedback networkingFeedback = new NetworkingFeedback(databaseAdapter);
//        networkingFeedback.getAvailableOrder(new GetOrderTypeInterface() {
//            @Override
//            public void gotOrderType(List<OrderTypeModel> listOfOrders) {
//                setDataToOrderSpinner(listOfOrders);
//            }
//
//            @Override
//            public void error(String errorMessage) {
//                Toast.makeText(requireContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        }, subscriberId);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.getBtn:
//                getTheOrders();
//                break;
//
//            case R.id.deliverDateCard:
//                openDatePicker();
//                break;
//        }
//    }
//
//    private void getTheOrders() {
//        Intent i = new Intent(requireContext(), OrdersActivity.class);
//        i.putExtra("deldate", date);
//        i.putExtra("routes", selectedRoute);
//        i.putExtra("ordertype", selectedOrder);
//        i.putExtra("delivery", delivery);
//        i.putExtra("routeName", routeName);
//        startActivity(i);
//    }
//
//    private void openDatePicker() {
//        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//                //Month
//                int j = i1 + 1;
//
//                //date = i + "-" + j + "-" + i2;
//                //date = i2 + "-" + j + "-" + i;
//                date = i + "-" + j + "-" + i2;
//                //2022-1-15
//                dateTextView.setText(date);
//
//            }
//            //}, day, month, year);
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//
//        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
////        new DatePickerDialog(AddTimeActivity.this, null, calendar.get(Calendar.YEAR),
////                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//
//        datePickerDialog.show();
//    }
//
//
//    /**
//     * Async Operation
//     */
//
//    private class CheckIfRegisteredOrNot extends AsyncTask<String, Void, String> {
//        private String customerOrders = "";
//        private int len;
//
//        @Override
//        protected String doInBackground(String... urls) {
//
//            return GET(urls[0]);
//        }
//
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            //Toast.makeText(getBaseContext(), "Service!", Toast.LENGTH_LONG).show();
//            len = result.length();
//            customerOrders = result;
//            Log.e("len***t", "len**************" + customerOrders);
//            if (len > 0) {
//                try {
//                    //lastmess
//                    //JSONObject data = new JSONObject(customerOrders);
//                    JSONArray BoardInfo = new JSONArray(customerOrders);
//                    Log.e("feeed", "feedback*****************************************" + BoardInfo.length());
//                    for (int j = 0; j < BoardInfo.length(); ++j) {
//                        Log.e("overhere", "//////sludooooooo");
//                        JSONObject BoardDetails = BoardInfo.getJSONObject(j);
//                        String results;
//                        results = BoardDetails.getString("results");
//
//                        if (results.equals("NOT REGISTERED")) {
//                            getBtn.setVisibility(View.GONE);
////                            saveddata.setText(results);
////                            saveddata.setBackgroundColor(Color.RED);
//                            Snackbar.make(snackBarLayout, "Sorry! You aren't Registered yet!", Snackbar.LENGTH_SHORT).show();
//                            Log.e("this is definitely", "not registered!!!!!!!!");
//                        } else {
//                            Log.e("registered", "device!!!!!!!!!!!!!!");
//                            Snackbar.make(snackBarLayout, "Welcome! You're Registered.", Snackbar.LENGTH_SHORT).show();
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    public static String GET(String urlp) {
//        Log.d("TEST_RESULT", "GET: " + urlp);
//        String movieJsonStr = "";
//        String result = "";
//        HttpURLConnection connection = null;
//        BufferedReader bufferedReader = null;
//        InputStream inputStream = null;
//        URL url;
//
//        try {
//            url = new URL(urlp);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            inputStream = connection.getInputStream();
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//            // Initialize a new string buffer object
//            StringBuffer stringBuffer = new StringBuffer();
//
//            String line = "";
//            // Loop through the lines
//            while ((line = bufferedReader.readLine()) != null) {
//                // Append the current line to string buffer
//                stringBuffer.append(line);
//            }
//
//            movieJsonStr = stringBuffer.toString();
//
//        } catch (Throwable e) {
//            Log.e("backgroundtask", "EXCEPTION", e);
//        } finally {
//            connection.disconnect();
//            try {
//
//
//                if (bufferedReader != null)
//                    bufferedReader.close();
//                if (inputStream != null)
//                    inputStream.close();
//            } catch (IOException e) {
//                Log.e("READER.CLOSE()", e.toString());
//            }
//        }
//
//        try {
//            result = movieJsonStr;
//            Log.d("TEST_RESULT", "GET: " + result);
//        } catch (Throwable e) {
//            Log.e("BACKGROUNDTASK", "EXCEPTION FROM jsonParse()", e);
//        }
//        return result;
//    }

}