package com.jlafshari.beerrecipecore

import com.jlafshari.beerrecipecore.utility.BitternessUtility.getIbuValues
import com.jlafshari.beerrecipecore.utility.BitternessUtility.getMedianIbuIndex
import org.junit.Assert.assertEquals
import org.junit.Test

class BitternessUtilityTests {
    @Test
    fun canGetIbuValues() {
        val threshold = StyleThreshold("ibu", 15.0, 18.0)
        val ibuValues = getIbuValues(threshold)
        assertEquals(4, ibuValues.size)
    }

    @Test
    fun canGetCorrectIbuIndexForOddCountList() {
        val ibuValues = listOf(4, 5, 5, 6, 7)
        val ibuIndex = getMedianIbuIndex(ibuValues)
        assertEquals(2, ibuIndex)
    }

    @Test
    fun canGetCorrectIbuIndexForEvenCountList() {
        val ibuValues = listOf(4, 5, 5, 6)
        val ibuIndex = getMedianIbuIndex(ibuValues)
        assertEquals(1, ibuIndex)
    }
}