package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipegenerator.srmColors.Colors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SrmColorTests {
    @Test
    fun canGetColorsInRange() {
        val colorRange = Colors.getColorsInRange(1, 5)
        assertEquals(5, colorRange.size)
    }

    @Test
    fun srmColorReturnsExpectedColor() {
        val srmColor = Colors.getColor(10)
        assertNotNull(srmColor)
        assertEquals(10, srmColor!!.srmColor)
    }

    @Test
    fun zeroSrmColorReturnsNull() {
        val srmColor = Colors.getColor(0)
        assertEquals(null, srmColor)
    }

    @Test
    fun darkSrmColorBeyondMaxSupportedSrmColorReturnsMaxSrmColor() {
        val srmColor = Colors.getColor(400)
        assertNotNull(srmColor)
        assertEquals(30, srmColor!!.srmColor)
    }
}