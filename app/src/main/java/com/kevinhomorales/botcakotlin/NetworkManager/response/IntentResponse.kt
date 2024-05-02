package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IntentResponse (
    @SerializedName("client_secret")
    val clientSecret: String
): Serializable