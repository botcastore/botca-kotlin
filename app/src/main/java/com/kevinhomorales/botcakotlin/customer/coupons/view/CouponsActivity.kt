package com.kevinhomorales.botcakotlin.customer.coupons.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsListResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel.AddAddressViewModel
import com.kevinhomorales.botcakotlin.customer.address.view.adapter.AddressAdapter
import com.kevinhomorales.botcakotlin.customer.cart.view.adapter.CartAdapter
import com.kevinhomorales.botcakotlin.customer.coupons.view.adapter.CouponsAdapter
import com.kevinhomorales.botcakotlin.customer.coupons.view.adapter.OnCouponsClickListener
import com.kevinhomorales.botcakotlin.customer.coupons.viewmodel.CouponsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityCouponsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants

class CouponsActivity : MainActivity(), OnCouponsClickListener {
    lateinit var binding: ActivityCouponsBinding
    lateinit var viewModel: CouponsViewModel
    lateinit var couponsAdapter: CouponsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.coupons_title_view)
        viewModel = ViewModelProvider(this).get(CouponsViewModel::class.java)
        viewModel.view = this
        if (intent.extras != null) {
            viewModel.couponsListResponse = intent.extras!!.get(Constants.couponsListResponseKey) as CouponsListResponse
        }
        couponsAdapter = CouponsAdapter(this, this)
        binding.recyclerCouponsId.layoutManager = LinearLayoutManager(this)
        binding.recyclerCouponsId.adapter = couponsAdapter
        couponsAdapter.setListData(viewModel.couponsListResponse.coupons)
        couponsAdapter.notifyDataSetChanged()
    }

    fun reloadData() {
        couponsAdapter = CouponsAdapter(this, this)
        binding.recyclerCouponsId.layoutManager = LinearLayoutManager(this)
        binding.recyclerCouponsId.adapter = couponsAdapter
        couponsAdapter.setListData(viewModel.couponsListResponse.coupons)
        couponsAdapter.notifyDataSetChanged()
    }

    override fun couponsClick(model: CouponsResponse) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.coupons_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_coupons_id -> {
                tapHaptic()
                Alerts.showAlertWithEditText(this) { reference ->
                    viewModel.couponApply(reference, this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}