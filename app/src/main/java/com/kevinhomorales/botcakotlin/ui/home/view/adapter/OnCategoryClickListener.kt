package com.kevinhomorales.botcakotlin.ui.home.view.adapter

import com.kevinhomorales.botcakotlin.ui.home.services.response.Category

interface OnCategoryClickListener {
    fun categoryClick(category: Category)
}