package com.jlafshari.beerrecipecore

import com.jlafshari.beerrecipecore.utility.AbvUtility.getAbvValues
import org.junit.Assert.assertEquals
import org.junit.Test

class AbvUtilityTests {
    @Test
    fun canGetAbvValues() {
        val threshold = StyleThreshold("abv", 4.5, 5.5)
        val abvValues = getAbvValues(threshold)
        assertEquals(3, abvValues.size)
    }
}