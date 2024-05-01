package com.kevinhomorales.botcakotlin.customer.payments.transfer.model

import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentType
import java.io.Serializable

data class TransferToCheckOut (
    var addressID: String,
    val imageBase64: String,
    val notes: String
): Serializable