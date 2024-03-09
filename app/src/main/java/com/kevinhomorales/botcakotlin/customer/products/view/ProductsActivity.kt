package com.kevinhomorales.botcakotlin.customer.products.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.view.AddAddressActivity
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.CategoryAdapter
import com.kevinhomorales.botcakotlin.customer.home.viewmodel.HomeViewModel
import com.kevinhomorales.botcakotlin.customer.product.view.ProductActivity
import com.kevinhomorales.botcakotlin.customer.products.view.adapter.OnProductClickListener
import com.kevinhomorales.botcakotlin.customer.products.view.adapter.ProductsAdapter
import com.kevinhomorales.botcakotlin.customer.products.viewmodel.ProductsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityProductsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.File
import java.io.Serializable

class ProductsActivity : MainActivity(), OnProductClickListener {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var viewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        if (intent.extras != null) {
            viewModel.productsResponse = intent.extras!!.get(Constants.productsResponseKey) as ProductsResponse
        }
        title = viewModel.productsResponse.products.first().category.name
        binding.descriptionId.text = viewModel.productsResponse.products.first().category.description
        productsAdapter = ProductsAdapter(this, this)
        binding.productsRecyclerId.setHasFixedSize(true)
        binding.productsRecyclerId.layoutManager = LinearLayoutManager(this)
        binding.productsRecyclerId.setLayoutManager(GridLayoutManager(this, 2))
        binding.productsRecyclerId.adapter = productsAdapter
        productsAdapter.setListData(viewModel.productsResponse.products)
        productsAdapter.notifyDataSetChanged()
    }

    override fun productClick(product: Product) {
        tapHaptic()
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra(Constants.productKey, product as Serializable)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.products_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_id -> {
                openFilterView()
                true
            }
            R.id.cart_id -> {
                openCartView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFilterView() {

    }

    private fun openCartView() {

    }

}