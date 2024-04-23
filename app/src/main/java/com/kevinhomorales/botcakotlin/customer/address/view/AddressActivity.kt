package com.kevinhomorales.botcakotlin.customer.address.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.Address
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.view.AddAddressActivity
import com.kevinhomorales.botcakotlin.customer.address.view.adapter.AddressAdapter
import com.kevinhomorales.botcakotlin.customer.address.view.adapter.OnAddressClickListener
import com.kevinhomorales.botcakotlin.customer.address.viewmodel.AddressViewModel
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.CategoryAdapter
import com.kevinhomorales.botcakotlin.customer.payments.cards.addcard.view.AddCardActivity
import com.kevinhomorales.botcakotlin.customer.profile.viewmodel.ProfileViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAddressBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants

class AddressActivity : MainActivity(), OnAddressClickListener {

    lateinit var binding: ActivityAddressBinding
    lateinit var viewModel: AddressViewModel
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.addresses_title_view)
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.addressResponse = intent.extras!!.get(Constants.addressResponseKey) as AddressResponse
        }
        if (viewModel.addressResponse.address.isEmpty()) {
            Alerts.warning(getString(R.string.alert_title),getString(R.string.please_add_address),this)
        }
        addressAdapter = AddressAdapter(this, this)
        binding.recyclerAddressId.layoutManager = LinearLayoutManager(this)
        binding.recyclerAddressId.adapter = addressAdapter
        addressAdapter.setListData(viewModel.addressResponse.address)
        addressAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.address_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_address_id -> {
                tapHaptic()
                openAddAddress()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun addressClick(address: Address) {

    }

    private fun openAddAddress() {
        tapHaptic()
        val intent = Intent(this, AddAddressActivity::class.java)
        startActivity(intent)
    }
}