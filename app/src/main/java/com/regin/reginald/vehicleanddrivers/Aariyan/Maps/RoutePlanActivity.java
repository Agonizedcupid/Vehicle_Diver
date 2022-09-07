package com.regin.reginald.vehicleanddrivers.Aariyan.Maps;

import static android.graphics.Bitmap.Config.ARGB_8888;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.bxl.mupdf.AsyncTask;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.itextpdf.text.pdf.parser.Line;
import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.RouteAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Adapter.WithoutCordinateAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DummyLocations;
import com.regin.reginald.vehicleanddrivers.R;

import org.json.JSONObject;

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


    List<Orders> listOfHeaders = new ArrayList<>();
    List<Orders> headersWithLocation = new ArrayList<>();
    List<Orders> headersWithoutLocation = new ArrayList<>();

    private RecyclerView listOfLocationRecyclerView, rec;

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

        int count = 0;
        for (Orders orders : listOfHeaderLocations) {
            count ++;
            Bitmap bitmap = makeBitmap(this,""+count);
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(orders.getLatitude()),
                    Double.parseDouble(orders.getLongitude()))).title(orders.getCustomerPastelCode())
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        }

        String url = getMapsApiDirectionsUrl(listOfHeaderLocations);
        Log.d("CHECKING_URL", "onMapReady: " + url);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(listOfHeaderLocations.get(0).getLatitude()),
                        Double.parseDouble(listOfHeaderLocations.get(0).getLongitude())),
                13));

        addMarkerOnLocation(listOfHeaderLocations, mMap);

    }

    private void addMarkerOnLocation(List<Orders> listOfHeaderLocations, GoogleMap mMap) {

        int count = 0;
        if (mMap != null)
            //Toast.makeText(this, "Size: "+listOfHeaderLocations.size(), Toast.LENGTH_SHORT).show();
            for (Orders orders : listOfHeaderLocations) {
                count++;
                Bitmap bitmap = makeBitmap(this,""+count);
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(orders.getLatitude()),
                        Double.parseDouble(orders.getLongitude()))).title(orders.getCustomerPastelCode())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            }
    }

    private String getMapsApiDirectionsUrl(List<Orders> listOfHeaderLocations) {

        // https://maps.googleapis.com/maps/api/directions/json?
        // origin=40.722543,-73.998585&destination=40.7064,-74.0094
        // &waypoints=40.7057,-73.9964
        // &key=AIzaSyC5vAgb-nawregIa5gRRG34wnabasN3blk;

        String originNDest = "origin=" + listOfHeaderLocations.get(0).getLatitude() + "," +
                listOfHeaderLocations.get(0).getLongitude()
                + "&destination=" + listOfHeaderLocations.get(listOfHeaderLocations.size() - 1).getLatitude() + ","
                + listOfHeaderLocations.get(listOfHeaderLocations.size() - 1).getLongitude();
        String wayPoints = "&waypoints=";
        StringBuilder builder = new StringBuilder();
        builder.append(wayPoints);
        for (int i = 0; i < listOfHeaderLocations.size(); i++) {
            Orders loc = listOfHeaderLocations.get(i);
            builder.append(loc.getLatitude() + "," + loc.getLongitude());
            if (i != listOfHeaderLocations.size() - 1) {
                builder.append("|");
            }
        }
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + originNDest + "" + builder.toString() +
                "&key=AIzaSyC5vAgb-nawregIa5gRRG34wnabasN3blk";
        return url;
    }

    public Bitmap makeBitmap(Context context, String text)
    {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.marker);
        bitmap = bitmap.copy(ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED); // Text color
        paint.setTextSize(24 * scale); // Text size
        paint.setShadowLayer(5f, 0f, 1f, Color.WHITE); // Text shadow
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        int x = bitmap.getWidth() - bounds.width(); // 10 for padding from right
        int y = bounds.height();
        canvas.drawText(text, x, y, paint);

        return  bitmap;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
                Log.d("CHECKING_ROUTES", "doInBackground: " + routes.size());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("CHECKING_ROUTES", "ERROR: " + e.getMessage());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
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
//        DummyLocations l1 = new DummyLocations(-24.323718, 29.465894,"A");
//        DummyLocations l2 = new DummyLocations(-24.324559, 29.467568,"B");
//        DummyLocations l3 = new DummyLocations(-24.323950, 29.465194,"C");
//        DummyLocations l4 = new DummyLocations(-24.323312, 29.466906,"D");
//        DummyLocations l5 = new DummyLocations(-24.323359, 29.467710,"E");
//        List<DummyLocations> d = new ArrayList<>();
//        d.add(l1);
//        d.add(l2);
//        d.add(l3);
//        d.add(l4);
//        d.add(l5);

        listOfHeaders = new DatabaseAdapter(RoutePlanActivity.this).returnOrderHeaders();
        if (listOfHeaders.size() > 0) {
            int count = 0;
            for (Orders data : listOfHeaders) {
                if ((!data.getLatitude().isEmpty() && !data.getLatitude().equals("")) &&
                        (!data.getLongitude().isEmpty() && !data.getLongitude().equals(""))) {
                    //Means it has the locations:
                    if (count < 25) {
                        headersWithLocation.add(data);
                    }
                    count++;
                } else {
                    headersWithoutLocation.add(data);
                }
            }
            if (headersWithLocation.size() > 0) {
                loadLocationsOnMaps(headersWithLocation, mMap);
                loadDataOnList(headersWithLocation);
            } else {
                Toast.makeText(this, "Not Enough Data With Location!", Toast.LENGTH_SHORT).show();
            }

            if (headersWithoutLocation.size() > 0) {
                loadWithoutCoordinate(headersWithoutLocation);
            } else {
                Toast.makeText(this, "There has no data without locations!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "There has no location.", Toast.LENGTH_SHORT).show();
        }

//        if (listOfHeaders.contains())
//
//            if (listOfHeaderLocations.size() > 0 && listOfHeaderLocations.size() < 25) {
//                OnMaps(listOfHeaderLocations, mMap);
//            } else {
//                Log.d("LOCATION_TESTING", "run: Empty");
//            }
//
//        loadDataOnList(d);
//
//        DummyLocations l10 = new DummyLocations(0.0, 0.0, "z");
//        DummyLocations l11 = new DummyLocations(0.0, 0.0, "y");
//        DummyLocations l12 = new DummyLocations(0.0, 0.0, "X");
//        DummyLocations l13 = new DummyLocations(0.0, 0.0, "W");
//        List<DummyLocations> d2 = new ArrayList<>();
//        d2.add(l10);
//        d2.add(l11);
//        d2.add(l12);
//        d2.add(l13);
//        loadWithoutCoordinate(d2);

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void loadWithoutCoordinate(List<Orders> d2) {
        WithoutCordinateAdapter adapter = new WithoutCordinateAdapter(this, d2);
        rec.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadDataOnList(List<Orders> d) {
        RouteAdapter adapter = new RouteAdapter(this, d);
        listOfLocationRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void OnMaps(List<DummyLocations> listOfHeaderLocations, GoogleMap mMap) {
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        for (DummyLocations orders : listOfHeaderLocations) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(orders.getLat(), orders.getLng())).icon(bitmapDescriptor));
            // below lin is use to zoom our camera on map.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(orders.getLat(), orders.getLng())));
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