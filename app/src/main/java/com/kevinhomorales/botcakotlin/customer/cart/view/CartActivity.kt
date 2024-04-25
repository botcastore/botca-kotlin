package com.kevinhomorales.botcakotlin.customer.cart.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductCart
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.AddressActivity
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.CartAdapter
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.OnAddRestClickListener
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.OnCartClickListener
import com.kevinhomorales.botcakotlin.customer.cart.viewmodel.CartViewModel
import com.kevinhomorales.botcakotlin.customer.coupon.view.CouponActivity
import com.kevinhomorales.botcakotlin.customer.coupons.view.CouponsActivity
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.CardsActivity
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.PaymentsMethodsActivity
import com.kevinhomorales.botcakotlin.customer.payments.transfer.model.TransferToCheckOut
import com.kevinhomorales.botcakotlin.databinding.ActivityCartBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart
import java.io.Serializable

class CartActivity : MainActivity(), OnCartClickListener, OnAddRestClickListener {

    lateinit var binding: ActivityCartBinding
    lateinit var viewModel: CartViewModel
    lateinit var cartAdapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.cart_title_view)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.cartAvailableResponse = intent.extras!!.get(Constants.cartAvailableResponseKey) as CartAvailableResponse
        }
        cartAdapter = CartAdapter(this, this, this)
        binding.productsRecyclerId.layoutManager = LinearLayoutManager(this)
        binding.productsRecyclerId.adapter = cartAdapter
        cartAdapter.setListData(viewModel.cartAvailableResponse.cart.products)
        cartAdapter.notifyDataSetChanged()
        setUpActions()
        binding.totalTextId.setText(viewModel.getTotalAmount(this))
        binding.deliveryCostTextId.setText(viewModel.getDeliveryCost(this))
        binding.subtotalTextId.setText(viewModel.getSubTotalAmount(this))
        val swipeToDeleteCallBackCart = object : SwipeToDeleteCallBackCart() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cartProductID = viewModel.cartAvailableResponse.cart.products[position].cartProductID
                viewModel.deleteProductInCart(cartProductID, this@CartActivity)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBackCart)
        itemTouchHelper.attachToRecyclerView(binding.productsRecyclerId)
        if (viewModel.getUseCoupon() != null) {
            val useCoupon = viewModel.getUseCoupon()
            binding.couponsTextId.text = if (useCoupon!!.slugCoupon == "FIRST_BUY") " ${useCoupon.percentage} %" else "Choose your discount"
            binding.couponsTextId.isClickable = false
            binding.discountTextId.text = "SAVE ${useCoupon.discount}"
        }
    }

    override fun onResume() {
        super.onResume()
        val transferToCheckOut = intent.extras!!.get(Constants.transferToCheckOutKey) as? TransferToCheckOut
        if (transferToCheckOut != null) {
            viewModel.transferToCheckOut = transferToCheckOut
            binding.paymentMethodTextId.text = "Transfer"
        }
    }

    private fun setUpActions() {
        binding.checkOutId.setOnClickListener {
            tapHaptic()
        }
        binding.paymentMethodLayoutId.setOnClickListener {
            tapHaptic()
            openPaymentsMethodsView()
        }
        binding.deliveryAddressLayoutId.setOnClickListener {
            tapHaptic()
            viewModel.getAddresses(this)
        }
        binding.couponsLayoutId.setOnClickListener {
            tapHaptic()
            viewModel.getCoupons(this)
        }
    }

    override fun cartClick(product: ProductCart) {
        tapHaptic()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cart_remove_id -> {
                tapHaptic()
                Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.delete_cart_question),getString(R.string.delete_cart),getString(R.string.cancel), this) { isOK ->
                    if (isOK) {
                        viewModel.clearCart(this)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openAddressView(addressResponse: AddressResponse) {
        val intent = Intent(this, AddressActivity::class.java)
        intent.putExtra(Constants.addressResponseKey, addressResponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openCouponsView(couponsListResponse: CouponsListResponse) {
        val intent = Intent(this, CouponsActivity::class.java)
        intent.putExtra(Constants.couponsListResponseKey, couponsListResponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openPaymentsMethodsView() {
        val intent = Intent(this, PaymentsMethodsActivity::class.java)
        startActivity(intent)
    }

    fun reloadData() {
        cartAdapter = CartAdapter(this, this, this)
        binding.productsRecyclerId.layoutManager = LinearLayoutManager(this)
        binding.productsRecyclerId.adapter = cartAdapter
        cartAdapter.setListData(viewModel.cartAvailableResponse.cart.products)
        cartAdapter.notifyDataSetChanged()
        binding.totalTextId.setText(viewModel.getTotalAmount(this))
        binding.deliveryCostTextId.setText(viewModel.getDeliveryCost(this))
        binding.subtotalTextId.setText(viewModel.getSubTotalAmount(this))
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getQuatity(cartProductID: String, quantity: Int) {
        viewModel.updateProductInCart(cartProductID, quantity, this)
    }
}