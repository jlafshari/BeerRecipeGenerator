package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Recipe(val id: String,
                  val size: Double,
                  val name: String,
                  val styleName: String?,
                  val projectedOutcome: RecipeProjectedOutcome,
                  val fermentableIngredients: MutableList<FermentableIngredient>,
                  val hopIngredients: MutableList<HopIngredient>,
                  val yeastIngredient: YeastIngredient,
                  val mashProfile: MashProfile,
                  val spargeWaterAmount: Float,
                  val fermentationSteps: List<FermentationStep>
)