package com.kevinhomorales.botcakotlin.customer.product.view

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.model.CartAvailableModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.CartAvailableResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Color
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.NetworkManager.response.Size
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.cart.view.CartActivity
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.CartAdapter
import com.kevinhomorales.botcakotlin.customer.coupon.view.CouponActivity
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.PaymentsMethodsActivity
import com.kevinhomorales.botcakotlin.customer.product.infosize.view.InfoSizeActivity
import com.kevinhomorales.botcakotlin.customer.product.view.adapter.OnProductImageClickListener
import com.kevinhomorales.botcakotlin.customer.product.view.adapter.ProductAdapter
import com.kevinhomorales.botcakotlin.customer.product.viewmodel.ProductViewModel
import com.kevinhomorales.botcakotlin.customer.productimage.view.ProductImageActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityProductBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.TransforColorFromHex
import java.io.File
import java.io.Serializable


class ProductActivity : MainActivity(), OnProductImageClickListener {

    lateinit var binding: ActivityProductBinding
    lateinit var viewModel: ProductViewModel
    lateinit var menu: Menu
    lateinit var productAdapter: ProductAdapter
    lateinit var colors: MutableList<Color>
    lateinit var imagesSelectedByColor: MutableList<String>

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
        if (viewModel.product.isFav) {
            binding.favoriteId.setImageResource(R.drawable.favorite_fill_icon)
        }
        if (viewModel.product.discount != null && viewModel.product.discount != 0) {
            binding.discountId.text = "% ${viewModel.product.discount!!}"
            binding.discountLayoutId.visibility = View.VISIBLE
        }
        colors = ArrayList()
        imagesSelectedByColor = ArrayList()
        setUpSizes()
     }

    private fun setUpSizes() {
        for (i in 0 until viewModel.getSizes().size) {
            val size = viewModel.getSizes()[i]
            val sizeButton = Button(this)
            sizeButton.tag = i
            sizeButton.text = size.size
            sizeButton.setBackgroundColor(getColor(R.color.whiteColor))
            sizeButton.setTextColor(getColor(R.color.blackColor))
            val layoutParams = LinearLayout.LayoutParams(
                0, // Ancho establecido en 0dp
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.weight = 1.0f
            sizeButton.layoutParams = layoutParams
            if (size.available) {
                val borderDrawable = GradientDrawable()
                borderDrawable.shape = GradientDrawable.RECTANGLE
                borderDrawable.setStroke(1, resources.getColor(android.R.color.black))
                borderDrawable.cornerRadius = 5f
                sizeButton.setBackgroundColor(getColor(R.color.whiteColor))
                sizeButton.setTextColor(getColor(R.color.blackColor))
                sizeButton.background = borderDrawable
                sizeButton.setOnClickListener { sizeButtonPressed(it) }
                binding.sizesLayoutId.addView(sizeButton)
            }

            if (sizeButton.tag == 0) {
                setUpColors(size)
                viewModel.sizeSelected = size
                val borderDrawable = GradientDrawable()
                borderDrawable.shape = GradientDrawable.RECTANGLE
                borderDrawable.setStroke(1, resources.getColor(android.R.color.black))
                borderDrawable.cornerRadius = 5f
                sizeButton.background = borderDrawable
                sizeButton.setBackgroundColor(getColor(R.color.blackColor))
                sizeButton.setTextColor(getColor(R.color.whiteColor))
            }
        }
    }

    private fun sizeButtonPressed(view: View) {
        val button = view as Button
        tapHaptic()
        colors.clear()
        binding.productCountId.text = "1 Product"
        viewModel.productCount = 1
//        addRestOutlet.value = 1
        deleteAllViewsByColors()
        val tag = button.tag as Int
        viewModel.sizeSelected = viewModel.getSizes()[tag]
        if (viewModel.sizeSelected != null) {
            val sizeSelected = viewModel.sizeSelected
            setUpColors(sizeSelected)
            binding.sizesLayoutId.forEach { view ->
                val sizeButton = view as Button
                if (sizeButton.text.toString() == sizeSelected.size) {
                    val borderDrawable = GradientDrawable()
                    borderDrawable.shape = GradientDrawable.RECTANGLE
                    borderDrawable.setStroke(1, resources.getColor(android.R.color.black))
                    borderDrawable.cornerRadius = 5f
                    sizeButton.background = borderDrawable
                    sizeButton.setBackgroundColor(getColor(R.color.blackColor))
                    sizeButton.setTextColor(getColor(R.color.whiteColor))
                    return@forEach
                }
                val borderDrawable = GradientDrawable()
                borderDrawable.shape = GradientDrawable.RECTANGLE
                borderDrawable.setStroke(1, resources.getColor(android.R.color.black))
                borderDrawable.cornerRadius = 5f
                sizeButton.setBackgroundColor(getColor(R.color.whiteColor))
                sizeButton.setTextColor(getColor(R.color.blackColor))
                sizeButton.background = borderDrawable
            }
        }
    }

    private fun setUpColors(size: Size) {
        size.colorsSizes.forEach { colorSize ->
            colors += viewModel.getColors().filter { it.colorProductID == colorSize.colorProductID }
        }
        for (i in 0 until colors.size) {
            val color = colors[i]
            val colorButton = Button(this)
            val layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.weight = 1.0f
            colorButton.layoutParams = layoutParams
            colorButton.tag = i
            colorButton.clipToOutline = true
            val hexColor = TransforColorFromHex.shared.getColorFromHex(color.code)
            val borderDrawable = GradientDrawable()
            borderDrawable.shape = GradientDrawable.RECTANGLE
            borderDrawable.setStroke(1, resources.getColor(android.R.color.black))
            borderDrawable.cornerRadius = 5f
            colorButton.background = borderDrawable
            colorButton.setBackgroundColor(hexColor)
            colorButton.setOnClickListener { colorButtonPressed(it) }
            firstSelectColor(0)
            if (colorButton.tag == 0) {
                val first = colors.first()
                setUpImages(first)
            }
            binding.colorsLayoutId.addView(colorButton)
        }
    }

    private fun colorButtonPressed(view: View) {
        tapHaptic()
        val button = view as Button
        val tag = button.tag as Int
        viewModel.colorSelected = colors[tag]
        if (viewModel.colorSelected != null) {
            val colorSelected = viewModel.colorSelected
            binding.productCountId.text = "1 product"
            viewModel.productCount = 1
//            addRestOutlet.value = 1
            Alerts.warning(getString(R.string.alert_title), "Color Selected ${colorSelected.label}", this)
            binding.colorTextId.text = "Color Selected ${colorSelected.label}"
            setUpImages(colorSelected)
        }
    }

    private fun firstSelectColor(senderTag: Int) {
        viewModel.colorSelected = colors[senderTag]
        val colorSelected = viewModel.colorSelected
        binding.colorTextId.text = "Color Selected ${colorSelected.label}"
    }

    private fun deleteAllViewsByColors() {
        binding.colorsLayoutId.removeAllViews()
    }

    private fun setUpImages(color: Color) {
        imagesSelectedByColor = color.images
        productAdapter = ProductAdapter(this, this)
        binding.imagesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.imagesRecyclerView.adapter = productAdapter
        productAdapter.setListData(imagesSelectedByColor)
        productAdapter.notifyDataSetChanged()
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
            openPDFDocument("sizes.pdf")
        }
    }

    fun openPDFDocument(filename: String) {
        Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.need_help_sizes), getString(R.string.ok), getString(R.string.cancel),this) { isOK ->
            if (isOK) {
                val intent = Intent(this, InfoSizeActivity::class.java)
                intent.putExtra(Constants.pdfNameKey, filename)
                startActivity(intent)
            }
        }
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

    override fun productClick(urlString: String) {
        tapHaptic()
        val intent = Intent(this, ProductImageActivity::class.java)
        intent.putExtra(Constants.imageURLIdKey, urlString)
        startActivity(intent)
    }

}