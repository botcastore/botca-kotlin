package com.kevinhomorales.botcakotlin.NetworkManager.model

data class AddAddressModel (
    val names: String,
    val phoneNumber: String,
    val address: String,
    val city: String,
    val reference: String,
    val state: String,
    val dni: Int,
    val isFav: Boolean,
    val postalCode: String
)