package com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.Card
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.RowCardsBinding

class CardsAdapter(private val context: Context, var itemClickListener: OnCardsClickListener): RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {
    private lateinit var binding: RowCardsBinding
    private var dataList = mutableListOf<Card>()

    fun setListData(data: MutableList<Card>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        binding = RowCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
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

    inner class CardsViewHolder(private val itemBinding: RowCardsBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(model: Card) {
            itemBinding.nameId.text = model.name
            itemBinding.expDateId.text = "${context.getString(R.string.exp_date)} ${model.expMonth}" + "/" + "${model.expYear}"
            itemBinding.lastNumberId.text = "${context.getString(R.string.last_numbers)} ${model.last4}"
            itemView.setOnClickListener { itemClickListener.cardsClick(model) }
            setUpCardImage(model.brand)
        }

        private fun setUpCardImage(brand: String) {
            when (brand.lowercase()) {
                "visa" -> itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.visa))
                "mastercard" -> itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.mastercard))
                "diners" -> itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.diners))
                "discover" -> itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.discover))
                "amex" -> itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.amex))
                else -> {
                    itemBinding.cardImageViewId.setImageDrawable(context.getDrawable(R.drawable.visa))
                }
            }
        }
    }
}