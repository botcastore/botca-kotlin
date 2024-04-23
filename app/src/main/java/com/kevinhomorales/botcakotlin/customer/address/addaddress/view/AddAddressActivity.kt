package com.kevinhomorales.botcakotlin.customer.address.addaddress.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel.AddAddressViewModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.viewmodel.AddCardViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAddAddressBinding
import com.kevinhomorales.botcakotlin.main.MainActivity

class AddAddressActivity : MainActivity() {
    lateinit var binding: ActivityAddAddressBinding
    lateinit var viewModel: AddAddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.add_addresses_title_view)
        viewModel = ViewModelProvider(this).get(AddAddressViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        binding.addAddressId.setOnClickListener {
            tapHaptic()
        }
    }
}