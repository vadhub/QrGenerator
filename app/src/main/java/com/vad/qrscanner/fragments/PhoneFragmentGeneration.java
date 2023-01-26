package com.vad.qrscanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vad.qrscanner.domain.CheckEmptyText;
import com.vad.qrscanner.R;
import com.vad.qrscanner.navigation.CustomAction;
import com.vad.qrscanner.navigation.HasCustomAction;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;

public class PhoneFragmentGeneration extends Fragment implements HasCustomTitle, HasCustomAction {

    private EditText editTextPhone;
    private EditText editTextName;
    private EditText editTextLastname;
    private String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_generation, container, false);
        editTextPhone = (EditText) v.findViewById(R.id.editTextPhone);
        editTextName = (EditText) v.findViewById(R.id.editTextTextName);
        editTextLastname = (EditText) v.findViewById(R.id.editTextTextLastname);
        return v;
    }

    @NonNull
    @Override
    public CustomAction setCustomAction(@NonNull Navigator navigator) {
        return new CustomAction(R.drawable.ic_baseline_done_24, () -> {
            CheckEmptyText.Companion.check(
                    new EditText[]{editTextPhone, editTextName, editTextLastname}, () -> {
                        str =
                                getString(R.string.phone_number_text) +
                                        editTextPhone.getText().toString() +
                                        getString(R.string.name_text) +
                                        editTextName.getText().toString()+
                                        getString(R.string.lastname_text) +
                                        editTextLastname.getText().toString();
                    });

            Bundle bundle = new Bundle();
            bundle.putString("result_text", str);
            Fragment fragment = new ResultQrFragment();
            fragment.setArguments(bundle);
            navigator.startFragment(fragment);
        });
    }

    @Override
    public int getTitle() {
        return R.string.fragment_contact;
    }
}