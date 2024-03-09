package com.kevinhomorales.botcakotlin.customer.payments.cards.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.CardsActivity

class CardsViewModel: ViewModel() {
    lateinit var view: CardsActivity
    lateinit var cardsReponse: CardsReponse
}