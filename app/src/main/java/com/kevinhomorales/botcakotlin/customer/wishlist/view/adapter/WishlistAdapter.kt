package com.kevinhomorales.botcakotlin.customer.wishlist.view.adapter

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
import com.kevinhomorales.botcakotlin.databinding.RowWishlistBinding

class WishlistAdapter(private val context: Context, var itemClickListener: OnWishlistClickListener): RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {
    private lateinit var binding: RowWishlistBinding
    private var dataList = mutableListOf<Product>()

    fun setListData(data: MutableList<Product>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        binding = RowWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val list = dataList[position]
        holder.bindView(list)
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    inner class WishlistViewHolder(private val itemBinding: RowWishlistBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Product) {
            Glide.with(context)
                .load(model.colors.first().images.first())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.imageViewId)
            itemBinding.nameId.text = model.name
            itemBinding.colorsId.text = model.colors.map { it.label }.joinToString(", ")
            itemBinding.sizesId.text = model.sizes.map { it.size }.joinToString(", ")
            if (model.finalPrice == null) {
                itemBinding.priceId.text = "$ ${model.price}"
            } else {
                itemBinding.priceId.text = "$ ${model.finalPrice}"
            }
            itemView.setOnClickListener { itemClickListener.wishlistClick(model) }
        }
    }
}