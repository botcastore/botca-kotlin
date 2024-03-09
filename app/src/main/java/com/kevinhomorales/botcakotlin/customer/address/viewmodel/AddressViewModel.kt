package com.kevinhomorales.botcakotlin.customer.address.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.customer.address.view.AddressActivity

class AddressViewModel: ViewModel() {
    lateinit var view: AddressActivity
    lateinit var addressResponse: AddressResponse
}