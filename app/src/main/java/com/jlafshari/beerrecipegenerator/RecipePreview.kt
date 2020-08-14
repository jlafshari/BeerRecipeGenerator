package com.jlafshari.beerrecipegenerator

data class RecipePreview(val id: String, val name: String, val styleName: String) {
    override fun toString(): String = "$name ($styleName)"
}