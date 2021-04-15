package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipegenerator.srmColors.Colors
import org.junit.Assert.assertEquals
import org.junit.Test

class SrmColorTests {
    @Test
    fun canGetColorsInRange() {
        val colorRange = Colors.getColorsInRange(1, 5)
        assertEquals(5, colorRange.size)
    }
}