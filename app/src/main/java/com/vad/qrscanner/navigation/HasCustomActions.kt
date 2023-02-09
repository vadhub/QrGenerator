package com.vad.qrscanner.navigation

interface HasCustomActions {
    fun setCustomAction(navigator: Navigator): List<CustomAction>
}