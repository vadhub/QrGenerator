package com.vad.qrscanner.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vad.qrscanner.GeneratorQr;
import com.vad.qrscanner.R;
import com.vad.qrscanner.result.ResultQrActivity;

public class TextFragmentGeneration extends Fragment {

    private Button btnGenerate;
    private EditText editText;
    private TextView textViewWatcher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_text_generation, container, false);
        btnGenerate = (Button) v.findViewById(R.id.buttonTextGenerate);
        editText = (EditText) v.findViewById(R.id.editTextText);
        textViewWatcher = (TextView) v.findViewById(R.id.textViewWatcher);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();

                if(!editText.getText().toString().equals("")){
                    Bitmap bitmap = GeneratorQr.generate(str);
                    resultActivityStart(bitmap);
                }else{
                    Toast toast =  Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewWatcher.setText(editText.getText().length()+"/200");
                if(editText.getText().length()==200){
                    textViewWatcher.setTextColor(Color.RED);
                }else{
                    textViewWatcher.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
                }
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