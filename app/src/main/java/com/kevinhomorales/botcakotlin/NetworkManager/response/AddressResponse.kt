package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class AddressResponse (
    val address: MutableList<Address>
): Serializable

data class Address (
    val addressID: String,
    val userID: String,
    val names: String,
    val phoneNumber: String,
    val address: String,
    val cityID: String,
    val reference: String,
    val dni: Long,
    val postalCode: String? = null,
    val isFav: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val city: String,
    val provinceID: String,
    val province: String
): Serializable