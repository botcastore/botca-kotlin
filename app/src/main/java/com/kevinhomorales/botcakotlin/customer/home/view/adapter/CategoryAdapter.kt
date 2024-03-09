package com.kevinhomorales.botcakotlin.customer.home.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.response.Category
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowCategoryBinding

class CategoryAdapter(private val context: Context, var itemClickListener: OnCategoryClickListener): RecyclerView.Adapter<CategoryAdapter.ProductsViewHolder>() {
    private lateinit var binding: RowCategoryBinding
    private var dataList = mutableListOf<Category>()

    fun setListData(data: MutableList<Category>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ProductsViewHolder(private val itemBinding: RowCategoryBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Category) {
            Glide.with(context)
                .load(model.imageURL)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.categoryImageViewId)
            itemView.setOnClickListener { itemClickListener.categoryClick(model) }
        }
    }
}