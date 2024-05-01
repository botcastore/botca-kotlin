package com.kevinhomorales.botcakotlin.NetworkManager.model

data class MyOrdersTransfersModel (
    val status: String,
    val type: String,
    val nextPage: Int
)