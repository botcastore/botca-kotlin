package com.kevinhomorales.botcakotlin.customer.address.addaddress.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddAddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CountryResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel.AddAddressViewModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.viewmodel.AddCardViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAddAddressBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager

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
        if (intent.extras != null) {
            viewModel.countryResponse = intent.extras!!.get(Constants.countriesResponseKey) as CountryResponse
        }
        viewModel.view = this
        setUpActions()
        binding.phoneEditTextId.setText(UserManager.shared.getUser(this).me.user.phoneNumber)
        binding.deliveryByEditTextId.setText(UserManager.shared.getUser(this).me.user.displayName)
    }

    private fun setUpActions() {
        binding.addAddressId.setOnClickListener {
            tapHaptic()
            postAddAddress()
        }
    }

    private fun postAddAddress() {
        val first = viewModel.getProvinces().first()
        val provinceText = binding.stateEditTextId.text.toString()
        val cityText = binding.cityEditTextId.text.toString()
        val addressText = binding.addressEditTextId.text.toString()
        val referenceText = binding.referenceEditTextId.text.toString()
        val phoneText = binding.phoneEditTextId.text.toString()
        val deliveryByNameText = binding.deliveryByEditTextId.text.toString()
        val postalCodeText = binding.postalCodeEditTextId.text.toString()
        val dniText = binding.idEditTextId.text.toString()
        val dniInt = dniText.toIntOrNull() ?: 0
        if (provinceText.isEmpty() ||
            cityText.isEmpty() ||
            addressText.isEmpty() ||
            phoneText.isEmpty() ||
            deliveryByNameText.isEmpty() ||
            (dniText.isEmpty() && first.country.code != "US")
        ) {
            Alerts.warning(getString(R.string.alert_title), getString(R.string.complete_data), this)
            return
        }
        val addAddressModel = AddAddressModel(
            names = deliveryByNameText,
            phoneNumber = phoneText,
            address = addressText,
            city = cityText,
            reference = referenceText,
            state = provinceText,
            dni = dniInt,
            isFav = false,
            postalCode = postalCodeText
        )
        viewModel.postAddAddress(addAddressModel, this)
    }
}