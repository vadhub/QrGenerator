package com.vad.qrscanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.vad.qrscanner.R;

public class ScannerQRFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_scanner_q_r, container, false);
    }

    private void getQrScann(){
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(ScannerQRFragment.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.initiateScan();
    }
}