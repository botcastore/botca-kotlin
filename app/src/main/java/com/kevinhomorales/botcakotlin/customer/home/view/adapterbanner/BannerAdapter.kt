package com.kevinhomorales.botcakotlin.customer.home.view.adapterbanner

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.response.Banner
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowBannerBinding

class BannerAdapter(private val context: Context): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private lateinit var binding: RowBannerBinding
    private var dataList = mutableListOf<Banner>()

    fun setListData(data: MutableList<Banner>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        binding = RowBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val bannersList = dataList[position]
        holder.bindView(bannersList)
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    inner class BannerViewHolder(private val itemBinding: RowBannerBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Banner) {
            Glide.with(context)
                .load(model.url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(2))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.category_hint)
                .into(itemBinding.bannerImageViewId)
        }
    }
}