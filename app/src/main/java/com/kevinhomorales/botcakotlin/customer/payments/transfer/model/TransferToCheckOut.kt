package com.kevinhomorales.botcakotlin.customer.payments.transfer.model

import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentType
import java.io.Serializable

data class TransferToCheckOut (
    val addressID: String,
    val imageBase64: String,
    val notes: String
): Serializable