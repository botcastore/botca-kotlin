package com.kevinhomorales.botcakotlin.customer.product.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.customer.product.view.ProductActivity

class ProductViewModel: ViewModel() {
    lateinit var view: ProductActivity
    lateinit var product: Product
}