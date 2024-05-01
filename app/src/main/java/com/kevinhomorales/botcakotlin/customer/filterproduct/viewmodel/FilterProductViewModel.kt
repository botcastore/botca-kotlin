package com.kevinhomorales.botcakotlin.customer.filterproduct.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProductsModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.ProductsRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.AvailableColorsResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ColorAvailable
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.filterproduct.view.FilterProductActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class FilterProductViewModel: ViewModel() {
    lateinit var view: FilterProductActivity
    lateinit var availableColorsResponse: AvailableColorsResponse
    lateinit var categoryId: String
    lateinit var sizeSelected: String
    lateinit var colorSelected: String

    fun getSizes(): MutableList<String> {
        return mutableListOf("ONESIZE","XS", "S", "S/M", "M", "M/L", "L")
    }

    fun getColors(): MutableList<ColorAvailable> {
        return availableColorsResponse.colors
    }

}