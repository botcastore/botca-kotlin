package com.kevinhomorales.botcakotlin.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.kevinhomorales.botcakotlin.NetworkManager.request.VersionRequest
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.databinding.LoadingBinding
import com.kevinhomorales.botcakotlin.utils.AddressManager
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.CardManager
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.FilterProductManager
import com.kevinhomorales.botcakotlin.utils.TransferManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        getVersion()
    }

    private fun statusBarCustomer()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, R.color.whiteColor)
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
        dialog = Dialog(this, R.style.Theme_AppCompat_Translucent)
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

    fun getVersion() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(VersionRequest::class.java).getCategories("configs/mandatoryVersion")
            val response = call.body()
            runOnUiThread {
                if(call.isSuccessful) {
                    val versionResponse = response!!
                    val currentBuild = getDeviceInfo().versionCode
                    val first = versionResponse.config.first()
                    val serverBuild = first.value.toInt()
                    if (serverBuild > currentBuild) {
                        Alerts.twoOptions(getString(R.string.update_version), Constants.clearString, Constants.ok, Constants.cancel, baseContext) { isOK ->
                            if (isOK) {
                                openURL(Constants.appURL)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getDeviceInfo(): PackageInfo {
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        return  info
    }

    fun openURL(urlString: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlString)))
    }

    override fun onDestroy() {
        super.onDestroy()
        CardManager.shared.removeCard(this)
        TransferManager.shared.removeTransfer(this)
        AddressManager.shared.removeAddress(this)
        FilterProductManager.shared.removeFilterProduct(this)
    }
}