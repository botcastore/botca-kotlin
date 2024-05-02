package com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddAddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.CityModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteAddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProvinceModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddAddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CityRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CountryRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.DeleteAddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ProvinceRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.City
import com.kevinhomorales.botcakotlin.NetworkManager.response.CityResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Country
import com.kevinhomorales.botcakotlin.NetworkManager.response.CountryResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Province
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProvinceResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.view.AddAddressActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddAddressViewModel: ViewModel() {
    lateinit var view: AddAddressActivity
    lateinit var countryResponse: CountryResponse
    lateinit var provinceResponse: ProvinceResponse
    lateinit var cityResponse: CityResponse

    lateinit var countrySelected: String
    var provinceSelected = Constants.clearString
    var citySelected = Constants.clearString
    fun getProvinces(): MutableList<Province> {
        return provinceResponse.provinces
    }
    fun getCities(): MutableList<City> {
        return cityResponse.cities
    }
    fun getCountries(): MutableList<Country> {
        return countryResponse.countries
    }

    fun postAddAddress(addAddressModel: AddAddressModel, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.add_address))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(AddAddressRequest::class.java).upload("address/add", token!!, jsonAddAddress(addAddressModel))
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.onBackPressed()
                    mainActivity.hideLoading()
                    return@runOnUiThread
                } else {
                    val error = call.errorBody()
                    val jsonObject = JSONObject(error!!.string())
                    val message = jsonObject.getString("status")
                    if (message == Constants.sessionExpired) {
                        Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                        return@runOnUiThread
                    }
                    Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                }
                mainActivity.hideLoading()
            }
        }
    }

    private fun jsonAddAddress(adAddressModel: AddAddressModel): JsonObject {
        val model = JsonObject()
        model.addProperty("names", adAddressModel.names)
        model.addProperty("phoneNumber", adAddressModel.phoneNumber)
        model.addProperty("address", adAddressModel.address)
        model.addProperty("state", adAddressModel.state)
        model.addProperty("dni", adAddressModel.dni)
        model.addProperty("isFav", adAddressModel.isFav)
        if (adAddressModel.city.isEmpty()) {
            model.addProperty("cityName", "${adAddressModel.cityName}, ${adAddressModel.state}, ${adAddressModel.country}")
        }
        if (adAddressModel.cityName.isEmpty()) {
            model.addProperty("cityID", adAddressModel.city)
        }
        if (!adAddressModel.reference.isEmpty()) {
            model.addProperty("reference", adAddressModel.reference)
        }
        if (!adAddressModel.postalCode.isEmpty()) {
            model.addProperty("postalCode", adAddressModel.postalCode)
        }
        return model
    }

    fun getProvince(countryID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_provinces))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = ProvinceModel(countryID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(ProvinceRequest::class.java).getProvinces("provinces?countryID=${model.countryID}",
                token
            )
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    provinceResponse = response!!
                    view.setUpProvinces()
                    mainActivity.hideLoading()
                    return@runOnUiThread
                } else {
                    val error = call.errorBody()
                    val jsonObject = JSONObject(error!!.string())
                    val message = jsonObject.getString("status")
                    if (message == Constants.sessionExpired) {
                        Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                        return@runOnUiThread
                    }
                    Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                }
                mainActivity.hideLoading()
            }
        }
    }
    fun getCities(provinceID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_cities))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = CityModel(provinceID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CityRequest::class.java).getCities("cities?provinceID=${model.provinceID}",
                token
            )
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    cityResponse = response!!
                    view.setUpCities()
                    mainActivity.hideLoading()
                    return@runOnUiThread
                } else {
                    val error = call.errorBody()
                    val jsonObject = JSONObject(error!!.string())
                    val message = jsonObject.getString("status")
                    if (message == Constants.sessionExpired) {
                        Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                        return@runOnUiThread
                    }
                    Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                }
                mainActivity.hideLoading()
            }
        }
    }
}