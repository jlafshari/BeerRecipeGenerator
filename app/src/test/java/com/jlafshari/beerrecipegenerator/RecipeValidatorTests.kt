package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipegenerator.recipes.RecipeValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RecipeValidatorTests {
    private val recipeValidator = RecipeValidator()

    @Test
    fun validateRecipeGenerationInfo_succeeds() {
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
    fun validateRecipeGenerationInfo_failsForEmptyRecipeGenerationInfo() {
        val recipeGenerationInfo = RecipeGenerationInfo()

        val validationResult = recipeValidator.validateRecipeGenerationInfo(recipeGenerationInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeGenerationInfo_failsWhenNameIsMissing() {
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
    fun validateRecipeGenerationInfo_failsWhenRecipeSizeIsZero() {
        val recipeGenerationInfo = RecipeGenerationInfo()
        recipeGenerationInfo.colorSrm = 5
        recipeGenerationInfo.name = "ESB"
        recipeGenerationInfo.abv = 6.0
        recipeGenerationInfo.ibu = 20
        recipeGenerationInfo.size = 0.0

        val validationResult = recipeValidator.validateRecipeGenerationInfo(recipeGenerationInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_succeeds() {
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
    fun validateRecipeUpdateInfo_failsForIdenticalHopIngredients() {
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
    fun validateRecipeUpdateInfo_failsForHopIngredientAddedToBoilTooEarly() {
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
    fun validateRecipeUpdateInfo_failsWhenThereAreNoGrains() {
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
    fun validateRecipeUpdateInfo_failsWhenThereAreNoHops() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenAnyFermentableIngredientAmountIsZero() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(0.0, "2 row", "1234")),
            mutableListOf(HopIngredient("Fuggles", 1.0, 60, "5678"))
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenAnyHopIngredientAmountIsZero() {
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            mutableListOf(FermentableIngredient(1.0, "2 row", "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678"),
                HopIngredient("Cascade", 0.0, 30, "6541")
            )
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }
}