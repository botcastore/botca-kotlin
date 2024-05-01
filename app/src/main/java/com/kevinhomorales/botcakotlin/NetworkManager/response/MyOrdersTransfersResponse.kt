package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyOrdersTransfersResponse(
    @SerializedName("orders")
    val orders: MutableList<OrderMyOrdersTransfersResponse>,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("next_page")
    val nextPage: Int
): Serializable

data class OrderMyOrdersTransfersResponse(
    val amount: String,
    val direccion: String,
    val status: String,
    val createdAt: String,
    val transferID: String,
    val cart: CartMyOrdersTransfersResponse
): Serializable

data class CartMyOrdersTransfersResponse(
    val cartID: String,
    val userID: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val user: UserMyOrdersTransfersResponse
): Serializable

data class UserMyOrdersTransfersResponse(
    val avatarURL: String,
    val displayName: String
): Serializable