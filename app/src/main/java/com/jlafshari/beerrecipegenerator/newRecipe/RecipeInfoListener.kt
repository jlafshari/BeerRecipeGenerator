package com.jlafshari.beerrecipegenerator.newRecipe

import com.jlafshari.beerrecipecore.RecipeGenerationInfo

interface RecipeInfoListener {
    fun getCurrentRecipeGenerationInfo() : RecipeGenerationInfo
}
