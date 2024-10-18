package com.jlafshari.beerrecipecore.batches

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BatchPreview(
    val id: String,
    val recipeName: String,
    val brewingDate: String,
    val status: BatchStatus
) {
    fun getFormattedBrewingDate(): String {
        val dateTime = LocalDateTime.parse(brewingDate, DateTimeFormatter.ISO_DATE_TIME)

        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        return dateTime.format(humanReadableFormatter)
    }
}