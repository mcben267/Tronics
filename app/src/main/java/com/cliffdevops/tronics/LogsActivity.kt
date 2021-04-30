package com.cliffdevops.tronics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cliffdevops.tronics.controller.LogAdapter
import com.cliffdevops.tronics.model.LogItem
import org.json.JSONObject

class LogsActivity : AppCompatActivity(), LogAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private val logList = ArrayList<LogItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        val backBtn: (ImageView) = findViewById(R.id.btnBack)
        val searchInput: EditText = findViewById(R.id.txtSearch)
        val searchBtn: Button = findViewById(R.id.btnSearch)
        recyclerView = findViewById(R.id.LogsView)

        backBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
        })

        searchBtn.setOnClickListener(View.OnClickListener {
            var inputID: String = searchInput.text.toString().trim()
            userLogs(inputID)
        })

    }

    override fun onResume() {
        super.onResume()
        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()

    }


    private fun userLogs(user_code: String) {

        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/tronics/mobile/tronics_logs.php"

        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val strResp = response.toString()
                    Log.d("API_M", "Devices: $strResp")

                    val obj = JSONObject(response)
                    val success = obj.getString("success")
                    val deviceArray = obj.getJSONArray("logs")
                    if (success == "1") {
                        Log.d("API_M", "Array loop")
                        for (i in 0 until deviceArray.length()) {
                            val deviceObject = deviceArray.getJSONObject(i)
                            val timeStamp: String = deviceObject.optString("time_stamp")
                            val deviceModel = deviceObject.getString("device_model")
                            val deviceSerial = deviceObject.getString("device_serial")
                            val devicePic = deviceObject.getString("device_pic")
                            val logStatus = deviceObject.getString("log_status")

                            val item = LogItem(
                                devicePic,
                                "Model: $deviceModel",
                                "Serial: $deviceSerial",
                                "Signed: $logStatus",
                                "Time: $timeStamp"
                            )
                            logList += item
                        }
                        recyclerView.adapter = LogAdapter(logList, this)
                        recyclerView.layoutManager = LinearLayoutManager(this)


                    } else if (success == "0") {
                        Toast.makeText(this, "No Log Record found", Toast.LENGTH_SHORT).show()
                    }

                },
                Response.ErrorListener { error ->
                    Log.d("API_M", "error => $error")
                    Toast.makeText(
                        this, "error: $error",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val map = HashMap<String, String>()
                    map["code"] = user_code
                    return map
                }
            }
        queue.add(stringReq)
    }

    override fun onItemClick(position: Int) {
        val currentItem = logList[position]
        Toast.makeText(this, "${currentItem.name}", Toast.LENGTH_SHORT)
            .show()
    }


}