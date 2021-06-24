package com.vad.qrscanner.result

import android.app.Activity
import android.content.Context
import android.graphics.Color
import dev.sasikanth.colorsheet.ColorSheet

class AdapterForColorSheet {
    fun getColorPick(activity: Activity){
        val colors = intArrayOf()
        ColorSheet().colorPicker(
                colors = colors,
                listener = { color ->
                    println(color)
                })
                .show(supportFragmentManager)
    }
}