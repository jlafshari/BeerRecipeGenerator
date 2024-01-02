package com.jlafshari.beerrecipecore.recipes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.FermentationStep
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.MashProfile
import com.jlafshari.beerrecipecore.RecipeProjectedOutcome
import com.jlafshari.beerrecipecore.YeastIngredient

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