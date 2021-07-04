package com.vad.qrscanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.vad.qrscanner.R;
import com.vad.qrscanner.showcontentqr.Showable;

public class ScannerQRFragment extends Fragment {

    private Showable show;
    private TextView textForResult;

    public ScannerQRFragment(Showable show) {
        this.show = show;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scanner_q_r, container, false);
        textForResult = (TextView) v.findViewById(R.id.textViewForResult);
        textForResult.setText(show.showContent());
        return v;
    }

    private void getQrScann(){
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(ScannerQRFragment.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.initiateScan();
    }
}