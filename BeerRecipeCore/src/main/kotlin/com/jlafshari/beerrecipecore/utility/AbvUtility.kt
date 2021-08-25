package com.jlafshari.beerrecipecore.utility

import com.jlafshari.beerrecipecore.StyleThreshold

object AbvUtility {
    private val potentialAbvValues = listOf(3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0)

    fun getAbvValues(abvThreshold: StyleThreshold): List<Double> =
        potentialAbvValues.filter { it in abvThreshold.minimum..abvThreshold.maximum }

    fun getMedianAbvIndex(abvValues: List<Double>): Int = (abvValues.size - 1) / 2
}