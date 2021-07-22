package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
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
        assertNotNull(validationResult.message)
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
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfoSucceeds() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678"),
                HopIngredient("Cascade", 1.0, 30, "6541")
            )
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(true, validationResult.succeeded)
    }

    @Test
    fun validateRecipeUpdateInfoFailsForIdenticalHopIngredients() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678"),
                HopIngredient("Fuggles", 0.5, 60, "5678"),
            )
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfoFailsForHopIngredientAddedToBoilTooEarly() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 61, "5678"),
                HopIngredient("Cascade", 1.0, 30, "6541")
            )
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfoFailsWhenThereAreNoGrains() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678"),
                HopIngredient("Cascade", 1.0, 30, "6541")
            )
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfoFailsWhenThereAreNoHops() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }
}