package com.jlafshari.beerrecipecore


data class HopIngredient(val name: String,
                         var amount: Double,
                         var boilAdditionTime: Int?,
                         val hopId: String,
                         var use: HopUse,
                         val dryHopDayStart: Int?,
                         val dryHopDayEnd: Int?,
                         val whirlpoolDuration: Int?)