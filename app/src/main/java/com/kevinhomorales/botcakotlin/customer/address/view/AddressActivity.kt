package com.kevinhomorales.botcakotlin.customer.address.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevinhomorales.botcakotlin.NetworkManager.response.Address
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CountryResponse
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
import com.kevinhomorales.botcakotlin.utils.AddressManager
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.CardManager
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.SwipeToDeleteCallBackCart
import java.io.Serializable

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
            viewModel.fromCart = intent!!.getBooleanExtra(Constants.addressFromCartKey, false)
        }
        if (viewModel.addressResponse.address.isEmpty()) {
            Alerts.warning(getString(R.string.alert_title),getString(R.string.please_add_address),this)
        }
        addressAdapter = AddressAdapter(this, this)
        binding.recyclerAddressId.layoutManager = LinearLayoutManager(this)
        binding.recyclerAddressId.adapter = addressAdapter
        addressAdapter.setListData(viewModel.addressResponse.address)
        addressAdapter.notifyDataSetChanged()
        val swipeToDeleteCallBackCart = object : SwipeToDeleteCallBackCart() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val addressID = viewModel.addressResponse.address[position].addressID
                viewModel.deleteAddress(addressID, this@AddressActivity)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBackCart)
        itemTouchHelper.attachToRecyclerView(binding.recyclerAddressId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.address_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_address_id -> {
                tapHaptic()
                viewModel.getCountries(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun addressClick(address: Address) {
        if (viewModel.fromCart) {
            AddressManager.shared.removeAddress(this)
            AddressManager.shared.saveAddress(address, this)
            onBackPressed()
        }
    }

    fun openAddAddress(countryResponse: CountryResponse) {
        tapHaptic()
        val intent = Intent(this, AddAddressActivity::class.java)
        intent.putExtra(Constants.countriesResponseKey, countryResponse as Serializable)
        startActivity(intent)
    }
}