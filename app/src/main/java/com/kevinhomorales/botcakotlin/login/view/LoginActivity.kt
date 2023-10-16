package com.kevinhomorales.botcakotlin.login.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.ActivityLoginBinding
import com.kevinhomorales.botcakotlin.login.viewmodel.LoginViewModel
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants

class LoginActivity : MainActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setUpActions()
    }

    private fun setUpActions() {
        binding.googleButtonId.setOnClickListener {
            tapHaptic()
            viewModel.googleSignIn(this)
        }
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
                            viewModel.postLogin(result)
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
}