package com.kevinhomorales.botcakotlin.customer.product.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CartAvailableRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.product.view.ProductActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProductViewModel: ViewModel() {
    lateinit var view: ProductActivity
    lateinit var product: Product

    fun getCartAvailable(mainActivity: MainActivity, model: CartAvailableModel) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_cart))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CartAvailableRequest::class.java).getCartAvailable("carts/available?couponsId=[${model.couponID}]", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    if (response!!.cart.products.isEmpty()) {
                        Alerts.warning(mainActivity.getString(R.string.alert_title), mainActivity.getString(R.string.no_products_in_cart),mainActivity)
                        mainActivity.hideLoading()
                        return@runOnUiThread
                    }
                    view.openCartView(response!!)
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

    fun checkCartAvailable(mainActivity: MainActivity, model: CartAvailableModel) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_cart))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CartAvailableRequest::class.java).getCartAvailable("carts/available?couponsId=[${model.couponID}]", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.updateCartIcon(response!!.cart.products.isEmpty())
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