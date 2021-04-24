package com.cliffdevops.tronics

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.loadingview.LoadingDialog

private const val sharedPrefFile = "kotlinsharedpreference"

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var searchCardView: CardView
    //private lateinit var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val signOut: Button = findViewById(R.id.signOutBtn)
        val signIn: Button = findViewById(R.id.signInBtn)
        val searchBtn: Button = findViewById(R.id.btnSearch)
        val gatePass: Button = findViewById(R.id.gatePassBtn)
        val viewLogs: Button = findViewById(R.id.viewLogsBtn)
        searchCardView = findViewById(R.id.searchView)
        searchCardView.visibility = View.GONE

        showUser()

        signIn.setOnClickListener(this)
        signOut.setOnClickListener(this)
        searchBtn.setOnClickListener(this)
        gatePass.setOnClickListener(this)
        viewLogs.setOnClickListener(this)

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
                searchCardView.visibility = View.VISIBLE
            }
            R.id.viewLogsBtn -> {
                startActivity(Intent(this, LogsActivity::class.java))
                overridePendingTransition(0, 0)
            }
            R.id.gatePassBtn -> {
                startActivity(Intent(this, GatepassActivity::class.java))
                overridePendingTransition(0, 0)
            }
            R.id.btnSearch -> {
                val searchText: EditText = findViewById(R.id.txtSearch)
                dialogMessage(searchText.text.toString().trim())
            }

        }

    }

    private fun dialogMessage(codeValue: String) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        val dialog = LoadingDialog.get(this).show()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("codeValue", codeValue)
            startActivity(intent)
            overridePendingTransition(0, 0)

            mAlertDialog.dismiss()
            dialog?.hide()

        }, 3000)


    }

}