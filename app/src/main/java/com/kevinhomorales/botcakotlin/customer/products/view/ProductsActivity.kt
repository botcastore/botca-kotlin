package com.kevinhomorales.botcakotlin.customer.products.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.customer.product.view.ProductActivity
import com.kevinhomorales.botcakotlin.customer.products.view.adapter.OnProductClickListener
import com.kevinhomorales.botcakotlin.customer.products.view.adapter.ProductsAdapter
import com.kevinhomorales.botcakotlin.customer.products.viewmodel.ProductsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityProductsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

class ProductsActivity : MainActivity(), OnProductClickListener {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var viewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    lateinit var menu: Menu
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
        viewModel.view = this
        title = viewModel.productsResponse.products.first().category.name
        binding.descriptionId.text = viewModel.productsResponse.products.first().category.description
        productsAdapter = ProductsAdapter(this, this)
        binding.productsRecyclerId.setHasFixedSize(true)
        binding.productsRecyclerId.layoutManager = LinearLayoutManager(this)
        binding.productsRecyclerId.setLayoutManager(GridLayoutManager(this, 2))
        binding.productsRecyclerId.adapter = productsAdapter
        productsAdapter.setListData(viewModel.productsResponse.products)
        productsAdapter.notifyDataSetChanged()
        val model = CartAvailableModel(Constants.clearString)
        viewModel.checkCartAvailable(this, model)
    }

    override fun productClick(product: Product) {
        tapHaptic()
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra(Constants.productKey, product as Serializable)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.products_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_id -> {
                tapHaptic()
                openFilterView()
                true
            }
            R.id.cart_id -> {
                tapHaptic()
                val model = CartAvailableModel(Constants.clearString)
                viewModel.getCartAvailable(this, model)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openFilterView() {

    }

    fun openCartView(cartAvailableResponse: CartAvailableResponse) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    fun updateCartIcon(isEmpty: Boolean) {
        if (isEmpty) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_icon));
            return
        }
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_full_icon));
    }

}