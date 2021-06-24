package com.jlafshari.beerrecipegenerator.srmColors
import android.graphics.Color

object Colors {
    fun getColor(srmColor: Int): SrmColor? {
        val maxColor = srmColors.maxOf { it.srmColor }
        return if (srmColor >= maxColor) {
            srmColors.find { it.srmColor == maxColor }
        } else {
            srmColors.find { it.srmColor == srmColor }
        }
    }

    fun getColorsInRange(startingSrmColor: Int, endingSrmColor: Int): List<SrmColor> {
        return srmColors.filter { it.srmColor in startingSrmColor..endingSrmColor }
    }

    private val srmColors = listOf(
        SrmColor(1, Color.rgb(243, 249, 147)),
        SrmColor(2, Color.rgb(245, 247, 92)),
        SrmColor(3, Color.rgb(246, 245, 19)),
        SrmColor(4, Color.rgb(234, 230, 21)),
        SrmColor(5, Color.rgb(224, 208, 27)),
        SrmColor(6, Color.rgb(213, 188, 38)),
        SrmColor(7, Color.rgb(205, 170, 55)),
        SrmColor(8, Color.rgb(193, 150, 60)),
        SrmColor(9, Color.rgb(190, 140, 58)),
        SrmColor(10, Color.rgb(190, 130, 58)),
        SrmColor(11, Color.rgb(193, 122, 55)),
        SrmColor(12, Color.rgb(191, 113, 56)),
        SrmColor(13, Color.rgb(188, 103, 51)),
        SrmColor(14, Color.rgb(178, 96, 51)),
        SrmColor(15, Color.rgb(168, 88, 57)),
        SrmColor(16, Color.rgb(152, 83, 54)),
        SrmColor(17, Color.rgb(141, 76, 50)),
        SrmColor(18, Color.rgb(124, 69, 45)),
        SrmColor(19, Color.rgb(107, 58, 30)),
        SrmColor(20, Color.rgb(93, 52, 26)),
        SrmColor(21, Color.rgb(78, 42, 12)),
        SrmColor(22, Color.rgb(74, 39, 39)),
        SrmColor(23, Color.rgb(54, 31, 27)),
        SrmColor(24, Color.rgb(38, 23, 22)),
        SrmColor(25, Color.rgb(35, 23, 22)),
        SrmColor(26, Color.rgb(25, 16, 15)),
        SrmColor(27, Color.rgb(22, 16, 15)),
        SrmColor(28, Color.rgb(18, 13, 12)),
        SrmColor(29, Color.rgb(16, 11, 10)),
        SrmColor(30, Color.rgb(5, 11, 10)),
    )
}