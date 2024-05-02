package com.kevinhomorales.botcakotlin.customer.address.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.Address
import com.kevinhomorales.botcakotlin.databinding.RowAddressBinding

class AddressAdapter(private val context: Context, var itemClickListener: OnAddressClickListener): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private lateinit var binding: RowAddressBinding
    private var dataList = mutableListOf<Address>()

    fun setListData(data: MutableList<Address>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        binding = RowAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
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

    inner class AddressViewHolder(private val itemBinding: RowAddressBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Address) {
            itemBinding.addressId.text = model.address
            if (model.city == null) {
                itemBinding.cityId.text = model.cityName
            } else {
                itemBinding.cityId.text = model.city
            }
            itemView.setOnClickListener { itemClickListener.addressClick(model) }
        }
    }
}