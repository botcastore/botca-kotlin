package com.kevinhomorales.botcakotlin.customer.products.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.response.Product
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowProductsBinding
import com.kevinhomorales.botcakotlin.utils.Constants

class ProductsAdapter(private val context: Context, var itemClickListener: OnProductClickListener): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    private lateinit var binding: RowProductsBinding
    private var dataList = mutableListOf<Product>()

    fun setListData(data: MutableList<Product>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        binding = RowProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val productsList = dataList[position]
        holder.bindView(productsList)
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    inner class ProductsViewHolder(private val itemBinding: RowProductsBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Product) {
            val first = model.colors.first()
            val urlString = first.images.first()
            Glide.with(context)
                .load(urlString)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.productImageViewId)
            itemBinding.productNameId.text = model.name
            itemBinding.productsSizeId.text = "${context.getString(R.string.sizes)} ${model.sizes.map { it.size }.joinToString(Constants.space)}"
            itemBinding.productPriceId.text = "$ ${model.price}"
            itemView.setOnClickListener { itemClickListener.productClick(model) }
        }
    }
}