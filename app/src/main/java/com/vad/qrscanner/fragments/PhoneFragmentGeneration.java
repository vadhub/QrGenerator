package com.vad.qrscanner.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vad.qrscanner.GeneratorQr;
import com.vad.qrscanner.R;
import com.vad.qrscanner.pojos.Contact;
import com.vad.qrscanner.result.ResultQrActivity;

public class PhoneFragmentGeneration extends Fragment {

    private EditText editTextPhone;
    private EditText editTextName;
    private EditText editTextLastname;
    private Button btnGenerationContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_generation, container, false);
        editTextPhone = (EditText) v.findViewById(R.id.editTextPhone);
        editTextName = (EditText) v.findViewById(R.id.editTextTextName);
        editTextLastname = (EditText) v.findViewById(R.id.editTextTextLastname);

        btnGenerationContact = (Button) v.findViewById(R.id.button_generate_phne);

        btnGenerationContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        (!editTextPhone.getText().toString().equals("")&&!editTextName.getText().toString().equals("")&&!editTextLastname.getText().toString().equals(""))&&
                                (!editTextPhone.getText().toString().equals("")&&!editTextName.getText().toString().equals("")||!editTextLastname.getText().toString().equals(""))&&
                                (!editTextPhone.getText().toString().equals("")||!editTextName.getText().toString().equals("")&&!editTextLastname.getText().toString().equals(""))
                ){
                    Contact contact = new Contact(editTextPhone.getText().toString(), editTextName.getText().toString(), editTextLastname.getText().toString());
                    String str = getString(R.string.phone_number_text)+contact.getNumberPhone()+getString(R.string.name_text)+contact.getName()+getString(R.string.lastname_text)+contact.getLastname();
                    Bitmap bitmap = GeneratorQr.generate(getString(R.string.phone_number_text)+contact.getNumberPhone()+getString(R.string.name_text)+contact.getName()+getString(R.string.lastname_text)+contact.getLastname());
                    resultActivityStart(bitmap, str);
                }else{
                    Toast toast =  Toast.makeText(getContext(), R.string.enter_text_pl, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
            }
        });

        return v;
    }

    private void resultActivityStart(Bitmap bitmap, String contact){
        Intent intentRes = new Intent(getContext(), ResultQrActivity.class);
        intentRes.putExtra("result_qr", bitmap);
        intentRes.putExtra("result_text", contact);
        getContext().startActivity(intentRes);
    }
}