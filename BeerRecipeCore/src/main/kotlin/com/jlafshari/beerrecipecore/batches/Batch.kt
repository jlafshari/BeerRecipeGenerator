package com.jlafshari.beerrecipecore.batches

import com.jlafshari.beerrecipecore.recipes.Recipe

data class Batch(
    val id: String,
    val recipe: Recipe,
    val brewingDate: String,
    val assistantBrewerName: String?,
    val notes: String?,
    val statusHistory: List<BatchStatusItem>,
    val gravityReadings: List<GravityReading>,
    val currentAbv: Double?
)