package com.kevinhomorales.botcakotlin.customer.wishlist.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteAddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProductModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.PushFavoriteModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CartAvailableRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ProductRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.PushFavoriteRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.FavoritesResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.wishlist.view.WishlistActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class WishlistViewModel: ViewModel() {
    lateinit var favoritesResponse: FavoritesResponse
    lateinit var view: WishlistActivity

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

    fun getProduct(slug: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_product))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val productModel = ProductModel(slug)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(ProductRequest::class.java).getProduct("products/slug/${productModel.slug}", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openProductView(response!!)
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

    fun pushFavorite(productID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_favorites))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = PushFavoriteModel(productID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(PushFavoriteRequest::class.java).push("products/favorite", token!!, jsonPushFavorite(model))
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
    private fun jsonPushFavorite(pushFavoriteModel: PushFavoriteModel): JsonObject {
        val model = JsonObject()
        model.addProperty("productID", pushFavoriteModel.productID)
        return model
    }
}