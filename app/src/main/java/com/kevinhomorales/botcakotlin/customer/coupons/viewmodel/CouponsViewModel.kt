package com.kevinhomorales.botcakotlin.customer.coupons.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.customer.coupons.view.CouponsActivity

class CouponsViewModel: ViewModel() {
    lateinit var view: CouponsActivity
    lateinit var couponsListResponse: CouponsListResponse
}