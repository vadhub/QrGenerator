package com.vad.qrscanner.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vad.qrscanner.R;
import com.vad.qrscanner.navigation.HasCustomTitle;


public class LocationFragmentGeneration extends Fragment implements HasCustomTitle{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_generation, container, false);
    }

    @Override
    public int getTitle() {
        return R.string.location_title;
    }
}