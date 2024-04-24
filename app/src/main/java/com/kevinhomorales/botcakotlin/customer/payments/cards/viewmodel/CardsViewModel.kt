package com.kevinhomorales.botcakotlin.customer.payments.cards.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteCardModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.RemoveProductFromCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.DeleteCardRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.RemoveProductFromCartRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.CardsActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CardsViewModel: ViewModel() {
    lateinit var view: CardsActivity
    lateinit var cardsReponse: CardsReponse

    fun deleteCard(cardID: String, mainActivity: MainActivity) {
        mainActivity.showLoading(mainActivity.getString(R.string.deleting_card))
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val model = DeleteCardModel(cardID)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(DeleteCardRequest::class.java).delete("cards/me/${model.cardID}", token!!)
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
}