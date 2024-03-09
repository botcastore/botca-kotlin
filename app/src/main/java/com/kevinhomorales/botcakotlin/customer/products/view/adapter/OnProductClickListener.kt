package com.kevinhomorales.botcakotlin.customer.products.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Product

interface OnProductClickListener {
    fun productClick(product: Product)
}