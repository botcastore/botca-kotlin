package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class ProvinceResponse (
    val provinces: MutableList<Province>
): Serializable

data class Province (
    val provinceID: String,
    val countryID: String,
    val province: String,
    val country: CodeCountry,
    val createdAt: String,
    val updatedAt: String
): Serializable

data class CodeCountry (
    val code: String
): Serializable