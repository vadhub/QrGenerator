package com.vad.qrscanner.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vad.qrscanner.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultQrActivity extends AppCompatActivity {

    private ImageView imageViewQr;
    private Bitmap bitmapQr;

    public static final int REQUEST_CODE = 2356;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_qr);
        setTitle("Save");

        ActivityCompat.requestPermissions(ResultQrActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        imageViewQr = (ImageView) findViewById(R.id.imageViewQrResult);
        Intent intent = getIntent();
        bitmapQr = intent.getParcelableExtra("result_qr");

        imageViewQr.setImageBitmap(bitmapQr);

    }

    public void onSaveQrClick(View view) {
        if(bitmapQr!=null){
            saveQr(bitmapQr);
        }

    }

    private void saveQr(Bitmap bitmap){
//        FileOutputStream outputStream = null;
//        File file = Environment.getExternalStorageDirectory();
//        File dir = new File(file.getAbsolutePath() + "/QrGenerate");
//        dir.mkdirs();
//        String fileName = String.format("%d.jpg", System.currentTimeMillis());
//        File outFile = new File(dir, fileName);
//
//        try {
//            outputStream = new FileOutputStream(outFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//        try {
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String outFile = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, ""+System.currentTimeMillis(), "");

        Toast.makeText(this, "Image save to "+outFile, Toast.LENGTH_SHORT).show();
    }
}