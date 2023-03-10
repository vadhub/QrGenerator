package com.vad.qrscanner.domain

class CheckLink {

    companion object {
        fun checkLink(text: String): Boolean {
            val regexLink = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".toRegex()
            return regexLink.containsMatchIn(text)
        }
    }
}