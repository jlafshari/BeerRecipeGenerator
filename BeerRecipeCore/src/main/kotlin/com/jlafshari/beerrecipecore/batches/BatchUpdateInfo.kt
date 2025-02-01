package com.jlafshari.beerrecipecore.batches

data class BatchUpdateInfo(
    var gravityReadings: MutableList<GravityReading> = mutableListOf(),
    var status: BatchStatus,
    var assistantBrewerName: String?,
    var notes: String?
)