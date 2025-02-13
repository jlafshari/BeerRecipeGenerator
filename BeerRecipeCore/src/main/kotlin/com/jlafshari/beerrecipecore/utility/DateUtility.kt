package com.jlafshari.beerrecipecore.utility

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtility {
    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneOffset.UTC)
    private val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")

    fun getFormattedDate(date: String) : String {
        val zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val currentZoneId = ZoneId.systemDefault()
        val currentZonedDateTime = zonedDateTime.withZoneSameInstant(currentZoneId)

        return currentZonedDateTime.format(humanReadableFormatter)
    }

    fun getFormattedTimeStamp(time: Instant): String = timestampFormatter.format(time)
}