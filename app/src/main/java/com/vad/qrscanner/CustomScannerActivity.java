package com.vad.qrscanner;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;

public class CustomScannerActivity extends AppCompatActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageView flashlight;
    private Common common;
    private boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        BannerAdView mBanner = (BannerAdView) findViewById(R.id.adViewQR);
        mBanner.setAdUnitId("R-M-2167912-2");
        mBanner.setAdSize(AdSize.stickySize(AdSize.FULL_SCREEN.getWidth()));
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);

        common = Common.getInstance();

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        flashlight = findViewById(R.id.flashlight);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);
        capture.decode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void chooseImage(View view) {
        common.start();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void flashlight(View view) {
        if (isOn) {
            flashlight.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_flash_off_24));
            barcodeScannerView.setTorchOn();
            isOn = false;
        } else {
            flashlight.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_flash_on_24));
            barcodeScannerView.setTorchOff();
            isOn = true;
        }
    }
}