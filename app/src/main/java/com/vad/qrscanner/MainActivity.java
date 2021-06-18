package com.vad.qrscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vad.qrscanner.GeneratorQr;
import com.vad.qrscanner.R;
import com.vad.qrscanner.fragments.LinksFragmentGeneration;
import com.vad.qrscanner.fragments.LocationFragmentGeneration;
import com.vad.qrscanner.fragments.PhoneFragmentGeneration;
import com.vad.qrscanner.fragments.TextFragmentGeneration;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, new PhoneFragmentGeneration());
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
                    selected = new LocationFragmentGeneration();
                    break;

                case R.id.links_nav:
                    selected = new LinksFragmentGeneration();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, selected);
            return true;
        }
    };


}