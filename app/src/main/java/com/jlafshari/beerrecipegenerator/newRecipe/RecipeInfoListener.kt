package com.jlafshari.beerrecipegenerator.newRecipe

import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipegenerator.RecipeDefaultSettings

interface RecipeInfoListener {
    fun getCurrentRecipeGenerationInfo() : RecipeGenerationInfo
    fun getRecipeDefaultSettings() : RecipeDefaultSettings
}
