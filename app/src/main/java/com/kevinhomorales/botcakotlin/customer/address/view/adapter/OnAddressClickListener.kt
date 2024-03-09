package com.kevinhomorales.botcakotlin.customer.address.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Address

interface OnAddressClickListener {
    fun addressClick(address: Address)
}