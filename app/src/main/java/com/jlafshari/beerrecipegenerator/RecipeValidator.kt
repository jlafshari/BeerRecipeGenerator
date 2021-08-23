package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipegenerator.editRecipe.RecipeUpdateValidationResult
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeGenerationValidationResult
import javax.inject.Inject

class RecipeValidator @Inject constructor() {
    fun validateRecipeGenerationInfo(recipeGenerationInfo: RecipeGenerationInfo):
            RecipeGenerationValidationResult {
        var succeeded = true
        val message = StringBuilder()
        if (recipeGenerationInfo.colorSrm == null) {
            succeeded = false
            message.appendLine("No beer color selected!")
        }
        if (recipeGenerationInfo.name.isNullOrEmpty()) {
            succeeded = false
            message.appendLine("No recipe name given!")
        }
        if (recipeGenerationInfo.abv == null) {
            succeeded = false
            message.appendLine("No ABV selected!")
        }
        if (recipeGenerationInfo.ibu == null) {
            succeeded = false
            message.appendLine("No IBU selected!")
        }

        if (recipeGenerationInfo.size == null) {
            succeeded = false
            message.appendLine("No recipe size given!")
        }
        else if (recipeGenerationInfo.size == 0.0) {
            succeeded = false
            message.appendLine("Recipe size cannot be zero!")
        }

        return RecipeGenerationValidationResult(succeeded, message.toString())
    }

    fun validateRecipeUpdateInfo(recipeUpdateInfo: RecipeUpdateInfo) : RecipeUpdateValidationResult {
        var isRecipeValid = true
        val message = StringBuilder()

        if (recipeUpdateInfo.name.isEmpty()) {
            isRecipeValid = false
            message.appendLine("Recipe name cannot be empty!")
        }

        if (recipeUpdateInfo.fermentableIngredients.size == 0) {
            isRecipeValid = false
            message.appendLine("Recipe needs at least one grain!")
        }

        if (recipeUpdateInfo.hopIngredients.size == 0) {
            isRecipeValid = false
            message.appendLine("Recipe needs at least one hop!")
        }

        for (fermentableIngredient in recipeUpdateInfo.fermentableIngredients) {
            if (fermentableIngredient.amount == 0.0) {
                isRecipeValid = false
                message.appendLine("Grain ingredient ${fermentableIngredient.name} must have an amount greater than zero")
            }
        }

        val identicalHopIngredients = recipeUpdateInfo.hopIngredients.groupBy {
            listOf(it.hopId, it.boilAdditionTime) }.filter { it.value.size > 1 }
        if (identicalHopIngredients.any()) {
            isRecipeValid = false
            message.appendLine("Hops of same variety are added to boil at same time!")

            for (identicalHopIngredient in identicalHopIngredients) {
                val ingredient = identicalHopIngredient.value.first()
                message.appendLine("${ingredient.name}: ${ingredient.boilAdditionTime}")
            }
        }

        for (hopIngredient in recipeUpdateInfo.hopIngredients) {
            if (hopIngredient.boilAdditionTime > Constants.BOIL_DURATION_TIME_DEFAULT) {
                isRecipeValid = false
                message.appendLine(
                    "Hop ingredient ${hopIngredient.amount} oz ${hopIngredient.name} (${hopIngredient.boilAdditionTime} min.) is added to boil for longer than ${Constants.BOIL_DURATION_TIME_DEFAULT} min. boil")
            }
            if (hopIngredient.amount == 0.0) {
                isRecipeValid = false
                message.appendLine("Hop ingredient ${hopIngredient.name} (${hopIngredient.boilAdditionTime} min.) must have an amount greater than zero")
            }
        }

        return RecipeUpdateValidationResult(isRecipeValid, message.toString())
    }
}