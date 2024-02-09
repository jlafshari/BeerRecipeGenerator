package com.jlafshari.beerrecipegenerator.recipes

import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import javax.inject.Inject

class RecipeValidator @Inject constructor() {
    fun validateRecipeGenerationInfo(recipeGenerationInfo: RecipeGenerationInfo):
            RecipeValidationResult {
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

        return RecipeValidationResult(succeeded, message.toString())
    }

    fun validateRecipeUpdateInfo(recipeUpdateInfo: RecipeUpdateInfo) : RecipeValidationResult {
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

        val boilDurationMinutes = recipeUpdateInfo.boilDurationMinutes
        for (hopIngredient in recipeUpdateInfo.hopIngredients) {
            if (hopIngredient.boilAdditionTime is Int && hopIngredient.boilAdditionTime!! > boilDurationMinutes) {
                isRecipeValid = false
                message.appendLine(
                    "Hop ingredient ${hopIngredient.amount} oz ${hopIngredient.name} (${hopIngredient.boilAdditionTime} min.) is added to boil for longer than $boilDurationMinutes min. boil")
            }
            if (hopIngredient.amount == 0.0) {
                isRecipeValid = false
                message.appendLine("Hop ingredient ${hopIngredient.name} (${hopIngredient.boilAdditionTime} min.) must have an amount greater than zero")
            }
        }

        return RecipeValidationResult(isRecipeValid, message.toString())
    }
}