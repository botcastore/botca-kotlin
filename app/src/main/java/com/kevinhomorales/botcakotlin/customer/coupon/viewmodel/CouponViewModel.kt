package com.kevinhomorales.botcakotlin.customer.coupon.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponResponse
import com.kevinhomorales.botcakotlin.customer.coupon.view.CouponActivity

class CouponViewModel: ViewModel() {
    lateinit var view: CouponActivity
    lateinit var couponResponse: CouponResponse

    fun getCoupon(): String {
        return "${couponResponse.coupons.first().discountPercentage}%"
    }
}