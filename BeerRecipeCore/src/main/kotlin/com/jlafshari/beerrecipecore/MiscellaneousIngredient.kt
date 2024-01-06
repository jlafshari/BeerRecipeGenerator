package com.jlafshari.beerrecipecore

data class MiscellaneousIngredient(val miscellaneousIngredientInfoId: String,
    val unit: String,
    val type: MiscellaneousIngredientType,
    val amount: Float,
    val name: String)

data class MiscellaneousIngredientForUpdate(val miscellaneousIngredientInfoId: String, val amount: Float)