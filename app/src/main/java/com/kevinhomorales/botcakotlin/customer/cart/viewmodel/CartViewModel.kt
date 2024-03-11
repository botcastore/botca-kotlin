package com.kevinhomorales.botcakotlin.customer.cart.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.Cart
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.UseCoupon
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.main.MainActivity

class CartViewModel: ViewModel() {
    lateinit var view: CartActivity
    lateinit var cartAvailableResponse: CartAvailableResponse

    fun getCart(): Cart {
        return cartAvailableResponse.cart
    }

    fun getUseCoupon(): UseCoupon? {
        return cartAvailableResponse.useCoupon
    }

    fun getTotalAmount(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.total_value)}${cartAvailableResponse.amount}"
    }

    fun getSubTotalAmount(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.sub_total_cost_value)}${cartAvailableResponse.subTotal}"
    }

    fun getDeliveryCost(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.delivery_cost_value)}${cartAvailableResponse.shipping}"
    }

    fun clearCart() {

    }
}