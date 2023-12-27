package com.jlafshari.beerrecipecore.recipes

data class RecipePreview(val id: String, val name: String,
                         val type: RecipeType, val isVersioned: Boolean,
                         val versionNumber: Int) {
    override fun toString(): String = name
}