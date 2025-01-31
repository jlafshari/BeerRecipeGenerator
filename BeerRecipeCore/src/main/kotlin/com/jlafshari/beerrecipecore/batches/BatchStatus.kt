package com.jlafshari.beerrecipecore.batches

enum class BatchStatus {
    NotStarted,
    Brewing,
    Fermenting,
    Carbonating,
    Completed
}

fun BatchStatus.displayText(): String = batchStatusEnumDisplayMap[this]!!

val batchStatusEnumDisplayMap = mapOf(
    BatchStatus.NotStarted to "Not Started",
    BatchStatus.Brewing to "Brewing",
    BatchStatus.Fermenting to "Fermenting",
    BatchStatus.Carbonating to "Carbonating",
    BatchStatus.Completed to "Completed"
)