package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class BannersResponse (
    val banners: MutableList<Banner>
): Serializable

data class Banner (
    val bannerID: String,
    val createdBy: String,
    val url: String,
    val createdAt: String,
    val updatedAt: String
): Serializable
