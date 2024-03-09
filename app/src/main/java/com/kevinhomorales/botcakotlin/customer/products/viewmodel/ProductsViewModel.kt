package com.kevinhomorales.botcakotlin.customer.products.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse

class ProductsViewModel: ViewModel() {
    lateinit var productsResponse: ProductsResponse
}