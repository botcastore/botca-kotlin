package com.kevinhomorales.botcakotlin.customer.coupon.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel.AddAddressViewModel
import com.kevinhomorales.botcakotlin.customer.coupon.viewmodel.CouponViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCouponBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class CouponActivity : MainActivity() {
    lateinit var binding: ActivityCouponBinding
    lateinit var viewModel: CouponViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.coupon_title_view)
        viewModel = ViewModelProvider(this).get(CouponViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.couponResponse = intent.extras!!.get(Constants.couponResponseKey) as CouponResponse
        }
        setUpActions()
        binding.discountTextId.setText(viewModel.getCoupon())
    }

    private fun setUpActions() {
        binding.shopNowId.setOnClickListener {
            tapHaptic()
            onBackPressed()
        }
    }
}