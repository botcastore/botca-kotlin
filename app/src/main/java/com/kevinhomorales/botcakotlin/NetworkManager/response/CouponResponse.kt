package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CouponResponse (
    val coupons: MutableList<CouponsResponse>,
    val pages: Int,
    val page: Int
): Serializable

data class CouponsResponse (
    val userCouponID: String,
    val userID: String,
    val status: String,
    val code: String,
    val discountPercentage: Int,
    val endDate: Any? = null,
    val couponID: String,
    val createdAt: String,
    val updatedAt: String,
    val coupon: Coupon
): Serializable

data class Coupon (
    val couponID: String,
    val slug: String,
    val descriptions: String,
    val isGeneral: Boolean,
    val isActive: Boolean,
    val referenceCode: Any? = null,
    val percentage: Any? = null,
    val createdAt: String,
    val updatedAt: String
): Serializable