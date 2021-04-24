package com.cliffdevops.tronics

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DecisionActivity : AppCompatActivity(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decision)

        val ownerTxt: TextView = findViewById(R.id.txtOwner)
        val ownerIdTxt: TextView = findViewById(R.id.txtOwnerId)
        val deviceSerialTxt: TextView = findViewById(R.id.txtDeviceSerial)
        val timeStampTxt: TextView = findViewById(R.id.txtTimeStamp)
        val deviceSignOut: Button = findViewById(R.id.SignOutDevice)
        val deviceSignIN: Button = findViewById(R.id.SignInDevice)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timeStamp = current.format(formatter)

        ownerTxt.text = intent.getStringExtra("owner")
        ownerIdTxt.text = intent.getStringExtra("id")
        deviceSerialTxt.text = intent.getStringExtra("serial")
        timeStampTxt.text = timeStamp.toString()

        deviceSignIN.setOnClickListener(this)
        deviceSignOut.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.SignOutDevice -> {
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT)
                    .show()
            }
            R.id.SignInDevice -> {
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}