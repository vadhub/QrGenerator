package com.vad.qrscanner.navigation

import androidx.fragment.app.Fragment

interface Navigator: KeyBoard {
    fun startFragment(fragment: Fragment)
}
