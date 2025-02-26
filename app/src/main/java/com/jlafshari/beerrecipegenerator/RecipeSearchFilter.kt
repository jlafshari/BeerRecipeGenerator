package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import java.io.Serializable

data class RecipeSearchFilter(
    val abvEnabled: Boolean,
    val abvMin: String?,
    val abvMax: String?,
    val colorMin: Int?,
    val colorMax: Int?,
    val aleEnabled: Boolean,
    val lagerEnabled: Boolean,
    val recipeType: String?,
    val fermentables: List<Fermentable>,
    val hops: List<Hop>,
    val daysSinceLastUpdated: Int?,
    val searchFilterVisible: Boolean
) : Serializable