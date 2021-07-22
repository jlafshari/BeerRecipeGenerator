package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RecipeValidatorTests {
    private val recipeValidator = RecipeValidator()

    @Test
    fun validateRecipeGenerationInfoSucceeds() {
        val recipeGenerationInfo = RecipeGenerationInfo()
        recipeGenerationInfo.colorSrm = 5
        recipeGenerationInfo.name = "ESB"
        recipeGenerationInfo.abv = 6.0
        recipeGenerationInfo.ibu = 20
        recipeGenerationInfo.size = 5.0

        val validationResult = recipeValidator.validateRecipeGenerationInfo(recipeGenerationInfo)
        assertEquals(true, validationResult.succeeded)
    }

    @Test
    fun validateRecipeGenerationInfoFailsForEmptyRecipeGenerationInfo() {
        val recipeGenerationInfo = RecipeGenerationInfo()

        val validationResult = recipeValidator.validateRecipeGenerationInfo(recipeGenerationInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.succeeded)
    }

    @Test
    fun validateRecipeGenerationInfoFailsWhenNameIsMissing() {
        val recipeGenerationInfo = RecipeGenerationInfo()
        recipeGenerationInfo.colorSrm = 5
        recipeGenerationInfo.abv = 6.0
        recipeGenerationInfo.ibu = 20
        recipeGenerationInfo.size = 5.0

        val validationResult = recipeValidator.validateRecipeGenerationInfo(recipeGenerationInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.succeeded)
    }
}