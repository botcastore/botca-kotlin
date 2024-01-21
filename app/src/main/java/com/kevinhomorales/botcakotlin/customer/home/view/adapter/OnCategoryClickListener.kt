package com.kevinhomorales.botcakotlin.customer.home.view.adapter

import com.kevinhomorales.botcakotlin.NetworkManager.response.Category

interface OnCategoryClickListener {
    fun categoryClick(category: Category)
}