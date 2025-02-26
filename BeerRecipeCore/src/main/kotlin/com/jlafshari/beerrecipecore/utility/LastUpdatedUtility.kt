package com.jlafshari.beerrecipecore.utility

object LastUpdatedUtility {
    private val daysSinceUpdatedDisplayMapping = mapOf(
        null to "---",
        1 to "1 day",
        7 to "1 week",
        14 to "2 weeks",
        30 to "1 month",
        60 to "2 months",
        90 to "3 months"
    )
    private val daySinceUpdateValues = daysSinceUpdatedDisplayMapping.keys.toList()

    fun timePeriodsForDisplay() = daysSinceUpdatedDisplayMapping.values.toList()

    fun getDaysSinceUpdated(index: Int) = daySinceUpdateValues[index]

    fun getLastUpdatedIndex(daysSinceLastUpdated: Int?): Int =
        daySinceUpdateValues.indexOf(daysSinceLastUpdated)
}