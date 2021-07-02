package com.jlafshari.beerrecipecore

data class HopIngredient(val name: String, var amount: Double, val boilAdditionTime: Int,
                         val hopId: String)