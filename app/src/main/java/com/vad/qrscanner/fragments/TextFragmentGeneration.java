package com.vad.qrscanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vad.qrscanner.R;
import com.vad.qrscanner.result.ResultQrActivity;

public class TextFragmentGeneration extends Fragment {

    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_text_generation, container, false);
        Button btnGenerate = (Button) v.findViewById(R.id.buttonTextGenerate);
        editText = (EditText) v.findViewById(R.id.editTextText);

        btnGenerate.setOnClickListener(view -> {
            String str = editText.getText().toString();

            if(!str.equals("")){
                Intent intentRes = new Intent(getContext(), ResultQrActivity.class);
                intentRes.putExtra("result_text", str);
                container.getContext().startActivity(intentRes);
            }else{
                Toast toast = Toast.makeText(getContext(), getString(R.string.enter_text_pl), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
            }

        });
        return v;
    }
}