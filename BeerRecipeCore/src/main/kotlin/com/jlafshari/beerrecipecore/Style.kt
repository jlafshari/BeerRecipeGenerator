package com.jlafshari.beerrecipecore


data class Style(val id: String, val name: String, val thresholds: List<StyleThreshold>) {
    override fun toString(): String {
        return name
    }

    val ibuThreshold = thresholds.find { it.value == "ibu" }!!
    val colorThreshold = thresholds.find { it.value == "color" }!!
    val abvThreshold = thresholds.find { it.value == "abv" }!!
}
