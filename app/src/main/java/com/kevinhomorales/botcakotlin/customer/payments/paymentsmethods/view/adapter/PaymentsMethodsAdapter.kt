package com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentMethod
import com.kevinhomorales.botcakotlin.databinding.RowPaymentsMethodsBinding

class PaymentsMethodsAdapter(private val context: Context, var itemClickListener: OnPaymentsMethodsClickListener): RecyclerView.Adapter<PaymentsMethodsAdapter.PaymentsMethodsViewHolder>() {
    private lateinit var binding: RowPaymentsMethodsBinding
    private var dataList = mutableListOf<PaymentMethod>()

    fun setListData(data: MutableList<PaymentMethod>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsMethodsViewHolder {
        binding = RowPaymentsMethodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentsMethodsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentsMethodsViewHolder, position: Int) {
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

    inner class PaymentsMethodsViewHolder(private val itemBinding: RowPaymentsMethodsBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: PaymentMethod) {
            itemBinding.nameId.text = model.title
            itemView.setOnClickListener { itemClickListener.paymentsMethodsClick(model) }
        }
    }
}