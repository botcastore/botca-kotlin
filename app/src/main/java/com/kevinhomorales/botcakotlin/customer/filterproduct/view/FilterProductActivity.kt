package com.kevinhomorales.botcakotlin.customer.filterproduct.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.response.AvailableColorsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.viewmodel.CartViewModel
import com.kevinhomorales.botcakotlin.customer.filterproduct.model.FilterProductModel
import com.kevinhomorales.botcakotlin.customer.filterproduct.viewmodel.FilterProductViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityFilterProductBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.FilterProductManager

class FilterProductActivity : MainActivity() {
    lateinit var binding: ActivityFilterProductBinding
    lateinit var viewModel: FilterProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.filter_product_title_view)
        viewModel = ViewModelProvider(this).get(FilterProductViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.availableColorsResponse = intent.extras!!.get(Constants.availableColorsResponseKey) as AvailableColorsResponse
            viewModel.categoryId = intent.extras!!.getString(Constants.categoryIdKey, Constants.clearString) as String
        }
        viewModel.sizeSelected = viewModel.getSizes().first()
        viewModel.colorSelected = viewModel.getColors().first().label
        val adaptadorSize = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getSizes())
        adaptadorSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterBySizeSpinnerId.adapter = adaptadorSize
        binding.filterBySizeSpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.sizeSelected = viewModel.getSizes()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }

        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.getColors().map { it.label })
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterByColorSpinnerId.adapter = adaptador
        binding.filterByColorSpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.colorSelected = viewModel.getColors()[position].label
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }

        binding.searchId.setOnClickListener {
            tapHaptic()
            val filterProductModel = FilterProductModel(viewModel.categoryId, viewModel.sizeSelected, viewModel.colorSelected)
            FilterProductManager.shared.removeFilterProduct(this)
            FilterProductManager.shared.saveFilterProduct(filterProductModel, this)
            onBackPressed()
        }
    }
}