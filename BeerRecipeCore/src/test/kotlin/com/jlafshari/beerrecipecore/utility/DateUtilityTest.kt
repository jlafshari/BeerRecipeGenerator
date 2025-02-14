package com.jlafshari.beerrecipecore.utility

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class DateUtilityTest {

    @Test
    fun testGetFormattedDate() {
        val date = "2023-10-15T10:15:30.00Z"
        val expected = "October 15, 2023"
        val result = DateUtility.getFormattedDate(date)
        assertEquals(expected, result)
    }

    @Test
    fun testGetFormattedDateShortMonth() {
        val date = "2023-10-15T10:15:30.00Z"
        val expected = "Oct 15, 2023"
        val result = DateUtility.getFormattedDateShortMonth(date)
        assertEquals(expected, result)
    }

    @Test
    fun testGetFormattedTimeStamp() {
        val instant = Instant.parse("2023-10-15T10:15:30.00Z")
        val expected = "2023-10-15T10:15:30.000Z"
        val result = DateUtility.getFormattedTimeStamp(instant)
        assertEquals(expected, result)
    }
}