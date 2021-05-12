package com.jlafshari.beerrecipecore.utility

import com.jlafshari.beerrecipecore.StyleThreshold

object BitternessUtility {
    fun getIbuValues(bitternessThreshold: StyleThreshold): List<Int> {
        val values = mutableListOf<Int>()
        for (i in bitternessThreshold.minimum.toInt()..bitternessThreshold.maximum.toInt())
            values += i

        return values
    }

    fun getMedianIbuIndex(ibuValues: List<Int>): Int = (ibuValues.size - 1) / 2
}