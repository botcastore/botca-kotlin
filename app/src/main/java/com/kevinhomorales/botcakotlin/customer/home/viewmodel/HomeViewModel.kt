package com.kevinhomorales.botcakotlin.customer.home.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProductsModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.BannersRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CouponMeRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ProductsRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.GUEST_TOKEN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel: ViewModel() {

    lateinit var view: HomeActivity

    var categoriesResponse: CategoriesResponse? = null

    fun getCategories(mainActivity: MainActivity, includeProducts: String = "&includeProducts=true") {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_categories))
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CategoriesRequest::class.java).getCategories("categorys?page=1&dataByPage=20" + includeProducts)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.updateTable(response!!)
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

    fun getProducts(mainActivity: MainActivity, model: ProductsModel) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_products))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(ProductsRequest::class.java).getProducts("products${model.categoryID}${model.bySize}${model.byColor}${model.incomplete}${model.page}", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    if (response!!.products.isEmpty()) {
                        Alerts.warning(mainActivity.getString(R.string.alert_title), mainActivity.getString(R.string.no_products_in_cart),mainActivity)
                        return@runOnUiThread
                    }
                    view.openProductsView(response!!)
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

    fun isGuest(mainActivity: MainActivity) : Boolean {
        return UserManager.shared.getUser(mainActivity).me.token == GUEST_TOKEN
    }

    fun getCoupons(mainActivity: MainActivity) {
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CouponMeRequest::class.java).getCoupons("coupons/me?status=AVAILABLE&slugCoupon=FIRST_BUY", token)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    val couponMeResponse = response!!
                    if (!couponMeResponse.coupons.isEmpty()) {
                        view.openCouponsView(couponMeResponse)
                    }
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
            }
        }
    }

    fun getBanners(mainActivity: MainActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(BannersRequest::class.java).getBanners("banners")
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.updateTableBanners(response!!)
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
            }
        }
    }
}