package com.jlafshari.beerrecipegenerator

data class RecipePreview(val name: String, val styleName: String) {
    override fun toString(): String = "$name ($styleName)"
}