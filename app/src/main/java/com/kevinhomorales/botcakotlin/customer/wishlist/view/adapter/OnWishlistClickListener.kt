package com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Product

interface OnWishlistClickListener {
    fun wishlistClick(product: Product)
}