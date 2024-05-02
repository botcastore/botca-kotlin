package com.kevinhomorales.botcakotlin.customer.address.addaddress.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        val adaptadorCountries = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getCountries().map { it.country })
        adaptadorCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpinnerId.adapter = adaptadorCountries
        binding.countrySpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.countrySelected = viewModel.getCountries()[position].country
                viewModel.getProvince(viewModel.getCountries()[position].countryID, this@AddAddressActivity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }
        binding.notCityId.setOnClickListener {
            tapHaptic()
            binding.otherLayoutId.visibility = View.VISIBLE
        }
    }

    fun setUpProvinces() {
        val adaptadorProvinces = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getProvinces().map { it.province })
        adaptadorProvinces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.statePinnerId.adapter = adaptadorProvinces
        binding.statePinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.provinceSelected = viewModel.getProvinces()[position].province
                viewModel.getCities(viewModel.getProvinces()[position].provinceID, this@AddAddressActivity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }
    }

    fun setUpCities() {
        val adaptadorCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getCities().map { it.city })
        adaptadorCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinnerId.adapter = adaptadorCities
        binding.citySpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.citySelected = viewModel.getCities()[position].city
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }
    }

    private fun setUpActions() {
        binding.addAddressId.setOnClickListener {
            tapHaptic()
            postAddAddress()
        }
    }

    private fun postAddAddress() {
        val first = viewModel.getProvinces().first()
        val provinceText = viewModel.provinceSelected
        val countryText = viewModel.countrySelected
        val cityText = viewModel.citySelected
        val addressText = binding.addressEditTextId.text.toString()
        val referenceText = binding.referenceEditTextId.text.toString()
        val phoneText = binding.phoneEditTextId.text.toString()
        val deliveryByNameText = binding.deliveryByEditTextId.text.toString()
        val postalCodeText = binding.postalCodeEditTextId.text.toString()
        val dniText = binding.idEditTextId.text.toString()
        val dniInt = dniText.toIntOrNull() ?: 0
        val cityID = viewModel.getCities().filter { it.city == cityText }.first().cityID

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
        var cityIDSend = cityID
        var cityNameSend = Constants.clearString
        if (binding.otherLayoutId.visibility == View.VISIBLE) {
            cityIDSend = Constants.clearString
            cityNameSend = binding.cityEditTextId.text.toString()
        }
        val addAddressModel = AddAddressModel(
            names = deliveryByNameText,
            phoneNumber = phoneText,
            address = addressText,
            city = cityIDSend,
            cityName = cityNameSend,
            reference = referenceText,
            state = provinceText,
            dni = dniInt,
            isFav = false,
            postalCode = postalCodeText,
            country = countryText
        )
        viewModel.postAddAddress(addAddressModel, this)
    }
}