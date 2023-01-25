package com.vad.qrscanner

import android.widget.EditText

class CheckEmptyText {
    companion object {
        fun check(vararg editText: EditText, runnable: Runnable) {
            editText.forEach {
                if (it.text.isEmpty()) {
                    it.error = "empty text"
                } else {
                    runnable.run()
                    return
                }
            }
        }
    }
}