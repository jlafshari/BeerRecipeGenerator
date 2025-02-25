package com.jlafshari.beerrecipecore


data class HopIngredient(val name: String,
                         var amount: Double,
                         var boilAdditionTime: Int?,
                         val hopId: String,
                         var use: HopUse,
                         var dryHopDayStart: Int?,
                         var dryHopDayEnd: Int?,
                         var whirlpoolDuration: Int?)