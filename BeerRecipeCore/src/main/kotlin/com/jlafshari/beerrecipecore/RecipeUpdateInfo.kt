package com.jlafshari.beerrecipecore

data class RecipeUpdateInfo(var name: String,
                            var fermentableIngredients: MutableList<FermentableIngredient>,
                            var hopIngredients: MutableList<HopIngredient>,
                            var extractionEfficiency: Int)