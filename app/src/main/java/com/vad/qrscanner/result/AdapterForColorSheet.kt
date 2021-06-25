package com.vad.qrscanner.result

import androidx.appcompat.app.AppCompatActivity
import dev.sasikanth.colorsheet.ColorSheet

class AdapterForColorSheet {
    fun getColorPick(activity: AppCompatActivity){
        val colors = intArrayOf()
        ColorSheet().colorPicker(
                colors = colors,
                listener = { color ->
                    println(color)
                })
                .show(activity.supportFragmentManager);
    }
}