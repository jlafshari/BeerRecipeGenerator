package com.jlafshari.beerrecipecore

import com.jlafshari.beerrecipecore.utility.AbvUtility.getAbvValues
import com.jlafshari.beerrecipecore.utility.AbvUtility.getMedianAbvIndex
import com.jlafshari.beerrecipecore.utility.AbvUtility.printAbvValue
import org.junit.Assert.assertEquals
import org.junit.Test

class AbvUtilityTests {
    @Test
    fun canGetAbvValues() {
        val threshold = StyleThreshold("abv", 4.5, 5.5)
        val abvValues = getAbvValues(threshold)
        assertEquals(3, abvValues.size)
    }

    @Test
    fun canGetCorrectAbvIndexForOddCountList() {
        val abvValues = listOf(4.5, 5.0, 5.5, 6.0, 6.5)
        val abvIndex = getMedianAbvIndex(abvValues)
        assertEquals(2, abvIndex)
    }

    @Test
    fun canGetCorrectAbvIndexForEvenCountList() {
        val abvValues = listOf(4.5, 5.0, 5.5, 6.0)
        val abvIndex = getMedianAbvIndex(abvValues)
        assertEquals(1, abvIndex)
    }

    @Test
    fun printAbvValuePrintsValueCorrectly() {
        val abv = 5.134
        val printedAbv = printAbvValue(abv)
        assertEquals("5.13%", printedAbv)
    }
}