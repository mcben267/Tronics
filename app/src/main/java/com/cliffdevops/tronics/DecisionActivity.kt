package com.cliffdevops.tronics

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DecisionActivity : AppCompatActivity() {
    private val prefKey = "prefSecret"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decision)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            prefKey,
            Context.MODE_PRIVATE
        )
        val ownerTxt: TextView = findViewById(R.id.txtOwner)
        val ownerIdTxt: TextView = findViewById(R.id.txtOwnerId)
        val deviceModelTxt: TextView = findViewById(R.id.lblModel)
        val deviceSerialTxt: TextView = findViewById(R.id.txtDeviceSerial)
        val devicePicTxt: TextView = findViewById(R.id.lblPic)
        val timeStampTxt: TextView = findViewById(R.id.txtTimeStamp)
        val deviceSignOut: Button = findViewById(R.id.SignOutDevice)
        val deviceSignIN: Button = findViewById(R.id.SignInDevice)

        val loggedUser: String? = sharedPreferences.getString("staff_id", "")
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timeStamp = current.format(formatter)

        ownerTxt.text = intent.getStringExtra("owner")
        ownerIdTxt.text = intent.getStringExtra("id")
        deviceSerialTxt.text = intent.getStringExtra("serial")
        deviceModelTxt.text = intent.getStringExtra("model")
        devicePicTxt.text = intent.getStringExtra("pic")
        timeStampTxt.text = timeStamp.toString()

        deviceSignIN.setOnClickListener {

            if (loggedUser != null) {
                deviceIn(
                    "signIn",
                    ownerIdTxt.text.toString(),
                    deviceModelTxt.text.toString(),
                    devicePicTxt.text.toString(),
                    deviceSerialTxt.text.toString(),
                    timeStampTxt.text.toString(),
                    loggedUser
                )
            }
        }
        deviceSignOut.setOnClickListener {
            if (loggedUser != null) {
                deviceIn(
                    "signOut",
                    ownerIdTxt.text.toString(),
                    deviceModelTxt.text.toString(),
                    devicePicTxt.text.toString(),
                    deviceSerialTxt.text.toString(),
                    timeStampTxt.text.toString(),
                    loggedUser
                )
            }
        }

    }

    private fun deviceIn(
        logType: String,
        ownerID: String,
        deviceModel: String,
        picUrl: String,
        serialNum: String,
        timestamp: String,
        guardID: String
    ) {

        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/tronics/mobile/tronics_devicelog.php"

        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val strResp = response.toString()
                    Log.d("API_M", "Response-data: $strResp")

                    val obj = JSONObject(response)
                    val success = obj.getString("success")
                    val message = obj.getString("message")
                    if (success == "1") {
                        Toast.makeText(this, "Successfully $message", Toast.LENGTH_SHORT).show()
                    } else if (success == "0") {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)

                },
                Response.ErrorListener { error ->
                    Log.d("API_M", "error => $error")
                    Toast.makeText(
                        this, "Error: $error",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val map = HashMap<String, String>()
                    map["logType"] = logType
                    map["ownerID"] = ownerID
                    map["deviceModel"] = deviceModel
                    map["devicePic"] = picUrl
                    map["serialNum"] = serialNum
                    map["timestamp"] = timestamp
                    map["guardID"] = guardID
                    return map
                }
            }
        queue.add(stringReq)
    }

}