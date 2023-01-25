package com.vad.qrscanner;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratorQr {
    public static Bitmap generate(String textToQr){
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

    public static Bitmap changeColor(Bitmap bitmap, int color){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        float[] srcHSV = new float[3];
        float[] dstHSV = new float[3];

        Bitmap bitmapResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for(int row = 0; row<height; row++){
            for(int col = 0; col<width; col++){
                int pixel = bitmap.getPixel(col, row);
                int alpha = Color.alpha(pixel);
                Color.colorToHSV(pixel,srcHSV);
                Color.colorToHSV(color, dstHSV);

                dstHSV[2]=srcHSV[2];

                bitmapResult.setPixel(col, row, Color.HSVToColor(alpha,dstHSV));
            }
        }

        return bitmapResult;
    }
}
