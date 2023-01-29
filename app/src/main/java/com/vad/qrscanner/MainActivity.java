package com.vad.qrscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.vad.qrscanner.domain.FileUtils;
import com.vad.qrscanner.domain.QRTools;
import com.vad.qrscanner.fragments.LocationFragmentGeneration;
import com.vad.qrscanner.fragments.PhoneFragmentGeneration;
import com.vad.qrscanner.fragments.ResultFragment;
import com.vad.qrscanner.fragments.ResultQrFragment;
import com.vad.qrscanner.fragments.TextFragmentGeneration;
import com.vad.qrscanner.navigation.CustomAction;
import com.vad.qrscanner.navigation.HasCustomAction;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements Navigator {

    private LocationManager mLocationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Toolbar toolbar;

    public static final int REQUEST_CHECK_SETTINGS = 15232;
    public static final int LOCATION_PERMISSION_CODE = 15032;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        BannerAdView mBanner = (BannerAdView) findViewById(R.id.adView);
        mBanner.setAdUnitId("R-M-2167912-1");
        mBanner.setAdSize(AdSize.stickySize(AdSize.FULL_SCREEN.getWidth()));
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);

        Common common = Common.getInstance();
        common.mGetContent = mGetContent;

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        navigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentListener, false);

        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setOrientationLocked(true);
        scanOptions.setPrompt("");
        scanOptions.setCaptureActivity(CustomScannerActivity.class);
        barcodeLauncher.launch(scanOptions);
    }

    private void checkPermission() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, getResources().getString(R.string.access_location), Toast.LENGTH_SHORT).show();
            requestLocationPermission();
        } else {
            requestGPSSettings();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOnUI();
        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    private final FragmentManager.FragmentLifecycleCallbacks fragmentListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            updateOnUI();
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            updateOnUI();
        }
    };


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_replacer);
    }

    private void updateOnUI() {
        Fragment fragment = getCurrentFragment();

        if (fragment instanceof HasCustomTitle) {
            toolbar.setTitle(((HasCustomTitle) fragment).getTitle());
        }

        if (fragment instanceof HasCustomAction) {
            createCustomToolbarAction(((HasCustomAction)fragment).setCustomAction(this));
        } else {
            toolbar.getMenu().clear();
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("ResourceType")
    private void createCustomToolbarAction(CustomAction customActionFragment) {
        toolbar.getMenu().clear();

        Drawable iconDrawable = DrawableCompat.wrap(Objects.requireNonNull(ContextCompat.getDrawable(this, customActionFragment.getIcon())));
        iconDrawable.setTint(Color.WHITE);
        MenuItem menuItem = toolbar.getMenu().add("");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(iconDrawable);
        menuItem.setOnMenuItemClickListener(menuItem1 -> {
            customActionFragment.getAction().run();
            return true;
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    startResult(result.getContents());
                }
            });

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    String content = QRTools.decodeQRImage(FileUtils.getPath(uri, this));
                    startResult(content);
                }
            });

    @SuppressLint("SuspiciousIndentation")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {

        Fragment selected = null;

        switch (item.getItemId()) {
            case R.id.phone_nav:
                selected = new PhoneFragmentGeneration();
                break;

            case R.id.text_nav:
                selected = new TextFragmentGeneration();
                break;

            case R.id.coord_nav:
                selected = new LocationFragmentGeneration();
                checkPermission();
                break;

            case R.id.scanner_nav:
                ScanOptions scanOptions = new ScanOptions();
                scanOptions.setOrientationLocked(true);
                scanOptions.setPrompt("");
                scanOptions.setCaptureActivity(CustomScannerActivity.class);
                barcodeLauncher.launch(scanOptions);
                break;
        }

        if (selected != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, selected).commit();
        return true;
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestGPSSettings();
            } else {
                Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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
                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("Applicationsett", e.toString());
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Toast.makeText(MainActivity.this, getString(R.string.unable_gsp), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
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

    private void startResult(String content) {
        Bundle args = new Bundle();
        args.putString("content", content);
        Fragment fragmentResult = new ResultFragment();
        fragmentResult.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, fragmentResult).commit();
    }

    private void startResultQR(String content) {
        Bundle args = new Bundle();
        args.putString("result_text", content);
        Fragment fragmentResult = new ResultQrFragment();
        fragmentResult.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, fragmentResult).commit();
    }


    @Override
    public void startFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.frame_replacer, fragment)
                .commit();
    }
}