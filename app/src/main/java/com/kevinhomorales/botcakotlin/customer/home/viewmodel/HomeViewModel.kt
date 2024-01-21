package com.kevinhomorales.botcakotlin.customer.home.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel: ViewModel() {

    lateinit var view: HomeActivity

    var categoriesResponse: CategoriesResponse? = null

    fun getCategories(mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_categories))
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CategoriesRequest::class.java).getCategories("categorys?page=1&dataByPage=20")
            val categoriesResponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.updateTable(categoriesResponse!!)
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