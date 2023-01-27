package com.vad.qrscanner.domain

import android.widget.EditText

class CheckEmptyText {
    companion object {
        fun check(text: String, vararg editText: EditText, runnable: Runnable) {
            editText.forEach {
                if (it.text.isEmpty()) {
                    it.error = text
                    return
                } else {
                    runnable.run()
                    return
                }
            }
        }
    }
}