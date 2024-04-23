package com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.CardsActivity
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentMethod
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.model.PaymentType
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.adapter.OnPaymentsMethodsClickListener
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.view.adapter.PaymentsMethodsAdapter
import com.kevinhomorales.botcakotlin.customer.payments.paymentsmethods.viewmodel.PaymentsMethodsViewModel
import com.kevinhomorales.botcakotlin.customer.payments.transfer.view.TransferActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityPaymentsMethodsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

class PaymentsMethodsActivity : MainActivity(), OnPaymentsMethodsClickListener {
    lateinit var binding: ActivityPaymentsMethodsBinding
    lateinit var viewModel: PaymentsMethodsViewModel
    lateinit var paymentsMethodsAdapter: PaymentsMethodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPaymentsMethodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.payments_methods_title)
        viewModel = ViewModelProvider(this).get(PaymentsMethodsViewModel::class.java)
        viewModel.view = this
        val dataList = mutableListOf(PaymentMethod(getString(R.string.payments_methods_card), PaymentType.creditCard), PaymentMethod(getString(R.string.payments_methods_transfer), PaymentType.transfer))
        paymentsMethodsAdapter = PaymentsMethodsAdapter(this, this)
        binding.recyclerPaymentsMethodsId.layoutManager = LinearLayoutManager(this)
        binding.recyclerPaymentsMethodsId.adapter = paymentsMethodsAdapter
        paymentsMethodsAdapter.setListData(dataList)
        paymentsMethodsAdapter.notifyDataSetChanged()
    }

    override fun paymentsMethodsClick(paymentMethod: PaymentMethod) {
        tapHaptic()
        if (paymentMethod.type == PaymentType.creditCard) {
            viewModel.getCards(this)
            return
        }
        openTransferView()
    }

    fun openCardsView(cardsReponse: CardsReponse) {
        val intent = Intent(this, CardsActivity::class.java)
        intent.putExtra(Constants.cardsResponseKey, cardsReponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openTransferView() {
        val intent = Intent(this, TransferActivity::class.java)
        startActivity(intent)
    }
}