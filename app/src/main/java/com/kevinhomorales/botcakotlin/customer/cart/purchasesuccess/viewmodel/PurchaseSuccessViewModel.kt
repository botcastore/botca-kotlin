package com.kevinhomorales.botcakotlin.customer.cart.purchasesuccess.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.LoginResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.purchasesuccess.view.PurchaseSuccessActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class PurchaseSuccessViewModel: ViewModel() {
    lateinit var view: PurchaseSuccessActivity

    fun getCategories(mainActivity: MainActivity, includeProducts: String = "&includeProducts=true") {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_categories))
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CategoriesRequest::class.java).getCategories("categorys?page=1&dataByPage=20" + includeProducts)
            val categoriesResponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openHome(categoriesResponse!!)
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