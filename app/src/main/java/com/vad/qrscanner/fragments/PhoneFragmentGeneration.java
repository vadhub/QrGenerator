package com.vad.qrscanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.vad.qrscanner.CheckEmptyText;
import com.vad.qrscanner.R;
import com.vad.qrscanner.result.ResultQrActivity;

public class PhoneFragmentGeneration extends Fragment {

    private EditText editTextPhone;
    private EditText editTextName;
    private EditText editTextLastname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_generation, container, false);
        editTextPhone = (EditText) v.findViewById(R.id.editTextPhone);
        editTextName = (EditText) v.findViewById(R.id.editTextTextName);
        editTextLastname = (EditText) v.findViewById(R.id.editTextTextLastname);

        Button btnGenerationContact = (Button) v.findViewById(R.id.button_generate_phne);

        btnGenerationContact.setOnClickListener(view -> {
            CheckEmptyText.Companion.check(
                    new EditText[]{editTextPhone, editTextName, editTextLastname}, () -> {
                        String str =
                                getString(R.string.phone_number_text) +
                                        editTextPhone.getText().toString() +
                                        getString(R.string.name_text) +
                                        editTextName.getText().toString()+
                                        getString(R.string.lastname_text) +
                                        editTextLastname.getText().toString();

                        Intent intentRes = new Intent(getContext(), ResultQrActivity.class);
                        intentRes.putExtra("result_text", str);
                        view.getContext().startActivity(intentRes);
                    });
        });

        return v;
    }

}