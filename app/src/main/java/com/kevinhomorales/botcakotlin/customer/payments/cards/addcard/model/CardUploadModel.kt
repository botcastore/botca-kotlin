package com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.model

import android.text.Editable

data class CardUploadModel(
    val cardName: Editable,
    val cardNumber: Editable,
    val expDate: Editable,
    val cvc: Editable
)