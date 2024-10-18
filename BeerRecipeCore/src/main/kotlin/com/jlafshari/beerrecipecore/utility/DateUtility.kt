package com.jlafshari.beerrecipecore.utility

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtility {
    fun getFormattedDate(date: String) : String {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)

        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        return dateTime.format(humanReadableFormatter)
    }
}