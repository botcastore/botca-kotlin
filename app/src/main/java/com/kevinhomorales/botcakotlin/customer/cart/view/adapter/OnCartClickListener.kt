package com.kevinhomorales.botcakotlin.customer.cart.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductCart

interface OnCartClickListener {
    fun cartClick(product: ProductCart)
}

interface OnAddRestClickListener {
    fun getQuatity(cartProductID: String, quantity: Int)
}