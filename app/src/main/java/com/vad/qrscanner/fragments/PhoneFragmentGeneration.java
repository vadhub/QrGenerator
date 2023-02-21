package com.vad.qrscanner.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vad.qrscanner.R;
import com.vad.qrscanner.domain.CheckEmptyText;
import com.vad.qrscanner.navigation.CustomAction;
import com.vad.qrscanner.navigation.HasCustomActions;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;

import java.util.ArrayList;
import java.util.List;

public class PhoneFragmentGeneration extends Fragment implements HasCustomTitle, HasCustomActions {

    private EditText editTextPhone;
    private EditText editTextName;
    private EditText editTextOrganization;
    private EditText editTextAddress;
    private EditText editTextEmail;
    private EditText editTextNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_generation, container, false);
        editTextPhone = (EditText) v.findViewById(R.id.editTextPhone);
        editTextName = (EditText) v.findViewById(R.id.editTextTextName);
        editTextOrganization = (EditText) v.findViewById(R.id.editTextOrganization);
        editTextAddress = (EditText) v.findViewById(R.id.editTextAddress);
        editTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
        editTextNotes = (EditText) v.findViewById(R.id.editTextNotes);
        return v;
    }

    @SuppressLint("Range")
    private final ActivityResultLauncher<Void> launcher = registerForActivityResult(new ActivityResultContracts.PickContact(), result -> {
        Cursor phone = getContext().getContentResolver().query(result, null, null, null, null);

        if (phone.moveToFirst()) {
            editTextName.setText(phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            String id = phone.getString(phone.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(phone.getString(phone.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                while (phones.moveToNext()) {
                    editTextPhone.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    editTextEmail.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                    editTextOrganization.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)));
                    editTextNotes.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)));

                    String address = "";
                    address += " " + phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    address += " " + phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    address += " " + phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    address += " " + phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                    editTextAddress.setText(address);
                }
                phones.close();
            }
            phone.close();
        }
    });

    private final ActivityResultLauncher<String> permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            launcher.launch(null);
        } else {
            Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    });

    @NonNull
    @Override
    public List<CustomAction> setCustomAction(@NonNull Navigator navigator) {
        List<CustomAction> customActions = new ArrayList();
        CustomAction contact = new CustomAction(R.drawable.ic_baseline_person_24, () -> {
            permission.launch(Manifest.permission.READ_CONTACTS);
        });

        CustomAction apply = new CustomAction(R.drawable.ic_baseline_done_24, () -> {
            CheckEmptyText.Companion.check(getContext().getString(R.string.required),
                    new EditText[]{editTextPhone, editTextName, editTextOrganization,
                            editTextAddress, editTextEmail, editTextNotes}, () -> {
                        String str =
                                getString(R.string.phone_number_text) +
                                        editTextPhone.getText().toString() +
                                        getString(R.string.name_text) +
                                        editTextName.getText().toString() +", "+
                                        getString(R.string.organization) + ": " +
                                        editTextOrganization.getText().toString()+", " +
                                        getString(R.string.address )+ ": "  +
                                        editTextAddress.getText().toString() +", "+
                                        getString(R.string.email) + ": " +
                                        editTextEmail.getText().toString() +", "+
                                        getString(R.string.notes) + ": "  +
                                        editTextNotes.getText().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString("result_text", str);
                        Fragment fragment = new ResultQrFragment();
                        fragment.setArguments(bundle);
                        navigator.hide();
                        navigator.startFragment(fragment);
                    });
        });

        customActions.add(contact);
        customActions.add(apply);

        return customActions;
    }

    @Override
    public int getTitle() {
        return R.string.fragment_contact;
    }
}