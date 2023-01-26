package com.vad.qrscanner.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vad.qrscanner.domain.QRTools;
import com.vad.qrscanner.R;

import dev.sasikanth.colorsheet.ColorSheet;

public class ResultQrActivity extends AppCompatActivity {

    private Bitmap bitmapQr;
    private int[] colors;
    private String text;

    private ImageView imageViewQr;
    private TextView textViewResult;

    public static final int REQUEST_CODE = 24356;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_qr);
        setTitle(getString(R.string.save));

        imageViewQr = (ImageView) findViewById(R.id.imageViewQrResult);
        textViewResult = (TextView) findViewById(R.id.textViewResult);

        Intent intent = getIntent();
        text = intent.getStringExtra("result_text");
        bitmapQr = QRTools.generate(text);

        String[] res = getResources().getStringArray(R.array.colors);
        colors = new int[res.length];

        for (int i = 0; i < res.length; i++) {
            colors[i] = Color.parseColor(res[i]);
        }

        imageViewQr.setImageBitmap(bitmapQr);
        textViewResult.setText(text);
    }

    public void onSaveQrClick(View view) {
        ActivityCompat.requestPermissions(ResultQrActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    public void onPickColor(View view) {
        new ColorSheet().colorPicker(colors, 0, true, c -> {
            bitmapQr = QRTools.changeColor(c, text);
            imageViewQr.setImageBitmap(bitmapQr);
            textViewResult.setText(text);
            return null;
        }
        ).show(getSupportFragmentManager());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bitmapQr != null) {
                    saveQr(bitmapQr);
                }
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveQr(Bitmap bitmap) {

        String outFile = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "" + System.currentTimeMillis(), "");

        Toast.makeText(this, getString(R.string.save_to) + outFile, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}