package com.jlafshari.beerrecipegenerator.ui.login

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.okta.oidc.clients.sessions.SessionClient
import java.util.*

object AuthHelper {
    var sessionClient : SessionClient? = null
    fun getAccessToken() = sessionClient?.tokens?.accessToken

    fun getUserName() : String {
        val payload = decodeJWTTokenPayload(sessionClient?.tokens?.idToken!!)
        val user: User = jacksonObjectMapper().readValue(payload)
        return user.name
    }

    private fun decodeJWTTokenPayload(token: String): String {
        val decoder: Base64.Decoder = Base64.getDecoder()
        val chunks = token.split(".").toTypedArray()

        return String(decoder.decode(chunks[1]))
    }
}