package com.jlafshari.beerrecipecore

data class Recipe(val id: String,
                  val size: Double,
                  val name: String,
                  val styleName: String,
                  val projectedOutcome: RecipeProjectedOutcome,
                  val fermentableIngredients: List<FermentableIngredient>,
                  val yeastIngredient: YeastIngredient)