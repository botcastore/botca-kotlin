package com.kevinhomorales.botcakotlin.customer.payments.transfer.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.payments.transfer.model.TransferToCheckOut
import com.kevinhomorales.botcakotlin.customer.payments.transfer.viewmodel.TransferViewModel
import com.kevinhomorales.botcakotlin.databinding.ActivityTransferBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.TransferManager

class TransferActivity : MainActivity() {
    lateinit var binding: ActivityTransferBinding
    lateinit var viewModel: TransferViewModel
    private val REQUEST_CODE_CAMERA = 200
    private lateinit var bitmap: Bitmap
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.transfer_title)
        viewModel = ViewModelProvider(this).get(TransferViewModel::class.java)
        if (intent.extras != null) {
            viewModel.fromCart = intent!!.getBooleanExtra(Constants.cardsTransferFromCartKey, false)
        }
        viewModel.view = this
        setUpActions()
        getCameraPermission()
    }

    private fun setUpActions() {
        binding.addTransferId.setOnClickListener {
            tapHaptic()
            val transferToCheckOut = TransferToCheckOut("PRUEBA ADDRESS","64","NOTAS")
            TransferManager.shared.removeTransfer(this)
            TransferManager.shared.saveTransfer(transferToCheckOut, this)
            onBackPressed()
        }
        binding.invoiceImageId.setOnClickListener {
            tapHaptic()
            selectOption()
        }
    }

    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        imageUri = data.data
        val bitmap_ = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        bitmap = bitmap_
        binding.invoiceImageId.setImageBitmap(bitmap)
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    private fun selectOption() {
        Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.please_choose_from_gallery), getString(R.string.ok), getString(R.string.cancel), this) { isOK ->
            if (isOK) {
                openGallery()
                return@twoOptions
            }
        }
    }
}