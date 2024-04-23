package com.kevinhomorales.botcakotlin.customer.coupons.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsResponse
import com.kevinhomorales.botcakotlin.databinding.RowCouponsBinding

class CouponsAdapter(private val context: Context, var itemClickListener: OnCouponsClickListener): RecyclerView.Adapter<CouponsAdapter.CouponsViewHolder>() {
    private lateinit var binding: RowCouponsBinding
    private var dataList = mutableListOf<CouponsResponse>()

    fun setListData(data: MutableList<CouponsResponse>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        binding = RowCouponsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponsViewHolder, position: Int) {
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

    inner class CouponsViewHolder(private val itemBinding: RowCouponsBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: CouponsResponse) {
            itemBinding.nameId.text = "% ${model.discountPercentage} OFF"
            itemBinding.descriptionId.text = model.coupon.descriptions
            itemView.setOnClickListener { itemClickListener.couponsClick(model) }
        }
    }
}