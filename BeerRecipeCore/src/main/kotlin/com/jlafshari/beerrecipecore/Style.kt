package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Style(val name: String, val thresholds: List<StyleThreshold>) {
    override fun toString(): String {
        return name
    }

    val colorThreshold = thresholds.find { it.value == "color" }!!
    val abvThreshold = thresholds.find { it.value == "abv" }!!
}
