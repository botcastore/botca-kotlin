package com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.adapter

import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentMethod

interface OnPaymentsMethodsClickListener {
    fun paymentsMethodsClick(paymentMethod: PaymentMethod)
}