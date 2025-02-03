package com.jlafshari.beerrecipecore.utility

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateUtility {
    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneOffset.UTC)

    fun getFormattedDate(date: String) : String {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)

        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        return dateTime.format(humanReadableFormatter)
    }

    fun getFormattedTimeStamp(time: Instant): String = timestampFormatter.format(time)
}