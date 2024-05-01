package com.kevinhomorales.botcakotlin.NetworkManager.model

import com.kevinhomorales.botcakotlin.utils.Constants

data class MyOrdersModel (
    val status: String,
    val type: String,
    val nextPage: String
)