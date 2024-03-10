package com.jlafshari.beerrecipecore


data class HopIngredient(val name: String,
                         var amount: Double,
                         var boilAdditionTime: Int?,
                         val hopId: String,
                         val use: HopUse,
                         val dryHopDayStart: Int?,
                         val dryHopDayEnd: Int?,
                         val whirlpoolDuration: Int?)