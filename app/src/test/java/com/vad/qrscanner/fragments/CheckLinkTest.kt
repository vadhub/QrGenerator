package com.vad.qrscanner.fragments

import com.vad.qrscanner.domain.CheckLink
import org.junit.Test
import org.junit.Assert.*


internal class CheckLinkTest {

    @Test
    fun checkLink() {
        assertEquals(true, CheckLink.checkLink("https://www.youtube.com/"))
    }
}