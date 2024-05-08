package com.kevinhomorales.botcakotlin.customer.coupons.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.CouponApplyModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UpdateProductCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CouponApplyRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CouponsListRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.coupons.view.CouponsActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CouponsViewModel: ViewModel() {
    lateinit var view: CouponsActivity
    lateinit var couponsListResponse: CouponsListResponse

    fun couponApply(referenceCode: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_coupons))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val couponApplyModel = CouponApplyModel(referenceCode)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CouponApplyRequest::class.java).upload("coupons/apply", token!!, jsonCouponApply(couponApplyModel))
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    getCoupons(mainActivity)
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
    private fun jsonCouponApply(couponApplyModel: CouponApplyModel): JsonObject {
        val model = JsonObject()
        model.addProperty("referenceCode", couponApplyModel.referenceCode)
        return model
    }

    private fun getCoupons(mainActivity: MainActivity) {
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CouponsListRequest::class.java).getCoupons("coupons/me?status=AVAILABLE", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    couponsListResponse = response!!
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
}