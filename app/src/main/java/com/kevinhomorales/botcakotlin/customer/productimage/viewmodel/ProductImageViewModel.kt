package com.kevinhomorales.botcakotlin.customer.productimage.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.customer.productimage.view.ProductImageActivity

class ProductImageViewModel: ViewModel() {
    lateinit var view: ProductImageActivity
    lateinit var imageURL: String
}