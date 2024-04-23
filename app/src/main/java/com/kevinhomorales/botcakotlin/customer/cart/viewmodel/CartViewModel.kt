package com.kevinhomorales.botcakotlin.customer.cart.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.ClearCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.RemoveProductFromCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UpdateProductCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.VerifyMemberModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ClearCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.RemoveProductFromCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.UpdateProductCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.Cart
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.UseCoupon
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.customer.payments.transfer.model.TransferToCheckOut
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CartViewModel: ViewModel() {
    lateinit var view: CartActivity
    lateinit var cartAvailableResponse: CartAvailableResponse
    lateinit var transferToCheckOut: TransferToCheckOut
    fun getCart(): Cart {
        return cartAvailableResponse.cart
    }

    fun getUseCoupon(): UseCoupon? {
        return cartAvailableResponse.useCoupon
    }

    fun getTotalAmount(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.total_value)}${cartAvailableResponse.amount}"
    }

    fun getSubTotalAmount(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.sub_total_cost_value)}${cartAvailableResponse.subTotal}"
    }

    fun getDeliveryCost(mainActivity: MainActivity): String {
        return "${mainActivity.getString(R.string.delivery_cost_value)}${cartAvailableResponse.shipping}"
    }

    fun updateProductInCart(cartProductID: String, quantity: Int, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.update_cart))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = UpdateProductCartModel(cartProductID, quantity)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(UpdateProductCartRequest::class.java).update("carts/${model.cartProductID}", token!!, jsonUpdateProductInCart(model))
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    cartAvailableResponse = response!!
                    view.reloadData()
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

    private fun jsonUpdateProductInCart(updateProductCartModel: UpdateProductCartModel): JsonObject {
        val model = JsonObject()
        model.addProperty("quantity", updateProductCartModel.quantity)
        return model
    }

    fun deleteProductInCart(cartProductID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.deleting_product))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = RemoveProductFromCartModel(cartProductID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(RemoveProductFromCartRequest::class.java).delete("carts/${model.cartProductID}", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    cartAvailableResponse = response!!
                    view.reloadData()
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

    fun clearCart(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.deleting_product))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = ClearCartModel(cartAvailableResponse.cart.cartID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(ClearCartRequest::class.java).delete("carts/clear/${model.cartID}", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.onBackPressed()
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

    fun getCoupons() {

    }

    fun postIntent(cardID: String, addressID: String) {

    }

    fun getCartAvailable(couponID: String) {

    }
}