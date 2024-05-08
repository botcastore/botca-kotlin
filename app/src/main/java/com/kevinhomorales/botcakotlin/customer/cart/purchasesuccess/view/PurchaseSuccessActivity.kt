package com.kevinhomorales.botcakotlin.customer.cart.purchasesuccess.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.addaddress.viewmodel.AddAddressViewModel
import com.kevinhomorales.botcakotlin.customer.cart.purchasesuccess.viewmodel.PurchaseSuccessViewModel
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityPurchaseSuccessBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

class PurchaseSuccessActivity : MainActivity() {

    lateinit var binding: ActivityPurchaseSuccessBinding
    lateinit var viewModel: PurchaseSuccessViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.purchase_success_title_view)
        viewModel = ViewModelProvider(this).get(PurchaseSuccessViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        binding.thanksButtonId.setOnClickListener {
            tapHaptic()
            viewModel.getCategories(this)
        }
    }

    fun openHome(categoriesResponse: CategoriesResponse) {
        hideLoading()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(Constants.categoriesResponseKey, categoriesResponse as Serializable)
        startActivity(intent)
    }
}