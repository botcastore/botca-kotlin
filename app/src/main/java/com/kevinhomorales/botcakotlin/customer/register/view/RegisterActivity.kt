package com.kevinhomorales.botcakotlin.customer.register.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.VerifyMemberResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.customer.profile.viewmodel.ProfileViewModel
import com.kevinhomorales.botcakotlin.customer.register.viewmodel.RegisterViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityRegisterBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

class RegisterActivity : MainActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        setUpActions()
    }

    private fun setUpView() {
        title = getString(R.string.register_title)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        if (intent.extras != null) {
            viewModel.verifyMemberResponse = intent.extras!!.get(Constants.verifyMemberResponse) as VerifyMemberResponse
        }
        viewModel.view = this
        Glide.with(this)
            .load(viewModel.verifyMemberResponse.avatarURL)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(2))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.category_hint)
            .into(binding.imageViewId)
        binding.fullNameEditTextId.setText(viewModel.verifyMemberResponse.displayName)
        binding.emailEditTextId.setText(viewModel.verifyMemberResponse.email)

        val countryCodes = viewModel.getCountryCodes(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodes.countryCodes.map { it.flag + Constants.space + it.dialCode })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countryCodeEditTextId.adapter = adapter
        binding.countryCodeEditTextId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position) as String
                val dialCode = countryCodes.countryCodes[position].dialCode
                binding.phoneEditTextId.setText(dialCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acción cuando no hay elementos seleccionados
            }
        }
    }

    private fun setUpActions() {
        binding.registerId.setOnClickListener {
            tapHaptic()
            viewModel.completeProfile(this, binding.phoneEditTextId.text.toString())
        }
    }

    fun openHome(categoriesResponse: CategoriesResponse?) {
        val intent = Intent(this, HomeActivity::class.java)
        if (categoriesResponse != null) {
            intent.putExtra(Constants.categoriesResponseKey, categoriesResponse as Serializable)
            hideLoading()
        }
        startActivity(intent)
    }
}