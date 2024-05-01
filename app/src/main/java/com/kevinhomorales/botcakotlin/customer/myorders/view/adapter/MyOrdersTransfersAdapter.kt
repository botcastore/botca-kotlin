package com.kevinhomorales.botcakotlin.customer.myorders.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersTransfersResponse
import com.kevinhomorales.botcakotlin.databinding.RowMyOrdersTransfersBinding

class MyOrdersTransfersAdapter(private val context: Context, var itemClickListener: OnMyOrdersTransfersClickListener): RecyclerView.Adapter<MyOrdersTransfersAdapter.MyOrdersTransfersViewHolder>() {
    private lateinit var binding: RowMyOrdersTransfersBinding
    private var dataList = mutableListOf<OrderMyOrdersTransfersResponse>()

    fun setListData(data: MutableList<OrderMyOrdersTransfersResponse>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersTransfersViewHolder {
        binding = RowMyOrdersTransfersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrdersTransfersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOrdersTransfersViewHolder, position: Int) {
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

    inner class MyOrdersTransfersViewHolder(private val itemBinding: RowMyOrdersTransfersBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: OrderMyOrdersTransfersResponse) {
            itemBinding.nameId.text = model.cart.user.displayName
            itemBinding.amountId.text = model.amount
            itemBinding.dateId.text = model.createdAt
            itemBinding.statusId.text = model.status.uppercase()
            itemView.setOnClickListener { itemClickListener.myOrdersTransfersClick(model) }
        }
    }
}