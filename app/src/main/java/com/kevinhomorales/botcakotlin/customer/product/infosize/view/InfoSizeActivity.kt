package com.kevinhomorales.botcakotlin.customer.product.infosize.view

import android.os.Bundle
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.product.infosize.viewmodel.InfoSizeViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityInfoSizeBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import java.io.File
import java.io.InputStream

class InfoSizeActivity : MainActivity() {

    lateinit var binding: ActivityInfoSizeBinding
    lateinit var viewModel: InfoSizeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.add_card_title_view)
        viewModel = ViewModelProvider(this).get(InfoSizeViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        val inputStream = File("gfg.txt").inputStream()
        binding.pdfViewId.fromStream(inputStream)
    }
}