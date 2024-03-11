package com.kevinhomorales.botcakotlin.customer.product.view

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.customer.coupon.view.CouponActivity
import com.kevinhomorales.botcakotlin.customer.product.viewmodel.ProductViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityProductBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.File
import java.io.Serializable


class ProductActivity : MainActivity() {

    lateinit var binding: ActivityProductBinding
    lateinit var viewModel: ProductViewModel
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.product = intent.extras!!.get(Constants.productKey) as Product
        }
        if (viewModel.product.finalPrice == null) {
            binding.priceId.text = "$ ${viewModel.product.price}"
            binding.finalPriceId.visibility = View.GONE
        } else {
            binding.priceId.text = "$ ${viewModel.product.price}"
            binding.priceId.setTextColor(resources.getColor(R.color.redColor))
            binding.priceId.showStrikeThrough(true)
            binding.finalPriceId.text = "$ ${viewModel.product.finalPrice}"
        }
        binding.descriptionId.text = viewModel.product.description
        setUpActions()
        val model = CartAvailableModel(Constants.clearString)
        viewModel.checkCartAvailable(this, model)
    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun setUpActions() {
        binding.addCartId.setOnClickListener {
            tapHaptic()
        }
        binding.sizeGuideId.setOnClickListener {
            tapHaptic()
            openPDFDocument("")
        }
    }

    fun openPDFDocument(filename: String) {
        //Create PDF Intent
        val pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/" + filename)
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        //Create Viewer Intent
        val viewerIntent = Intent.createChooser(pdfIntent, "Open PDF")
        startActivity(viewerIntent)
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

    fun updateCartIcon(isEmpty: Boolean) {
        if (isEmpty) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_icon))
            return
        }
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.cart_full_icon))
    }

}