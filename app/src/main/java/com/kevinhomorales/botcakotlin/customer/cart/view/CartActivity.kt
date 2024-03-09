package com.kevinhomorales.botcakotlin.customer.cart.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.viewmodel.CartViewModel
import com.kevinhomorales.botcakotlin.customer.product.viewmodel.ProductViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCartBinding
import com.kevinhomorales.botcakotlin.main.MainActivity

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
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
    }
}