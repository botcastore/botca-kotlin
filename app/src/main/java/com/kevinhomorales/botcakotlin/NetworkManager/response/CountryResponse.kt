package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class CountryResponse (
    val countries: MutableList<Country>
): Serializable

data class Country (
    val countryID: String,
    val country: String,
    val code: String,
    val createdAt: String,
    val updatedAt: String
): Serializable