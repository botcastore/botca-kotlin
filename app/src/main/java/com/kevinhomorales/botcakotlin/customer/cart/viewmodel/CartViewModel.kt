package com.kevinhomorales.botcakotlin.customer.cart.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity

class CartViewModel: ViewModel() {
    lateinit var view: CartActivity
    lateinit var cartAvailableResponse: CartAvailableResponse
}