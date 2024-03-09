package com.kevinhomorales.botcakotlin.customer.payments.addcard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.addcard.viewmodel.AddCardViewModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.viewmodel.CardsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAddCardBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class AddCardActivity : MainActivity() {
    lateinit var binding: ActivityAddCardBinding
    lateinit var viewModel: AddCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.add_card_title_view)
        viewModel = ViewModelProvider(this).get(AddCardViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        binding.addCardId.setOnClickListener {
            tapHaptic()
        }
    }
}