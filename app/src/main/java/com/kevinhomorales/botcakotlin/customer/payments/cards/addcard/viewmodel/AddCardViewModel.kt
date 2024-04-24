package com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddCardModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.UpdateProductCartModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddCardRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.AddressRequest
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.model.CardUploadModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.view.AddCardActivity
import com.kevinhomorales.botcakotlin.customer.payments.cards.stripetools.StripeToolsManager
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_LOGIN
import com.kevinhomorales.botcakotlin.utils.UserManager
import com.stripe.android.model.CardBrand
import com.stripe.android.model.CardParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddCardViewModel: ViewModel() {
    lateinit var view: AddCardActivity

    fun getToken(cardUploadModel: CardUploadModel, mainActivity: MainActivity) {
        view.showLoading(mainActivity.getString(R.string.uploading_card))
        val number = cardUploadModel.cardNumber.toString().replace(Constants.space, Constants.clearString)
        val expMonth = (cardUploadModel.expDate.split("/")[0].toUIntOrNull() ?: 0U).toInt()
        val expYear = (cardUploadModel.expDate.split("/")[1].toUIntOrNull() ?: 0U).toInt()
        val cvc = cardUploadModel.cvc.toString()
        val name = cardUploadModel.cardName.toString()

        val card = CardParams(number, expMonth, expYear, cvc, name, null, null, null)

        StripeToolsManager.shared.generateToken(mainActivity, card) { token, error ->
            if (error != null || token == null) {
                // Manejar el error aquí
                Alerts.warning(mainActivity.getString(R.string.error_title), mainActivity.getString(R.string.ok), mainActivity)
                view.hideLoading()
            } else {
                // Si el token no es nulo, continuar con el proceso
                createCard(token.id, mainActivity)
            }
        }
    }
    private fun createCard(tokenCard: String, mainActivity: MainActivity) {
        var token = UserManager.shared.getUser(mainActivity).me.token
        if (token == null) {
            token = GUEST_LOGIN
        }
        val addCardModel = AddCardModel(tokenCard)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(AddCardRequest::class.java).upload("cards/me", token!!, jsonAddCard(addCardModel))
            val response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    view.onBackPressed()
                    view.hideLoading()
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

    private fun jsonAddCard(addCardModel: AddCardModel): JsonObject {
        val model = JsonObject()
        model.addProperty("token", addCardModel.token)
        return model
    }
}