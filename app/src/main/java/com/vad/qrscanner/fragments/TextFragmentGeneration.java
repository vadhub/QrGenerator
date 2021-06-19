package com.vad.qrscanner.fragments;

import android.content.Intent;
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
import com.vad.qrscanner.result.ResultQrActivity;

public class TextFragmentGeneration extends Fragment {

    private Button btnGenerate;
    private EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_text_generation, container, false);
        btnGenerate = (Button) v.findViewById(R.id.buttonTextGenerate);
        editText = (EditText) v.findViewById(R.id.editTextText);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                Bitmap bitmap = GeneratorQr.generate(str);
                resultActivityStart(bitmap);
            }
        });
        return v;
    }

    private void resultActivityStart(Bitmap bitmap){
        Intent intentRes = new Intent(getContext(), ResultQrActivity.class);
        intentRes.putExtra("result_qr", bitmap);
        getContext().startActivity(intentRes);
    }
}