package com.kevinhomorales.botcakotlin.customer.profile.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.CardsModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.FavoritesModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.MyOrdersModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProductsModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CardsRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.FavoritesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.MyOrdersRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ProductsRequest
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.customer.profile.view.ProfileActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel: ViewModel() {

    lateinit var view: ProfileActivity

    fun getVersion(mainActivity: MainActivity): String {
        return "v${mainActivity.getDeviceInfo().versionName} Build ${mainActivity.getDeviceInfo().versionCode}"
    }

    fun getAddresses(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_addresses))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(AddressRequest::class.java).getAddresses("address", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openAddressView(response!!)
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

    fun getCards(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_cards))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CardsRequest::class.java).getCards("cards/me", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openCardsView(response!!)
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

    fun getFavorites(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_favorites))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(FavoritesRequest::class.java).getProducts("products?myFavorites=true", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    if (response!!.products.isEmpty()) {
                        Alerts.warning(mainActivity.getString(R.string.alert_title), mainActivity.getString(R.string.not_favorites), mainActivity)
                        mainActivity.hideLoading()
                        return@runOnUiThread
                    }
                    view.openFavoritesView(response!!)
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

    fun getMyOrders(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_payments))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = MyOrdersModel("processing", Constants.clearString, Constants.clearString)
        var endpoint = "orders/status/${model.status}${model.type}?page=${model.nextPage}&type=STRIPE"
        if (model.status == "redund") {
            endpoint = "orders/${model.status}${model.type}?page=${model.nextPage}&type=STRIPE"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(MyOrdersRequest::class.java).getOrders(endpoint, token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openMyOrders(response!!)
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