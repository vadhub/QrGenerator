
package com.vad.qrscanner.result

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import dev.sasikanth.colorsheet.ColorSheet

class AdapterForColorSheet {
    fun getColorPick(activity: AppCompatActivity, colors: IntArray): Int{
        var col: Int = Color.BLACK
        ColorSheet().colorPicker(
                colors = colors,
                listener = { color ->
                    col = color
                })
                .show(activity.supportFragmentManager);

        return col
    }
}