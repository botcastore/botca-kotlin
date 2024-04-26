package com.kevinhomorales.botcakotlin.customer.payments.cards.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.Card
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.view.AddCardActivity
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter.CardsAdapter
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter.OnCardsClickListener
import com.kevinhomorales.botcakotlin.customer.payments.cards.viewmodel.CardsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCardsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.CardManager
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart

class CardsActivity : MainActivity(), OnCardsClickListener {
    lateinit var binding: ActivityCardsBinding
    lateinit var viewModel: CardsViewModel
    private lateinit var cardsAdapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.cards_title_view)
        viewModel = ViewModelProvider(this).get(CardsViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.cardsReponse = intent.extras!!.get(Constants.cardsResponseKey) as CardsReponse
            viewModel.fromCart = intent!!.getBooleanExtra(Constants.cardsTransferFromCartKey, false)
        }
        if (viewModel.cardsReponse.cards.isEmpty()) {
            Alerts.warning(getString(R.string.alert_title),getString(R.string.please_add_cards),this)
        }
        cardsAdapter = CardsAdapter(this, this)
        binding.recyclerCardsId.layoutManager = LinearLayoutManager(this)
        binding.recyclerCardsId.adapter = cardsAdapter
        cardsAdapter.setListData(viewModel.cardsReponse.cards)
        cardsAdapter.notifyDataSetChanged()
        val swipeToDeleteCallBackCart = object : SwipeToDeleteCallBackCart() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cardID = viewModel.cardsReponse.cards[position].id
                viewModel.deleteCard(cardID, this@CardsActivity)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBackCart)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCardsId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cards_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_cards_id -> {
                tapHaptic()
                openAddCard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun cardsClick(card: Card) {
        if (viewModel.fromCart) {
            CardManager.shared.removeCard(this)
            CardManager.shared.saveCard(card, this)
            onBackPressed()
        }
    }

    private fun openAddCard() {
        tapHaptic()
        val intent = Intent(this, AddCardActivity::class.java)
        startActivity(intent)
    }
}