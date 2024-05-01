package com.kevinhomorales.botcakotlin.customer.myorders.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersResponse

interface OnMyOrdersClickListener {
    fun myOrdersClick(model: OrderMyOrdersResponse)
}