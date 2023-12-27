package com.jlafshari.beerrecipecore.recipes

data class RecipePreview(val id: String, val name: String, val styleName: String) {
    override fun toString(): String = "$name ($styleName)"
}