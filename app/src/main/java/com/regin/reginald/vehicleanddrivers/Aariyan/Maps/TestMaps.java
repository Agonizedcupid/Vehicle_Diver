package com.regin.reginald.vehicleanddrivers.Aariyan.Maps;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.bxl.mupdf.AsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.regin.reginald.model.Orders;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.DummyLocations;
import com.regin.reginald.vehicleanddrivers.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMaps extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng origin = new LatLng(40.722543,
            -73.998585);
    //private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng destination = new LatLng(40.7064, -74.0094);

    GoogleMap googleMap;
    final String TAG = "PathGoogleMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMaps) {
        this.googleMap = googleMaps;

        ///////////////////////////////////////////////////////////////////////////////////////
//        MarkerOptions options = new MarkerOptions();
//        options.position(origin);
//        options.position(BROOKLYN_BRIDGE);
//        options.position(destination);

        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        DummyLocations l1 = new DummyLocations(40.722543, -73.998585, "A");
        DummyLocations l2 = new DummyLocations(40.7057, -73.9964, "B");
        DummyLocations l3 = new DummyLocations(40.705147, -74.009969, "C");
        DummyLocations l4 = new DummyLocations(40.706140, -74.006074, "D");
        DummyLocations l5 = new DummyLocations(40.707701, -74.008273, "E");
        List<DummyLocations> d = new ArrayList<>();
        d.add(l1);
        d.add(l2);
        d.add(l3);
        d.add(l4);
        d.add(l5);

        for (DummyLocations orders : d) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(orders.getLat(),
                    orders.getLat())).title(orders.getTitle()).icon(bitmapDescriptor));
        }

        //googleMap.addMarker(options);
        String url = getMapsApiDirectionsUrl(d);
        Log.d("CHECKING_URL", "onMapReady: " + url);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.722543, -73.998585),
                13));
        addMarkers(d);


        //////////////////////////////////////////////////////////////////////////////////////

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String getMapsApiDirectionsUrl(List<DummyLocations> d) {

        // https://maps.googleapis.com/maps/api/directions/json?
        // origin=40.722543,-73.998585&destination=40.7064,-74.0094
        // &waypoints=40.7057,-73.9964
        // &key=AIzaSyC5vAgb-nawregIa5gRRG34wnabasN3blk;


        String originNDest = "origin=" + d.get(0).getLat() + "," + d.get(0).getLng() + "&destination=" + d.get(d.size() - 1).getLat() + "," + d.get(d.size() - 1).getLng();
        String wayPoints = "&waypoints=";
        StringBuilder builder = new StringBuilder();
        builder.append(wayPoints);
        for (int i = 0; i < d.size(); i++) {
            DummyLocations loc = d.get(i);
            builder.append(loc.getLat() + "," + loc.getLng());
            if (i != d.size() - 1) {
                builder.append("|");
            }
        }
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + originNDest + "" + builder.toString() +
                "&key=AIzaSyC5vAgb-nawregIa5gRRG34wnabasN3blk";
        return url;
    }

    private void addMarkers(List<DummyLocations> d) {
        if (googleMap != null) {
//            googleMap.addMarker(new MarkerOptions().position(BROOKLYN_BRIDGE)
//                    .title("First Point"));
//            googleMap.addMarker(new MarkerOptions().position(destination)
//                    .title("Second Point"));
//            googleMap.addMarker(new MarkerOptions().position(origin)
//                    .title("Third Point"));

            for (DummyLocations loc : d) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLat(), loc.getLng()))
                        .title(loc.getTitle()));
            }
        }
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

            googleMap.addPolyline(polyLineOptions);
        }
    }
}