package com.kevinhomorales.botcakotlin.customer.product.infosize.view

import android.os.Bundle
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.product.infosize.viewmodel.InfoSizeViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityInfoSizeBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
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
        title = getString(R.string.pdf_info_title_view)
        viewModel = ViewModelProvider(this).get(InfoSizeViewModel::class.java)
        if (intent.extras != null) {
            viewModel.pdfName = intent.extras!!.getString(Constants.pdfNameKey, Constants.clearString) as String
        }
        viewModel.view = this
        binding.pdfViewId.fromAsset(viewModel.pdfName).load()
    }

}