package ebtkar.app.oilex2;
//ebtkar.app.oilex

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ebtkar.app.oilex2.activities.MainActivity;
import ebtkar.app.oilex2.activities.RateUserActivity;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        Locale locale = new Locale(SharedPrefManager.getInstance(this).getSavedLanguage());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //  getSupportActionBar().setTitle("Map Location Activity");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);


    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        init(0);
        mGoogleMap = googleMap;
        //  mGoogleMap.setMapType(GoogleMap.MAP_TYPE);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getString(R.string.cueent_pos));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                //   mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                my_start = latLng;
                if (my_start != null && dest != null) {
                    Toast.makeText(MapsActivity.this, R.string.lookiung_for, Toast.LENGTH_SHORT).show();
                    new LongOperation().execute();
                }


            }
        }
    };

    LatLng my_start;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    void approve(final String time, final int t, final boolean flag) {
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        findViewById(R.id.loading_app).setVisibility(View.VISIBLE);
        HashMap args = new HashMap<String, String>();
        args.put("flag", flag ? "true" : "false");
        args.put("order_id", getIntent().getStringExtra("order_id"));
        args.put("time", time);
        args.put("agent_id", SharedPrefManager.getInstance(this).getUser().getId());
        findViewById(R.id.approve).setEnabled(false);
        findViewById(R.id.reject).setEnabled(false);
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "Approval_or_reject", args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if (s.equals("")) {
                            if (t < 20) approve(time, t + 1, flag);
                            else {
                                Toast.makeText(MapsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            try {


                                if (flag) {
                                    JSONObject array = new JSONObject(s);
                                    Toast.makeText(MapsActivity.this, array.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MapsActivity.this, FinishedOrdersActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MapsActivity.this, R.string.skja, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    LatLng dest;

    void init(final int t) {
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        HashMap args = new HashMap<String, String>();
        args.put("order_id", getIntent().getStringExtra("order_id"));

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_order_descr", args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if (s.equals("")) {
                            if (t < 20) init(t + 1);
                            else {
                                Toast.makeText(MapsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            try {
                                JSONObject array = new JSONObject(s);
                                dest = new LatLng(
                                        array.getDouble("latet"),
                                        array.getDouble("longat")

                                );
                                LatLng latLng = dest;
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(getString(R.string.cus_loc));
                                mGoogleMap.addMarker(markerOptions);
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                                ((TextView) findViewById(R.id.customer_name))
                                        .setText(getString(R.string.cus_name) + array.getString("customer_name"));

                                ((TextView) findViewById(R.id.oil_name))
                                        .setText(getString(R.string.oil_brand) + array.getString("oil"));
                                ((TextView) findViewById(R.id.price))
                                        .setText(getString(R.string.price) + array.getInt("price") + " ");
                                ((TextView) findViewById(R.id.phone))
                                        .setText(getString(R.string.phone) + array.getString("phone"));


                                findViewById(R.id.heart).setVisibility(View.VISIBLE);
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                findViewById(R.id.reject).setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                approve("0", 0, false);
                                            }
                                        });

                                findViewById(R.id.approve)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                                builder.setTitle(R.string.how_much_time);

// Set up the input
                                                final EditText input = new EditText(MapsActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                builder.setView(input);

// Set up the buttons
                                                builder.setPositiveButton(getString(R.string.ok),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String m_Text = input.getText().toString();
                                                                if (m_Text.equals("")) {
                                                                    Toast.makeText(MapsActivity.this,
                                                                            R.string.enter_valid_time, Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                int x= Integer.parseInt(m_Text);
                                                                if(x>60){
                                                                    Toast.makeText(MapsActivity.this, R.string.max_time, Toast.LENGTH_SHORT).show();
                                                               return;
                                                                }
                                                                approve(m_Text, 0, true);
                                                            }
                                                        });
                                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });

                                                builder.show();

                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, R.string.no_permissions_granted, Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        List<LatLng> path = new ArrayList<>();

        public LongOperation() {
            // mGoogleMap.addMarker(new MarkerOptions().position(dest).title(getString(R.string.cus_loc)));
            //Execute Directions API request
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
            req = DirectionsApi.getDirections(context, my_start.latitude + "," + my_start.longitude
                    , dest.latitude + "," + dest.longitude);
        }

        DirectionsApiRequest req;
        DirectionsResult res;

        @Override
        protected String doInBackground(String... params) {

            try {
                res = req.await();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {


            try {

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e("kaka", ex.getLocalizedMessage());
            }

            //Draw the polyline
            if (path.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                mGoogleMap.addPolyline(opts);
            }

            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_start, 6));


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }
    }

}