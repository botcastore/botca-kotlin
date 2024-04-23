package com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model

data class PaymentMethod (
    val title: String,
    val type: PaymentType
)

enum class PaymentType {
    creditCard, transfer
}