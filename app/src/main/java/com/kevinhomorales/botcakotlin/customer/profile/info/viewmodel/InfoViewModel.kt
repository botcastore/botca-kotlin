package com.kevinhomorales.botcakotlin.customer.profile.info.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddressRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.DeleteRequest
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.profile.info.view.InfoActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class InfoViewModel: ViewModel() {
    lateinit var view: InfoActivity

    fun deleteAccount(mainActivity: MainActivity, model: DeleteModel) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_delete))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(DeleteRequest::class.java).delete("users/me", token!!)
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.openLoginView()
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