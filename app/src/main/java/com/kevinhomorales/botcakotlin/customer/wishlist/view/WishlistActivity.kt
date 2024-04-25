package com.kevinhomorales.botcakotlin.customer.wishlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.FavoritesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.adapter.AddressAdapter
import com.kevinhomorales.botcakotlin.customer.address.viewmodel.AddressViewModel
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.customer.product.view.ProductActivity
import com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter.OnWishlistClickListener
import com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter.WishlistAdapter
import com.kevinhomorales.botcakotlin.customer.wishlist.viewmodel.WishlistViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityWishlistBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart
import java.io.Serializable

class WishlistActivity : MainActivity(), OnWishlistClickListener {

    lateinit var binding: ActivityWishlistBinding
    lateinit var viewModel: WishlistViewModel
    lateinit var wishlistAdapter: WishlistAdapter
    lateinit var menu: Menu

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
        val model = CartAvailableModel(Constants.clearString)
        viewModel.checkCartAvailable(this, model)
        val swipeToDeleteCallBackCart = object : SwipeToDeleteCallBackCart() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cardID = viewModel.favoritesResponse.products[position].productID
//                viewModel.deleteCard(cardID, this@CardsActivity)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBackCart)
        itemTouchHelper.attachToRecyclerView(binding.recyclerWishlistId)
    }

    override fun wishlistClick(product: Product) {
        tapHaptic()
        viewModel.getProduct(product.productSlug, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cart_id -> {
                tapHaptic()
                val model = CartAvailableModel(Constants.clearString)
                viewModel.getCartAvailable(this, model)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openCartView(cartAvailableResponse: CartAvailableResponse) {
        val intent = Intent(this, CartActivity::class.java)
        intent.putExtra(Constants.cartAvailableResponseKey, cartAvailableResponse as Serializable)
        startActivity(intent)
    }

    fun openProductView(productResponse: ProductResponse) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra(Constants.productKey, productResponse.product as Serializable)
        startActivity(intent)
    }

    fun updateCartIcon(isEmpty: Boolean) {
        if (isEmpty) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_icon));
            return
        }
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_full_icon));
    }
}