package com.cliffdevops.tronics

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 3000)

    }

}