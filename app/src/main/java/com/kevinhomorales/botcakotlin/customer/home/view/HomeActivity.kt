package com.kevinhomorales.botcakotlin.customer.home.view

import android.os.Bundle
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Category
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.CategoryAdapter
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.OnCategoryClickListener
import com.kevinhomorales.botcakotlin.databinding.ActivityHomeBinding
import com.kevinhomorales.botcakotlin.customer.home.viewmodel.HomeViewModel
import com.kevinhomorales.botcakotlin.main.MainActivity

class HomeActivity : MainActivity(), OnCategoryClickListener {

    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = "Hi Kevinho!"
    }

    fun updateTable(categoriesResponse: CategoriesResponse) {
        val category = categoriesResponse.categorys
        categoryAdapter.setListData(category)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun categoryClick(category: Category) {

    }
}