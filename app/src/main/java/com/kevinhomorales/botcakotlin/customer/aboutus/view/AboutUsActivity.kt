package com.kevinhomorales.botcakotlin.customer.aboutus.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.aboutus.viewmodel.AboutUsViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityAboutUsBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants


class AboutUsActivity : MainActivity() {
    lateinit var binding: ActivityAboutUsBinding
    lateinit var viewModel: AboutUsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.about_us_title_view)
        viewModel = ViewModelProvider(this).get(AboutUsViewModel::class.java)
        viewModel.view = this
        setUpActions()
    }

    private fun setUpActions() {
        binding.contactId.setOnClickListener {
            openEmail()
        }
    }

    private fun openEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("plain/text")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_botca)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.body))
        startActivity(Intent.createChooser(intent, Constants.clearString))
    }
}