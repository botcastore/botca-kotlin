package com.kevinhomorales.botcakotlin.main

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.kevinhomorales.botcakotlin.databinding.LoadingBinding

open class MainActivity: AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var binding: LoadingBinding

    fun tapHaptic() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            vibrator.vibrate(50)
        }
    }

    fun showLoading(text: String) {
        dialog = Dialog(this, R.style.Theme_Translucent_NoTitleBar)
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
}