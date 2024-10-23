package com.jlafshari.beerrecipecore.batches

enum class BatchStatus {
    NotStarted,
    Brewing,
    Fermenting,
    Carbonating,
    Completed
}

fun BatchStatus.displayText(): String {
    return when (this) {
        BatchStatus.NotStarted -> "Not Started"
        BatchStatus.Brewing -> "Brewing"
        BatchStatus.Fermenting -> "Fermenting"
        BatchStatus.Carbonating -> "Carbonating"
        BatchStatus.Completed -> "Completed"
    }
}