package com.jlafshari.beerrecipecore

data class MiscellaneousIngredient(val miscellaneousIngredientInfoId: String,
    val unit: String,
    val type: MiscellaneousIngredientType,
    val amount: Float,
    val name: String)