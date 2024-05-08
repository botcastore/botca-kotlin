package com.kevinhomorales.botcakotlin.customer.product.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowProductBinding

class ProductAdapter(private val context: Context, var itemClickListener: OnProductImageClickListener): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private lateinit var binding: RowProductBinding
    private var dataList = mutableListOf<String>()

    fun setListData(data: MutableList<String>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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

    inner class ProductViewHolder(private val itemBinding: RowProductBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(urlString: String) {
            Glide.with(context)
                .load(urlString)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.productImageViewId)
            itemView.setOnClickListener { itemClickListener.productClick(urlString) }
        }
    }
}