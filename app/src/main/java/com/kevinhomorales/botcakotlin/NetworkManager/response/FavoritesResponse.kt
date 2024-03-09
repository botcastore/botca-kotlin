package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FavoritesResponse (
    val products: MutableList<Product>,
    val page: Int,
    val pages: Int
): Serializable