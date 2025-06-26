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
    private val humanReadableFormatterShortMonth = DateTimeFormatter.ofPattern("MMM d, yyyy")
    private val humanReadableFormatterShortMonthNoYear = DateTimeFormatter.ofPattern("MMM d")

    fun getFormattedDate(date: String) : String {
        val currentZonedDateTime = parseZonedDateTime(date)

        return currentZonedDateTime.format(humanReadableFormatter)
    }

    fun getFormattedDateShortMonth(date: String): String {
        val currentZonedDateTime = parseZonedDateTime(date)

        return currentZonedDateTime.format(humanReadableFormatterShortMonth)
    }

    fun getFormattedDateShortMonthNoYear(date: String): String {
        val currentZonedDateTime = parseZonedDateTime(date)

        return currentZonedDateTime.format(humanReadableFormatterShortMonthNoYear)
    }

    private fun parseZonedDateTime(date: String): ZonedDateTime {
        val zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val currentZoneId = ZoneId.systemDefault()
        val currentZonedDateTime = zonedDateTime.withZoneSameInstant(currentZoneId)
        return currentZonedDateTime
    }

    fun getFormattedTimeStamp(time: Instant): String = timestampFormatter.format(time)
}