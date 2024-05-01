package com.kevinhomorales.botcakotlin.customer.myorders.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.MyOrdersModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.MyOrdersTransfersModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.MyOrdersRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.MyOrdersTransfersRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.MyOrdersResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.MyOrdersTransfersResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.myorders.view.MyOrdersActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MyOrdersViewModel: ViewModel() {
    lateinit var view: MyOrdersActivity
    lateinit var myOrdersResponse: MyOrdersResponse
    lateinit var myOrdersTransfersResponse: MyOrdersTransfersResponse
    lateinit var paymentsType: String

    fun getMyOrders(status: String, nextPage: String, isPagination: Boolean, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_payments))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = MyOrdersModel(status, nextPage, Constants.clearString)
        var endpoint = "orders/status/${model.status}${model.type}?page=${model.nextPage}&type=STRIPE"
        if (model.status == "redund") {
            endpoint = "orders/${model.status}${model.type}?page=${model.nextPage}&type=STRIPE"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(MyOrdersRequest::class.java).getOrders(endpoint, token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    myOrdersResponse = response!!
                    view.reloadDataMyOrders()
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

    fun getMyOrdersTransfer(status: String, nextPage: Int, isPagination: Boolean, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_payments))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = MyOrdersTransfersModel(status, "?type=transfer", nextPage)
        var endpoint = "orders/status/${model.status}${model.type}&page=${model.nextPage}"
        if (model.status == "REFUND") {
            endpoint = "orders/${model.status}${model.type}&page=${model.nextPage}"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(MyOrdersTransfersRequest::class.java).getOrders(endpoint, token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    myOrdersTransfersResponse = response!!
                    view.reloadDataMyOrdersTransfers()
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