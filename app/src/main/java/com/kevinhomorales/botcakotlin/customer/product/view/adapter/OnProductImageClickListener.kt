package com.kevinhomorales.botcakotlin.customer.product.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Product

interface OnProductImageClickListener {
    fun productClick(urlString: String)

}