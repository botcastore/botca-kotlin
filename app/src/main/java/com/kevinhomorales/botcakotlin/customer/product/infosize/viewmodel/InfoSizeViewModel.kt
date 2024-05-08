package com.kevinhomorales.botcakotlin.customer.product.infosize.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.customer.product.infosize.view.InfoSizeActivity

class InfoSizeViewModel: ViewModel() {
    lateinit var view: InfoSizeActivity
    lateinit var pdfName: String
}