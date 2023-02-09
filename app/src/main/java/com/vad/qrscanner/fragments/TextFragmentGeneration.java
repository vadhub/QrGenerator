package com.vad.qrscanner.fragments;


import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vad.qrscanner.R;
import com.vad.qrscanner.navigation.CustomAction;
import com.vad.qrscanner.navigation.HasCustomAction;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;

public class TextFragmentGeneration extends Fragment implements HasCustomTitle, HasCustomAction {

    private EditText editText;
    private Button clipBoard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_text_generation, container, false);
        editText = (EditText) v.findViewById(R.id.editTextText);
        clipBoard = v.findViewById(R.id.clipboardButton);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clipBoard.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            try {
                CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
                editText.setText(textToPaste);
            } catch (Exception e) {
                return;
            }
        });
    }

    @NonNull
    @Override
    public CustomAction setCustomAction(@NonNull Navigator navigator) {
        return new CustomAction(R.drawable.ic_baseline_done_24, () -> {
            String str = editText.getText().toString();
            if (!str.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("result_text", str);
                Fragment fragment = new ResultQrFragment();
                fragment.setArguments(bundle);
                navigator.hide();
                navigator.startFragment(fragment);
            } else {
                Toast toast = Toast.makeText(getContext(), getString(R.string.enter_text_pl), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        });
    }

    @Override
    public int getTitle() {
        return R.string.fragment_text;
    }
}