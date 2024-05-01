package com.kevinhomorales.botcakotlin.customer.productimage.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.productimage.viewmodel.ProductImageViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityProductImageBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class ProductImageActivity : MainActivity() {
    lateinit var binding: ActivityProductImageBinding
    lateinit var viewModel: ProductImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.product_image_title_view)
        viewModel = ViewModelProvider(this).get(ProductImageViewModel::class.java)
        if (intent.extras != null) {
            viewModel.imageURL = intent.extras!!.getString(Constants.imageURLIdKey, Constants.clearString) as String
        }
        viewModel.view = this
        setUpActions()
        Glide.with(this)
            .load(viewModel.imageURL)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(2))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.category_hint)
            .into(binding.imageViewId)
    }

    private fun setUpActions() {

    }
}