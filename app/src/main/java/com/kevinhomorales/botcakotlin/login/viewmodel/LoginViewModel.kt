package com.kevinhomorales.botcakotlin.login.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Constants

final class LoginViewModel: ViewModel() {

    fun googleSignIn(activity: MainActivity) {
//        activity.showLoading(activity.getString(R.string.loading_login))
//        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(activity.getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        val googleClient = GoogleSignIn.getClient(activity, googleConf)
//        activity.startActivityForResult(googleClient.signInIntent, Constants.GOOGLE_SIGN_IN)
//        googleClient.signOut()
    }

    fun postLogin(result: Task<AuthResult>) {

    }

    fun openTermsAndPrivacyView() {

    }
}