package com.kevinhomorales.botcakotlin.customer.register.model

import com.google.gson.annotations.SerializedName

data class CountryCodes (
    val countryCodes: MutableList<CountryCode>
)

data class CountryCode (
    val name: String,
    val flag: String,
    val code: String,

    @SerializedName("dial_code")
    val dialCode: String
)