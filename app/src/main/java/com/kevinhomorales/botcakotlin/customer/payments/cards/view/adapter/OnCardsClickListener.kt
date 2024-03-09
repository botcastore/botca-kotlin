package com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Card

interface OnCardsClickListener {
    fun cardsClick(card: Card)
}