package com.jlafshari.beerrecipegenerator.ui.login

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(val name: String, @JsonProperty("sub") val id: String)