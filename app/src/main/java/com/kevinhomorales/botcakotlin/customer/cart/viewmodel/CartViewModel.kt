package com.kevinhomorales.botcakotlin.customer.cart.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.ClearCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.IntentModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.LoginModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.PushFavoriteModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.RemoveProductFromCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UpdateProductCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UploadTransferModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.VerifyMemberModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CartAvailableRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.ClearCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CouponsListRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.IntentRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.LoginRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.PushFavoriteRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.RemoveProductFromCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.UpdateProductCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.Address
import com.kevinhomorales.botcakotlin.NetworkManager.response.Card
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
import com.kevinhomorales.botcakotlin.utils.GooglePictureQuality
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
    lateinit var address: Address
    lateinit var card: Card
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

    fun getCoupons(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_coupons))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CouponsListRequest::class.java).getCoupons("coupons/me?status=AVAILABLE", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openCouponsView(response!!)
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

    fun postIntent(cardID: String, addressID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_payment))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val intentModel = IntentModel(cardID, addressID)
            val call = mainActivity.getRetrofit().create(IntentRequest::class.java).pay("payment/intent", token!!, jsonIntent(intentModel))
            val intentReponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.payWithClientSecret(intentReponse!!.clientSecret)
                } else {
                    val error = call.errorBody()
                    val jsonObject = JSONObject(error!!.string())
                    val message = jsonObject.getString("status")
                    if (message == Constants.sessionExpired) {
                        Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                        return@runOnUiThread
                    }
                    Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                    mainActivity.hideLoading()
                }
            }
        }
    }

    private fun jsonIntent(intentModel: IntentModel): JsonObject {
        val user = JsonObject()
        user.addProperty("addressID", intentModel.addressID)
        user.addProperty("cardID", intentModel.cardID)
        return user
    }

    fun getCartAvailable(couponID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_cart))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CartAvailableRequest::class.java).getCartAvailable("\"${couponID}\"", token!!)
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

    fun uploadTransfer(model: TransferToCheckOut, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.upload_transfer))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val uploadTransferModel = UploadTransferModel(model.addressID, model.imageBase64, model.notes)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(PushFavoriteRequest::class.java).push("payment/transfers", token!!, jsonUploadTransfer(uploadTransferModel))
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

    private fun jsonUploadTransfer(model: UploadTransferModel): JsonObject {
        val user = JsonObject()
        user.addProperty("addressID", model.addressID)
        user.addProperty("imgBase64", model.imgBase64)
        if (model.notes.isNotEmpty()) {
            user.addProperty("notes", model.notes)
        }
        return user
    }
}