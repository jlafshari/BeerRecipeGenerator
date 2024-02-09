package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.HopUse
import com.jlafshari.beerrecipecore.MashProfileForUpdate
import com.jlafshari.beerrecipecore.MashStep
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
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(1.0, "2 row", 2.0, "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678",
                    HopUse.Boil, null, null, null),
                HopIngredient("Cascade", 1.0, 30, "6541",
                    HopUse.Boil, null, null, null)
            ),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(true, validationResult.succeeded)
    }

    @Test
    fun validateRecipeUpdateInfo_failsForIdenticalHopIngredients() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(1.0, "2 row", 2.0, "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678",
                    HopUse.Boil, null, null, null),
                HopIngredient("Fuggles", 0.5, 60, "5678",
                    HopUse.Boil, null, null, null),
            ),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsForHopIngredientAddedToBoilTooEarly() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(1.0, "2 row", 2.0, "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 61, "5678",
                    HopUse.Boil, null, null, null),
                HopIngredient("Cascade", 1.0, 30, "6541",
                    HopUse.Boil, null, null, null)
            ),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenThereAreNoGrains() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678",
                    HopUse.Boil, null, null, null),
                HopIngredient("Cascade", 1.0, 30, "6541",
                    HopUse.Boil, null, null, null)
            ),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenThereAreNoHops() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(1.0, "2 row", 2.0, "1234")),
            mutableListOf(),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenAnyFermentableIngredientAmountIsZero() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(0.0, "2 row", 2.5, "1234")),
            mutableListOf(HopIngredient("Fuggles", 1.0, 60, "5678",
                HopUse.Boil, null, null, null)),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }

    @Test
    fun validateRecipeUpdateInfo_failsWhenAnyHopIngredientAmountIsZero() {
        val mashProfileForUpdate = MashProfileForUpdate(listOf(MashStep(60, 153)), 65, 1.25F)
        val recipeUpdateInfo = RecipeUpdateInfo("Some beer",
            5.0,
            mutableListOf(FermentableIngredient(1.0, "2 row", 1.0, "1234")),
            mutableListOf(
                HopIngredient("Fuggles", 1.0, 60, "5678",
                    HopUse.Boil, null, null, null),
                HopIngredient("Cascade", 0.0, 30, "6541",
                    HopUse.Boil, null, null, null)
            ),
            "8234765woirudsjchv",
            70,
            60,
            0.25F,
            0.25F,
            mashProfileForUpdate,
            mutableListOf(),
            mutableListOf()
        )

        val validationResult = recipeValidator.validateRecipeUpdateInfo(recipeUpdateInfo)
        assertEquals(false, validationResult.succeeded)
        assertNotNull(validationResult.message)
    }
}