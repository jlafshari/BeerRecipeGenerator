package com.jlafshari.beerrecipecore


data class Style(val id: String, val name: String, val thresholds: List<StyleThreshold>) {
    override fun toString(): String {
        return name
    }

    fun ibuThreshold(): StyleThreshold = thresholds.find { it.value == "ibu" }!!
    fun colorThreshold(): StyleThreshold = thresholds.find { it.value == "color" }!!
    fun abvThreshold(): StyleThreshold = thresholds.find { it.value == "abv" }!!
}
