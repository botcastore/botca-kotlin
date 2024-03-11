package com.kevinhomorales.botcakotlin.customer.cart.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.viewmodel.CartViewModel
import com.kevinhomorales.botcakotlin.customer.payments.addcard.viewmodel.AddCardViewModel
import com.kevinhomorales.botcakotlin.customer.product.viewmodel.ProductViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCartBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class CartActivity : MainActivity() {

    lateinit var binding: ActivityCartBinding
    lateinit var viewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.cart_title_view)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.cartAvailableResponse = intent.extras!!.get(Constants.cartAvailableResponseKey) as CartAvailableResponse
        }
        setUpActions()
    }

    private fun setUpActions() {

    }
}