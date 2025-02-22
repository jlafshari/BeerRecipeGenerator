package com.jlafshari.beerrecipecore.recipes.recipeSorting

import com.jlafshari.beerrecipecore.recipes.RecipePreview

object RecipeSorter {
    fun sortRecipes(recipes: List<RecipePreview>, sortType: RecipeSortType, ascending: Boolean):
            List<RecipePreview> {
        return when (sortType) {
            RecipeSortType.None -> recipes
            RecipeSortType.Abv -> if (ascending) recipes.sortedBy { it.abv } else
                recipes.sortedByDescending { it.abv }
            RecipeSortType.Color -> if (ascending) recipes.sortedBy { it.colorSrm } else
                recipes.sortedByDescending { it.colorSrm }
            RecipeSortType.BatchCount -> if (ascending) recipes.sortedBy { it.numberOfBatches } else
                recipes.sortedByDescending { it.numberOfBatches }
        }
    }
}