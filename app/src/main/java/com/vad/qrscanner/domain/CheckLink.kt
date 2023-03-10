package com.vad.qrscanner.domain

class CheckLink {

    companion object {
        fun checkLink(text: String): Boolean {
            val regex = "((http?|https|ftp|file)://)?((W|w){3}.)?[a-zA-Z0-9]+\\.[a-zA-Z]+".toRegex()
            return regex.containsMatchIn(text)
        }
    }
}