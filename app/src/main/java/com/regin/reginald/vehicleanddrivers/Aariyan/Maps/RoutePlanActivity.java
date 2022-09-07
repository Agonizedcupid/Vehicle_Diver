package com.regin.reginald.vehicleanddrivers.Aariyan.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itextpdf.text.pdf.parser.Line;
import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.RouteAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.WithoutCordinateAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DummyLocations;
import com.regin.reginald.vehicleanddrivers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Route;

public class RoutePlanActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;

    private GoogleApiClient mGoogleApiClient;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    List<Orders> listOfHeaderLocations = new ArrayList<>();

    private RecyclerView listOfLocationRecyclerView,rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initUI();
    }

    private void initUI() {
        listOfLocationRecyclerView = findViewById(R.id.rec);
        listOfLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        rec = findViewById(R.id.noCoordinatesRec);
        rec.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    private void loadLocationsOnMaps(List<Orders> listOfHeaderLocations, GoogleMap mMap) {
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        for (Orders orders : listOfHeaderLocations) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(orders.getLatitude()),
                    Double.parseDouble(orders.getLongitude()))).title(orders.getCustomerPastelCode()).icon(bitmapDescriptor));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(orders.getLatitude()),
                    Double.parseDouble(orders.getLongitude()))));
        }

    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupGoogleMapScreenSettings(mMap);
        checkLocation();
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                listOfHeaderLocations.clear();
//                listOfHeaderLocations = new DatabaseAdapter(RoutePlanActivity.this).returnOrderHeaders();
//                if (!executorService.isShutdown()) {
//                    executorService.shutdown();
//                }
//                if (listOfHeaderLocations.size() > 0) {
//                    loadLocationsOnMaps(listOfHeaderLocations, mMap);
//                    Log.d("LOCATION_TESTING", "run: "+listOfHeaderLocations.get(0).getLatitude()+":"+
//                            listOfHeaderLocations.get(0).getLongitude());
//                } else {
//                    Log.d("LOCATION_TESTING", "run: Empty");
//                }
//            }
//        });


        //Workable code:
//        listOfHeaderLocations = new DatabaseAdapter(RoutePlanActivity.this).returnOrderHeaders();
//        if (listOfHeaderLocations.size() > 0) {
//            loadLocationsOnMaps(listOfHeaderLocations, mMap);
//            Log.d("LOCATION_TESTING", "run: "+listOfHeaderLocations.get(0).getLatitude()+":"+
//                    listOfHeaderLocations.get(0).getLongitude());
//        } else {
//            Log.d("LOCATION_TESTING", "run: Empty");
//        }

        //Test with multiple location:
        DummyLocations l1 = new DummyLocations(-24.323718, 29.465894,"A");
        DummyLocations l2 = new DummyLocations(-24.324559, 29.467568,"B");
        DummyLocations l3 = new DummyLocations(-24.323950, 29.465194,"C");
        DummyLocations l4 = new DummyLocations(-24.323312, 29.466906,"D");
        DummyLocations l5 = new DummyLocations(-24.323359, 29.467710,"E");
        List<DummyLocations> d = new ArrayList<>();
        d.add(l1);
        d.add(l2);
        d.add(l3);
        d.add(l4);
        d.add(l5);

        if (d.size() > 0) {
            OnMaps(d, mMap);
        } else {
            Log.d("LOCATION_TESTING", "run: Empty");
        }

        loadDataOnList(d);

        DummyLocations l10 = new DummyLocations(0.0, 0.0,"z");
        DummyLocations l11 = new DummyLocations(0.0, 0.0,"y");
        DummyLocations l12 = new DummyLocations(0.0, 0.0,"X");
        DummyLocations l13 = new DummyLocations(0.0, 0.0,"W");
        List<DummyLocations> d2 = new ArrayList<>();
        d2.add(l10);
        d2.add(l11);
        d2.add(l12);
        d2.add(l13);
        loadWithoutCoordinate(d2);

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void loadWithoutCoordinate(List<DummyLocations> d2) {
        WithoutCordinateAdapter adapter = new WithoutCordinateAdapter(this,d2);
        rec.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadDataOnList(List<DummyLocations> d) {
        RouteAdapter adapter = new RouteAdapter(this,d);
        listOfLocationRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void OnMaps(List<DummyLocations> listOfHeaderLocations, GoogleMap mMap) {
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        for (DummyLocations orders : listOfHeaderLocations) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(orders.getLat(),orders.getLng())).icon(bitmapDescriptor));
            // below lin is use to zoom our camera on map.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(orders.getLat(),orders.getLng())));
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}