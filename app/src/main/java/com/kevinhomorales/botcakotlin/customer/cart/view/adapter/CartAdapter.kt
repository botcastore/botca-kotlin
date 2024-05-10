package com.kevinhomorales.botcakotlin.customer.cart.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductCart
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowCartBinding
import com.kevinhomorales.botcakotlin.utils.Alerts

class CartAdapter(private val context: Context, var itemClickListener: OnCartClickListener, var itemAddRestClickListener: OnAddRestClickListener): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private lateinit var binding: RowCartBinding
    private var dataList = mutableListOf<ProductCart>()

    fun setListData(data: MutableList<ProductCart>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        binding = RowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
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

    inner class CartViewHolder(private val itemBinding: RowCartBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: ProductCart) {
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.imageViewId)
            itemBinding.nameId.text = model.product.name
            itemBinding.colorsId.text = "${context.getString(R.string.color)} ${model.labelColor}"
            itemBinding.sizesId.text = "${context.getString(R.string.size)} ${model.size}"
            if (model.product.finalPrice == null) {
                itemBinding.priceId.text = "$ ${model.product.price}"
            } else {
                itemBinding.priceId.text = "$ ${model.product.finalPrice}"
            }
            val maxStock = model.stock.toDouble()
            var productCount = model.quantity
            itemView.setOnClickListener { itemClickListener.cartClick(model) }
            itemBinding.increaseId.setOnClickListener {
                if (productCount >= maxStock) {
                    Alerts.warning(context.getString(R.string.alert_title), "There is only ${maxStock.toInt()}", context)
                    return@setOnClickListener
                }
                productCount += 1
                itemAddRestClickListener.getQuatity(model.cartProductID, productCount)
            }
            itemBinding.decreaseId.setOnClickListener {
                if (productCount >= maxStock) {
                    Alerts.warning(context.getString(R.string.alert_title), "There is only ${maxStock.toInt()}", context)
                    return@setOnClickListener
                }
                productCount -= 1
                itemAddRestClickListener.getQuatity(model.cartProductID, productCount)
            }
            binding.productCountId.text = productCount.toString()
        }
    }
}