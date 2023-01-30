package com.vad.qrscanner.fragments;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vad.qrscanner.MainActivity;
import com.vad.qrscanner.R;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;

import java.util.Objects;


public class LocationFragmentGeneration extends Fragment implements HasCustomTitle  {

    private LocationManager mLocationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private Navigator navigator;
    public static final int REQUEST_CHECK_SETTINGS = 15232;
    public static final int LOCATION_PERMISSION_CODE = 15032;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_generation, container, false);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestGPSSettings();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getLastLocation();
            }
        }
    }

    private void requestGPSSettings() {
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i("", "All location settings are satisfied.");
                    getLastLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                    try {
                        status.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("Applicationsett", e.toString());
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Toast.makeText(getContext(), getString(R.string.unable_gsp), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void checkPermission() {
        if (
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(getContext(), getResources().getString(R.string.access_location), Toast.LENGTH_SHORT).show();
            requestLocationPermission();
        } else {
            requestGPSSettings();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = null;
                    if (task.isSuccessful() && task.getResult() != null) {
                        location = task.getResult();
                    }

                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        startResultQR(lat+", "  +lon);
                    } else {
                        LocationCallback callback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location loc = locationResult.getLastLocation();
                                startResultQR(loc.getLatitude()+", "+ loc.getLongitude());
                                fusedLocationProviderClient.removeLocationUpdates(this);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), callback, Looper.myLooper());
                    }

                }
            });
        } else {
            checkPermission();
        }

    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        return locationRequest;
    }


    private void startResultQR(String content) {
        Bundle args = new Bundle();
        args.putString("result_text", content);
        Fragment fragmentResult = new ResultQrFragment();
        fragmentResult.setArguments(args);
        navigator.startFragment(fragmentResult);
    }


    @Override
    public int getTitle() {
        return R.string.location_title;
    }
}