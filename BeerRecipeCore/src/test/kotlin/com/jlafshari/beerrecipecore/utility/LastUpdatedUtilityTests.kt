package com.jlafshari.beerrecipecore.utility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LastUpdatedUtilityTests {

    @Test
    fun testTimePeriodsForDisplay() {
        val result = LastUpdatedUtility.timePeriodsForDisplay()
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testGetDaysSinceUpdated() {
        val expected = 7
        val result = LastUpdatedUtility.getDaysSinceUpdated(2)
        assertEquals(expected, result)
    }

    @Test
    fun testGetLastUpdatedIndex() {
        val expected = 4
        val result = LastUpdatedUtility.getLastUpdatedIndex(30)
        assertEquals(expected, result)
    }

    @Test
    fun testGetLastUpdatedIndexNull() {
        val expected = 0
        val result = LastUpdatedUtility.getLastUpdatedIndex(null)
        assertEquals(expected, result)
    }
}