package com.jlafshari.beerrecipegenerator

import java.io.Serializable

data class RecipeSearchFilter(
    val abvEnabled: Boolean,
    val abvMin: String?,
    val abvMax: String?,
    val colorEnabled: Boolean,
    val colorMin: String?,
    val colorMax: String?,
    val aleEnabled: Boolean,
    val lagerEnabled: Boolean,
    val yeastType: String?,
    val fermentableIds: List<String>
) : Serializable