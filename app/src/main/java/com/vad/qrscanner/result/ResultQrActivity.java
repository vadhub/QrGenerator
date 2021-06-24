package com.vad.qrscanner.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vad.qrscanner.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import dev.sasikanth.colorsheet.ColorSheet;

public class ResultQrActivity extends AppCompatActivity {

    private ImageView imageViewQr;
    private TextView textViewResult;
    private Bitmap bitmapQr;
    private String text;

    public static final int REQUEST_CODE = 24356;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_qr);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.save));

        imageViewQr = (ImageView) findViewById(R.id.imageViewQrResult);
        textViewResult = (TextView) findViewById(R.id.textViewResult);

        Intent intent = getIntent();
        bitmapQr = intent.getParcelableExtra("result_qr");
        text = intent.getStringExtra("result_text");

        imageViewQr.setImageBitmap(bitmapQr);
        textViewResult.setText(text);
    }

    public void onSaveQrClick(View view) {
        ActivityCompat.requestPermissions(ResultQrActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(bitmapQr!=null){
                    saveQr(bitmapQr);
                }
            }else{
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveQr(Bitmap bitmap){

        String outFile = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, ""+System.currentTimeMillis(), "");

        Toast.makeText(this, getString(R.string.save_to)+outFile, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}