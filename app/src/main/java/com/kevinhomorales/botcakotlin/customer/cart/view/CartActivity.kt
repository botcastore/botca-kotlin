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
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductCart
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.AddressActivity
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.CartAdapter
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.OnAddRestClickListener
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.OnCartClickListener
import com.kevinhomorales.botcakotlin.customer.cart.viewmodel.CartViewModel
import com.kevinhomorales.botcakotlin.customer.coupons.view.CouponsActivity
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.PaymentsMethodsActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityCartBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.AddressManager
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.CardManager
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart
import com.kevinhomorales.botcakotlin.utils.TransferManager
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
            binding.couponsTextId.text = if (useCoupon!!.slugCoupon == "FIRST_BUY") "${useCoupon.description} ${useCoupon.percentage} %" else getString(R.string.choose_discount)
            binding.couponsLayoutId.isClickable = false
            binding.discountTextId.text = "SAVE ${useCoupon.discount}"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.card = CardManager.shared.getCard(this)
        if (viewModel.card.id.isNotEmpty()) {
            binding.paymentMethodTextId.text = "${getString(R.string.card)}\n\n${viewModel.card.brand} ${viewModel.card.last4}"
        }
        viewModel.transferToCheckOut = TransferManager.shared.getTransfer(this)
        if (viewModel.transferToCheckOut.addressID.isNotEmpty()) {
            binding.paymentMethodTextId.text = getString(R.string.transfer)
        }
        viewModel.address = AddressManager.shared.getAddress(this)
        if (viewModel.address.address.isNotEmpty()) {
            binding.addressTextId.text =  viewModel.address.address
        }
    }

    private fun checkOut() {
        if (viewModel.address.address.isEmpty()) {
            Alerts.warning(getString(R.string.alert_title),"add address",this)
            return
        }

        val paymentMethodText = binding.paymentMethodTextId.text

        if (paymentMethodText.contains(getString(R.string.card))) {
             viewModel.card = CardManager.shared.getCard(this)
            if (viewModel.card.id.isEmpty()) {
                Alerts.warning(getString(R.string.alert_title),"add card",this)
                return
            }
            viewModel.postIntent(cardID = viewModel.card.id, addressID = viewModel.address.addressID)
            return
        }

        viewModel.transferToCheckOut = TransferManager.shared.getTransfer(this)
        if (binding.paymentMethodTextId.text == getString(R.string.transfer)) {
            viewModel.transferToCheckOut.addressID = viewModel.address.addressID
//            viewModel.uploadTransfer(transferToCheckOut)
            return
        }

        Alerts.warning(getString(R.string.alert_title),"alertChoosePaymentsMethods", this)
    }

    private fun setUpActions() {
        binding.checkOutId.setOnClickListener {
            tapHaptic()
            checkOut()
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
        intent.putExtra(Constants.addressFromCartKey, true)
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
        intent.putExtra(Constants.cardsTransferFromCartKey, true)
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
        if (viewModel.getUseCoupon() != null) {
            val useCoupon = viewModel.getUseCoupon()
            binding.couponsLayoutId.isClickable = false
            binding.discountTextId.text = "SAVE ${useCoupon!!.discount}"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getQuatity(cartProductID: String, quantity: Int) {
        viewModel.updateProductInCart(cartProductID, quantity, this)
    }
}