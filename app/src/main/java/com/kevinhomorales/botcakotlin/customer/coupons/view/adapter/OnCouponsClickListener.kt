package com.kevinhomorales.botcakotlin.customer.coupons.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsResponse

interface OnCouponsClickListener {
    fun couponsClick(model: CouponsResponse)
}