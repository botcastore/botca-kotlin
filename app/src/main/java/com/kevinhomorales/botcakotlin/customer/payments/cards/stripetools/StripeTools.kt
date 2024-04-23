package com.kevinhomorales.botcakotlin.customer.payments.cards.stripetools

import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import com.stripe.android.Stripe
import com.stripe.android.core.exception.StripeException
import com.stripe.android.model.CardParams
import com.stripe.android.model.Token


class StripeToolsManager {
    private val stripeSecret = if (Constants.isProd()) {
        "pk_live_51O03fkG4xk8zqx8WxX3kHPphxok6DOxXvxsz6Xp2Epx7pFQsMHymEFA0TNyCsZsXWK6EwhFJGqKT9uiaMSixVdcn00N5XzkXKr"
    } else {
        "pk_test_51O03fkG4xk8zqx8WysrFlF2B0b8ojcJ2mpe9E1NhsR1ZGnbUC6I1T4q0D55lBupNijxjXBkts8QfnNf3tAbt7DoN00l66Jsu0V"
    }
    companion object {
        val shared = StripeToolsManager()
    }

    fun generateToken(mainActivity: MainActivity, card: CardParams, completion: (token: Token?, error: Exception?) -> Unit) {
        val stripe = Stripe(mainActivity, stripeSecret)
        try {
            val token = stripe.createCardTokenSynchronous(card, null)
            completion(token, null)
        } catch (stripeEx: StripeException) {
            completion(null, stripeEx)
        }
    }

    fun getBasicAuth(): String {
        return "Bearer $stripeSecret"
    }

}