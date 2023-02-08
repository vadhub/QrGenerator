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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vad.qrscanner.MainActivity;
import com.vad.qrscanner.R;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;

import java.util.Objects;
import java.util.concurrent.Executor;


public class LocationFragmentGeneration extends Fragment implements HasCustomTitle  {

    private FusedLocationProviderClient fusedLocationClient;
    private Navigator navigator;
    public static final int GPS_PERMISSION = 15232;
    public static final int LOCATION_PERMISSION_CODE = 15032;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            display();

        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_PERMISSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_generation, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GPS_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                display();
            } else {
                Toast.makeText(getContext(), "Gps not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void display() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();
        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());

        Task<LocationSettingsResponse> taskCheckLocationSettings = settingsClient.checkLocationSettings(
                locationSettingsRequestBuilder
                        .addLocationRequest(locationRequest)
                        .setAlwaysShow(true)
                        .build()
        );

        taskCheckLocationSettings.addOnFailureListener(requireActivity(), e -> {
            if (e instanceof ResolvableApiException){
                try {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    resolvableApiException.startResolutionForResult(requireActivity(),
                            LOCATION_PERMISSION_CODE);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                    Toast.makeText(requireContext(), "not available get GPS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = null;
            if (task.isSuccessful() && task.getResult() != null) {
                location = task.getResult();
                startResultQR(location.getLatitude()+", "+ location.getLongitude());
            }

            if (location == null) {
                LocationCallback callback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location loc = locationResult.getLastLocation();
                        startResultQR(loc.getLatitude() + ", "+ loc.getLongitude());
                        fusedLocationClient.removeLocationUpdates(this);
                    }
                };
                fusedLocationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper());
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "not available get GPS -", Toast.LENGTH_SHORT).show();
        });

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