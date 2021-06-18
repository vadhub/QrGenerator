package com.vad.qrscanner.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vad.qrscanner.GeneratorQr;
import com.vad.qrscanner.R;


public class LinksFragmentGeneration extends Fragment {

    private Button btnGenerate;
    private EditText editTextLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_links_generation, container, false);
        editTextLink = (EditText) v.findViewById(R.id.editTextLink);
        btnGenerate = (Button) v.findViewById(R.id.btnGenerateLink);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editTextLink.getText().toString();
                Bitmap bitmap = GeneratorQr.generate(str);
                
            }
        });
        return v;
    }
}