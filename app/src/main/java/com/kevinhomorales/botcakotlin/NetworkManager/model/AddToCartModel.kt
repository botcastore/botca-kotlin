package com.kevinhomorales.botcakotlin.NetworkManager.model

data class AddToCartModel (
    val productID: String,
    val quantity: Int,
    val color: String,
    val size: String,
)