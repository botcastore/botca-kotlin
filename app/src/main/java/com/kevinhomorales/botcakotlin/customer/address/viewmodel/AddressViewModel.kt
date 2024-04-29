package com.kevinhomorales.botcakotlin.customer.address.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.CountryModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteAddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteCardModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UpdateProductCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CountryRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.DeleteAddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.DeleteCardRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.AddressActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddressViewModel: ViewModel() {
    lateinit var view: AddressActivity
    lateinit var addressResponse: AddressResponse
    var fromCart = false
    fun deleteAddress(addressID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.deleting_address))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = DeleteAddressModel(addressID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(DeleteAddressRequest::class.java).delete("address/remove", token!!, jsonDeleteAddress(model))
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

    private fun jsonDeleteAddress(deleteAddressModel: DeleteAddressModel): JsonObject {
        val model = JsonObject()
        model.addProperty("addressID", deleteAddressModel.addressID)
        return model
    }

    fun getCountries(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_addresses))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CountryRequest::class.java).getCountries("countries",
                token
            )
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openAddAddress(response!!)
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