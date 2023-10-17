package com.kevinhomorales.botcakotlin.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.login.services.response.LoginResponse
import com.kevinhomorales.botcakotlin.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.ui.home.services.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.ui.home.services.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.ui.home.view.HomeFragment
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel : ViewModel() {

    lateinit var view: HomeFragment
    lateinit var categoriesResponse: CategoriesResponse

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