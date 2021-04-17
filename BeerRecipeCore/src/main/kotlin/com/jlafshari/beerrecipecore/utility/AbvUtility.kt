package com.jlafshari.beerrecipecore.utility

import com.jlafshari.beerrecipecore.StyleThreshold

object AbvUtility {
     fun getAbvValues(abvThreshold: StyleThreshold): List<String> {
        val potentialAbvValues = listOf(3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0)

        return potentialAbvValues.filter { it in abvThreshold.minimum..abvThreshold.maximum }
            .map { it.toString() }
    }
}