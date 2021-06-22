package com.vad.qrscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vad.qrscanner.fragments.PhoneFragmentGeneration;
import com.vad.qrscanner.fragments.TextFragmentGeneration;


public class MainActivity extends AppCompatActivity{

    private BottomNavigationView navigationView;

    private LocationManager mLocationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int LOCATION_PERMISSION_CODE = 15032;
    public static final int REQUEST_CHECK_SETTINGS = 15232;

    private void checkPermission() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Access location", Toast.LENGTH_SHORT).show();
            requestLocationPermission();
        } else {
            requestGPSSettings();
        }

    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        navigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, new PhoneFragmentGeneration()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selected = null;

            switch (item.getItemId()){
                case R.id.phone_nav:
                    selected = new PhoneFragmentGeneration();
                    break;

                case R.id.text_nav:
                    selected = new TextFragmentGeneration();
                    break;

                case R.id.coord_nav:
                    checkPermission();
                    break;
            }

            if(selected!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, selected).commit();
            return true;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestGPSSettings();
                Toast.makeText(this, "onRequest", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "onResult", Toast.LENGTH_SHORT).show();
                getLastLocation();
            }
        }
    }

    private void requestGPSSettings() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(MainActivity.this, "GPS unable", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        Toast.makeText(MainActivity.this, "" + lat + " " + lon, Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @SuppressLint("MissingPermission")
                                    @Override
                                    public void run() {

                                        LocationCallback callback = new LocationCallback() {
                                            @Override
                                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                                super.onLocationResult(locationResult);

                                                Location loc = locationResult.getLastLocation();
                                                Toast.makeText(MainActivity.this, "" + loc.getLatitude() + " " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                                                fusedLocationProviderClient.removeLocationUpdates(this);
                                            }
                                        };
                                        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), callback, Looper.myLooper());
                                    }
                                });
                            }
                        }).start();
                    }
                }
            });
        }else{
            checkPermission();
        }

    }

    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        return locationRequest;
    }


}