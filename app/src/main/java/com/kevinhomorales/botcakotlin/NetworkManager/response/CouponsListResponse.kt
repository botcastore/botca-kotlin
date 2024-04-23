package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class CouponsListResponse(
    val coupons: MutableList<CouponsResponse>,
    val pages: Int,
    val page: Int
): Serializable