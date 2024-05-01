package com.kevinhomorales.botcakotlin.customer.myorders.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersResponse
import com.kevinhomorales.botcakotlin.databinding.RowMyOrdersBinding

class MyOrdersAdapter(private val context: Context, var itemClickListener: OnMyOrdersClickListener): RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {
    private lateinit var binding: RowMyOrdersBinding
    private var dataList = mutableListOf<OrderMyOrdersResponse>()

    fun setListData(data: MutableList<OrderMyOrdersResponse>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        binding = RowMyOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
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

    inner class MyOrdersViewHolder(private val itemBinding: RowMyOrdersBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: OrderMyOrdersResponse) {
            itemBinding.nameId.text = model.user.displayName
            itemBinding.amountId.text = model.amount
            itemBinding.dateId.text = model.created
            itemBinding.statusId.text = model.status.uppercase()
            itemView.setOnClickListener { itemClickListener.myOrdersClick(model) }
        }
    }
}