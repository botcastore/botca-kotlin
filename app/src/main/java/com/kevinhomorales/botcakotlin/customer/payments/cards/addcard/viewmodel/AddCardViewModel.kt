package com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.model.CardUploadModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.view.AddCardActivity
import com.kevinhomorales.botcakotlin.customer.payments.cards.stripetools.StripeToolsManager
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.stripe.android.model.CardBrand
import com.stripe.android.model.CardParams

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
                createCard(token.id)
            }
        }
    }
    private fun createCard(token: String) {
        println("Token -> ${token}")
    }
}