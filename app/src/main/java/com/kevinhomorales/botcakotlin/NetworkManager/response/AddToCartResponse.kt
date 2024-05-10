package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

data class AddToCartResponse (
    val ok: String = Constants.clearString
)