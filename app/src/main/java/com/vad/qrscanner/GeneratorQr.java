package com.vad.qrscanner;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratorQr {
    public Bitmap generate(String textToQr){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToQr, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
