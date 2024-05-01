package com.kevinhomorales.botcakotlin.customer.myorders.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersTransfersResponse

interface OnMyOrdersTransfersClickListener {
    fun myOrdersTransfersClick(model: OrderMyOrdersTransfersResponse)
}