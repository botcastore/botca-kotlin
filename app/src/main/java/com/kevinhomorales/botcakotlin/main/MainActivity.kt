package com.kevinhomorales.botcakotlin.main

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.LoadingBinding
import com.kevinhomorales.botcakotlin.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class MainActivity: AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var binding: LoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (isOnline(this)) {
//            createAlertWarning("Sin internet, por favor revisa tu conexión")
//        }
        statusBarCustomer()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private fun statusBarCustomer()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, R.color.blackColor)
        }
    }

    fun tapHaptic() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            vibrator.vibrate(50)
        }
    }

    fun showLoading(text: String) {
        dialog = Dialog(this, R.style.Theme_Botcakotlin_NoActionBar)
        binding = LoadingBinding.inflate(layoutInflater)
        binding.textLoading.text = text
        binding
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun hideLoading() {
        dialog.dismiss()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.enviroment)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}