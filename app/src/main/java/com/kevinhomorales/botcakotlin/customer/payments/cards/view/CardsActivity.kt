package com.kevinhomorales.botcakotlin.customer.payments.cards.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.Card
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.customer.payments.addcard.view.AddCardActivity
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter.CardsAdapter
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.adapter.OnCardsClickListener
import com.kevinhomorales.botcakotlin.customer.payments.cards.viewmodel.CardsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCardsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager

class CardsActivity : MainActivity(), OnCardsClickListener {
    lateinit var binding: ActivityCardsBinding
    lateinit var viewModel: CardsViewModel
    private lateinit var cardsAdapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.cards_title_view)
        viewModel = ViewModelProvider(this).get(CardsViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.cardsReponse = intent.extras!!.get(Constants.cardsResponseKey) as CardsReponse
        }
        cardsAdapter = CardsAdapter(this, this)
        binding.recyclerCardsId.layoutManager = LinearLayoutManager(this)
        binding.recyclerCardsId.adapter = cardsAdapter
        cardsAdapter.setListData(viewModel.cardsReponse.cards)
        cardsAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cards_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_cards_id -> {
                openAddCard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun cardsClick(card: Card) {

    }

    private fun openAddCard() {
        tapHaptic()
        val intent = Intent(this, AddCardActivity::class.java)
        startActivity(intent)
    }
}