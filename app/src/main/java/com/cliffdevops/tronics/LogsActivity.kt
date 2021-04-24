package com.cliffdevops.tronics

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        val backBtn: (ImageView) = findViewById(R.id.btnBack)
        val searchBtn: Button = findViewById(R.id.btnSearch)

        backBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
        })

        searchBtn.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT)
                .show()
        })

    }
}