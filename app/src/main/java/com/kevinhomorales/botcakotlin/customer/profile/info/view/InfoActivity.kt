package com.kevinhomorales.botcakotlin.customer.profile.info.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.DeleteModel
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.viewmodel.AddressViewModel
import com.kevinhomorales.botcakotlin.customer.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.customer.profile.info.viewmodel.InfoViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityInfoBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.UserManager

class InfoActivity : MainActivity() {

    lateinit var binding: ActivityInfoBinding
    lateinit var viewModel: InfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.info_title_view)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        binding.deleteId.setOnClickListener {
            tapHaptic()
            delete()
        }
    }

    private fun delete() {
        Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.delete_ask), getString(R.string.ok), getString(R.string.cancel), this) { isOK ->
            if (isOK) {
                val model = DeleteModel()
                viewModel.deleteAccount(this, model)
            }
        }
    }
    fun openLoginView() {
        UserManager.shared.removeUser(this)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        hideLoading()
    }
}