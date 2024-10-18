package com.jlafshari.beerrecipecore.batches

data class BatchPreview(
    val id: String,
    val recipeName: String,
    val brewingDate: String,
    val status: BatchStatus
)