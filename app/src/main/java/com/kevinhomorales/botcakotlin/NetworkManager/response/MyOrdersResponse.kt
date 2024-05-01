package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyOrdersResponse(
    @SerializedName("orders")
    val orders: MutableList<OrderMyOrdersResponse>,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("next_page")
    val nextPage: String?
): Serializable

data class OrderMyOrdersResponse(
    val id: String,
    val description: String?,
    val amount: String,
    val created: String,
    val currency: String,
    val customer: String,
    val status: String,
    val user: UserMyOrdersResponse
): Serializable

data class UserMyOrdersResponse(
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("avatarURL")
    val avatarURL: String
): Serializable