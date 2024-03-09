package com.kevinhomorales.botcakotlin.customer.wishlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.FavoritesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.adapter.AddressAdapter
import com.kevinhomorales.botcakotlin.customer.address.viewmodel.AddressViewModel
import com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter.OnWishlistClickListener
import com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter.WishlistAdapter
import com.kevinhomorales.botcakotlin.customer.wishlist.viewmodel.WishlistViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityWishlistBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class WishlistActivity : MainActivity(), OnWishlistClickListener {

    lateinit var binding: ActivityWishlistBinding
    lateinit var viewModel: WishlistViewModel
    lateinit var wishlistAdapter: WishlistAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.wishlist_title_view)
        viewModel = ViewModelProvider(this).get(WishlistViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.favoritesResponse = intent.extras!!.get(Constants.favoritesResponseKey) as FavoritesResponse
        }
        wishlistAdapter = WishlistAdapter(this, this)
        binding.recyclerWishlistId.layoutManager = LinearLayoutManager(this)
        binding.recyclerWishlistId.adapter = wishlistAdapter
        wishlistAdapter.setListData(viewModel.favoritesResponse.products)
        wishlistAdapter.notifyDataSetChanged()
    }

    override fun wishlistClick(product: Product) {

    }
}