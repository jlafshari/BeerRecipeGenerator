package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Style(val name: String) {
    override fun toString(): String {
        return name
    }
}
