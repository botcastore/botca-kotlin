package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class CartAvailableResponse (
    val cart: Cart,
    val subTotal: String,
    val amount: String,
    val shipping: String,
    val useCoupon: UseCoupon,
): Serializable

data class Cart (
    val cartID: String,
    val userID: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val products: MutableList<ProductCart>
): Serializable

data class ProductCart (
    val cartProductID: String,
    val cartID: String,
    val productID: String,
    val quantity: Int,
    val size: String,
    val color: String,
    val labelColor: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val product: ProductData,
    val stock: Int
): Serializable

data class ProductData (
    val productID: String,
    val name: String,
    val price: String,
    val productSlug: String,
    val discount: Any? = null,
    val description: String
): Serializable

data class UseCoupon (
    val userCouponID: String,
    val slugCoupon: String,
    val description: String,
    val percentage: Int,
    val discount: String
): Serializable