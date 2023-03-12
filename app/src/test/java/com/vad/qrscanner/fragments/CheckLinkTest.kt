package com.vad.qrscanner.fragments

import com.vad.qrscanner.domain.CheckLink
import org.junit.Test
import org.junit.Assert.*


internal class CheckLinkTest {

    @Test
    fun checkLink() {
        assertEquals(true, CheckLink.checkLink("https://www.youtube.com/"))
        assertEquals(true, CheckLink.checkLink("http://www.youtube.com/"))
        assertEquals(true, CheckLink.checkLink("www.youtube.com/"))
        assertEquals(true, CheckLink.checkLink("youtube.ru"))
        assertEquals(true, CheckLink.checkLink("youtube.com"))
    }

    @Test
    fun extractLink() {
        assertEquals("youtube.com", CheckLink.extractLink("youtube.com"))
        assertEquals("www.youtube.com", CheckLink.extractLink("https://www.youtube.com/"))
    }
}