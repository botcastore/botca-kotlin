package com.kevinhomorales.botcakotlin.profile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.main.MainActivity

class ProfileActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}