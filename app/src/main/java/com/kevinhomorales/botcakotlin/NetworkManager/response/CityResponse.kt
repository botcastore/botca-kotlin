package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class CityResponse (
    val cities: MutableList<City>
): Serializable

data class City (
    val cityID: String,
    val provinceID: String,
    val city: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
): Serializable