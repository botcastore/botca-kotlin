package com.kevinhomorales.botcakotlin.customer.myorders.view

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.MyOrdersResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.OrderMyOrdersTransfersResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.myorders.view.adapter.MyOrdersAdapter
import com.kevinhomorales.botcakotlin.customer.myorders.view.adapter.MyOrdersTransfersAdapter
import com.kevinhomorales.botcakotlin.customer.myorders.view.adapter.OnMyOrdersClickListener
import com.kevinhomorales.botcakotlin.customer.myorders.view.adapter.OnMyOrdersTransfersClickListener
import com.kevinhomorales.botcakotlin.customer.myorders.viewmodel.MyOrdersViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityMyOrdersBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

class MyOrdersActivity : MainActivity(), OnMyOrdersClickListener, OnMyOrdersTransfersClickListener {
    lateinit var binding: ActivityMyOrdersBinding
    lateinit var viewModel: MyOrdersViewModel
    lateinit var myOrdersAdapter: MyOrdersAdapter
    lateinit var myOrdersTransfersAdapter: MyOrdersTransfersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.my_orders_title_view)
        viewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        if (intent.extras != null) {
            viewModel.myOrdersResponse = intent.extras!!.get(Constants.myOrdersResponseKey) as MyOrdersResponse
        }
        viewModel.view = this
        setUpActions()
        viewModel.paymentsType = getString(R.string.cards)
        myOrdersAdapter = MyOrdersAdapter(this, this)
        binding.recyclerMyOrdersId.layoutManager = LinearLayoutManager(this)
        binding.recyclerMyOrdersId.adapter = myOrdersAdapter
        myOrdersAdapter.setListData(viewModel.myOrdersResponse.orders)
        myOrdersAdapter.notifyDataSetChanged()
    }

    private fun setUpActions() {
        setUpRadioButtons()
    }

    fun reloadDataMyOrders() {
        hideLoading()
        binding.recyclerMyOrdersId.visibility = View.VISIBLE
        binding.recyclerMyOrdersTransfersId.visibility = View.GONE
        myOrdersAdapter = MyOrdersAdapter(this, this)
        binding.recyclerMyOrdersId.layoutManager = LinearLayoutManager(this)
        binding.recyclerMyOrdersId.adapter = myOrdersAdapter
        myOrdersAdapter.setListData(viewModel.myOrdersResponse.orders)
        myOrdersAdapter.notifyDataSetChanged()
    }

    fun reloadDataMyOrdersTransfers() {
        hideLoading()
        binding.recyclerMyOrdersId.visibility = View.GONE
        binding.recyclerMyOrdersTransfersId.visibility = View.VISIBLE
        myOrdersTransfersAdapter = MyOrdersTransfersAdapter(this, this)
        binding.recyclerMyOrdersTransfersId.layoutManager = LinearLayoutManager(this)
        binding.recyclerMyOrdersTransfersId.adapter = myOrdersTransfersAdapter
        myOrdersTransfersAdapter.setListData(viewModel.myOrdersTransfersResponse.orders)
        myOrdersTransfersAdapter.notifyDataSetChanged()
    }

    private fun setUpRadioButtons() {
        binding.paymentsTypesGroupId.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedOption = radioButton.text.toString()
            when (selectedOption) {
                getString(R.string.cards) -> {
                    viewModel.paymentsType = selectedOption
                    binding.progressId.isChecked = true
                }
                getString(R.string.transfers) -> {
                    viewModel.paymentsType = selectedOption
                    binding.progressId.isChecked = true
                }
            }
        }

        binding.statusGroupId.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedOption = radioButton.text.toString()
            when (selectedOption) {
                getString(R.string.progress) -> {
                    if (viewModel.paymentsType == getString(R.string.cards)) {
                        viewModel.getMyOrders("processing", Constants.clearString, false, this)
                        return@setOnCheckedChangeListener
                    }
                    viewModel.getMyOrdersTransfer("PROGRESS", 0, false, this)
                }

                getString(R.string.paid) -> {
                    if (viewModel.paymentsType == getString(R.string.cards)) {
                        viewModel.getMyOrders("succeeded", Constants.clearString, false, this)
                        return@setOnCheckedChangeListener
                    }
                    viewModel.getMyOrdersTransfer("SUCCESS", 0, false, this)
                }

                getString(R.string.delivered) -> {
                    if (viewModel.paymentsType == getString(R.string.cards)) {
                        viewModel.getMyOrders("succeeded", Constants.clearString, false, this)
                        return@setOnCheckedChangeListener
                    }
                    viewModel.getMyOrdersTransfer("SUCCESS", 0, false, this)
                }

                getString(R.string.refund) -> {
                    if (viewModel.paymentsType == getString(R.string.cards)) {
                        viewModel.getMyOrders("refund", Constants.clearString, false, this)
                        return@setOnCheckedChangeListener
                    }
                    viewModel.getMyOrdersTransfer("REFUND", 0, false, this)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_orders_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.email_id -> {
                tapHaptic()
                openEmail()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openEmail() {
        val url = "https://www.botca.store/contact/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun myOrdersClick(model: OrderMyOrdersResponse) {
        tapHaptic()
    }

    override fun myOrdersTransfersClick(model: OrderMyOrdersTransfersResponse) {
        tapHaptic()
    }
}