package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class MashStep(val duration: Int, val targetTemperature: Int)
