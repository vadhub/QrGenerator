package com.vad.qrscanner;

import androidx.activity.result.ActivityResultLauncher;

public class Common {
    private static Common instance = null;
    private Common() {}

    public static synchronized Common getInstance() {
        if (instance == null) {
            instance = new Common();
        }

        return instance;
    }

    ActivityResultLauncher<String> mGetContent;

    public void start() {
        if (mGetContent != null) mGetContent.launch("image/*");
    }
}
