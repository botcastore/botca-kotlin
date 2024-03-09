package com.kevinhomorales.botcakotlin.customer.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.ActivityLoginBinding
import com.kevinhomorales.botcakotlin.customer.login.viewmodel.LoginViewModel
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.VerifyMemberResponse
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.customer.register.view.RegisterActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import java.io.Serializable

class LoginActivity : MainActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.view = this
        viewModel.checkIsLogged(this)
        setUpActions()
    }

    private fun setUpActions() {
        binding.googleButtonId.setOnClickListener {
            tapHaptic()
            viewModel.googleSignIn(this)
        }
        binding.termsPrivacyTextId.setOnClickListener {
            tapHaptic()
            openTermsAndPrivacyView()
        }
        binding.guestId.setOnClickListener {
            tapHaptic()
            viewModel.guestSignIn(this)
        }
    }

    fun openTermsAndPrivacyView() {
        val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.terms))
        startActivity(termsIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            viewModel.verifyMember(account, result, this)
                        } else {
                            Alerts.warning(getString(R.string.error_title), getString(R.string.error_message),this)
                            hideLoading()
                        }
                    }
                }
            } catch (e: ApiException) {
                Alerts.warning(getString(R.string.error_title), getString(R.string.google_sign_in_canceled),this)
                hideLoading()
            }
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

    fun openRegister(verifyMemberResponse: VerifyMemberResponse) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra(Constants.verifyMemberResponse, verifyMemberResponse as Serializable)
        startActivity(intent)
        hideLoading()
    }

    override fun onBackPressed() {
    }
}