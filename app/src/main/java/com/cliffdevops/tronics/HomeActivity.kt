package com.cliffdevops.tronics

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val sharedPrefFile = "kotlinsharedpreference"

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val signOut: Button = findViewById(R.id.signOutBtn)
        val signIn: Button = findViewById(R.id.signInBtn)

        showUser()

        signIn.setOnClickListener(this)
        signOut.setOnClickListener(this)

    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    private fun showUser() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val user = sharedPreferences.getString("username", "")
        val lblUser: TextView = findViewById(R.id.lblUsername)
        lblUser.text = user

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signInBtn -> {
                startActivity(Intent(this, SignInActivity::class.java))
                overridePendingTransition(0, 0)
            }
            R.id.signOutBtn -> {
                startActivity(Intent(this, SignOutActivity::class.java))
                overridePendingTransition(0, 0)
            }
            R.id.viewLogsBtn -> {
                startActivity(Intent(this, LogsActivity::class.java))
                overridePendingTransition(0, 0)
            }
            R.id.gatePassBtn -> {
                startActivity(Intent(this, GetPassActivity::class.java))
                overridePendingTransition(0, 0)
            }

        }

    }

}