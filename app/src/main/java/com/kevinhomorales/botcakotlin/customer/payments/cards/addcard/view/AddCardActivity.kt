package com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.view

import android.os.Bundle
import android.text.InputFilter
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.model.CardUploadModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.viewmodel.AddCardViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAddCardBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.CreditCardTextWatcher
import com.kevinhomorales.botcakotlin.utils.ExpiryDateTextWatcher

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
        binding.cardNumberEditTextId.addTextChangedListener(CreditCardTextWatcher(binding.cardNumberEditTextId))
        binding.cardNumberEditTextId.filters += InputFilter.LengthFilter(19)

        binding.expDateEditTextId.addTextChangedListener(ExpiryDateTextWatcher(binding.expDateEditTextId))
        binding.expDateEditTextId.filters += InputFilter.LengthFilter(5)

        binding.cvcEditTextId.addTextChangedListener(CreditCardTextWatcher(binding.cvcEditTextId))
        binding.cvcEditTextId.filters += InputFilter.LengthFilter(3)
        binding.addCardId.setOnClickListener {
            tapHaptic()
            addCard()
        }
    }

    fun addCard() {
        if (binding.fullNameEditTextId.text.isEmpty() || binding.cardNumberEditTextId.text.isEmpty() || binding.expDateEditTextId.text.isEmpty() || binding.cvcEditTextId.text.isEmpty()) {
                    Alerts.warning(getString(R.string.alert_title), getString(R.string.complete_data), this)
                    return
                }
        val model = CardUploadModel(binding.fullNameEditTextId.text, binding.cardNumberEditTextId.text, binding.expDateEditTextId.text, binding.cvcEditTextId.text)
        viewModel.getToken(model, this)
    }
}