package com.cliffdevops.tronics

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cliffdevops.tronics.controller.DeviceAdapter
import com.cliffdevops.tronics.model.DeviceItem
import com.squareup.picasso.Picasso
import org.json.JSONObject


class ResultsActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userMajor: TextView
    private lateinit var userID: TextView
    private lateinit var userCode: String
    lateinit var recyclerView: RecyclerView
    val list = ArrayList<DeviceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        userCode = intent.getStringExtra("codeValue").toString()
        //val exampleList = generateDummyList(10)

        //val devicesList = userDevices(userCode)
        recyclerView = findViewById(R.id.recycler_view)


        userName = findViewById(R.id.lblName)
        userID = findViewById(R.id.lblStudentId)
        userMajor = findViewById(R.id.lblMajor)


        //updateRecyclerView(userCode)
        Toast.makeText(this, "data: $userCode", Toast.LENGTH_SHORT).show()
        userDetails(userCode)
        userDevices(userCode)

    }

    /* private fun updateRecyclerView(inputCode: String) {
         val devicesList = userDevices(inputCode)
         val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
         recyclerView.adapter = DeviceAdapter(devicesList)
         recyclerView.layoutManager = LinearLayoutManager(this)
         recyclerView.setHasFixedSize(true)
     }*/

    private fun userDetails(user_code: String) {

        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/tronics/mobile/tronics_users.php"


        val progress = ProgressDialog(this)
        progress.setMessage("fetching data...")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.show()

        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    progress.hide()
                    val strResp = response.toString()
                    Log.d("API_M", strResp)

                    val obj = JSONObject(response)
                    val success = obj.getString("success")
                    if (success == "1") {
                        val userId = obj.getString("user_id")
                        val name = obj.getString("name")
                        val major = obj.getString("major")
                        val photo = obj.getString("user_photo")

                        userName.text = name
                        userID.text = userId
                        userMajor.text = major
                        userPhoto(photo)

                    } else if (success == "0") {
                        Toast.makeText(this, "Record Not found", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
                    }

                },
                Response.ErrorListener { error ->
                    progress.hide()
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

    private fun userDevices(user_code: String) {

        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/tronics/mobile/tronics_devices.php"
        //val list = ArrayList<DeviceItem>()

        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val strResp = response.toString()
                    Log.d("API_M", "Devices: $strResp")

                    val obj = JSONObject(response)
                    val success = obj.getString("success")
                    val deviceArray = obj.getJSONArray("devices")
                    if (success == "1") {
                        Log.d("API_M", "Array loop")
                        for (i in 0 until deviceArray.length()) {
                            val deviceObject = deviceArray.getJSONObject(i)
                            val deviceVendor: String = deviceObject.optString("device_name")
                            val deviceModel = deviceObject.getString("device_model")
                            val deviceSerial = deviceObject.getString("device_serial")
                            val devicePic = deviceObject.getString("device_pic")

                            val item = DeviceItem(
                                devicePic,
                                "Model: $deviceModel",
                                "Serial: $deviceSerial",
                                "Vendor: $deviceVendor"
                            )
                            list += item


                        }
                        recyclerView.adapter = DeviceAdapter(list)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.setHasFixedSize(true)

                    } else if (success == "0") {
                        Toast.makeText(this, "No Devices Record found", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)
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

    private fun userPhoto(profilePic: String) {
        val imageView: ImageView = findViewById(R.id.profilePic)
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_account).into(imageView);
    }

    /*   private fun generateDummyList(size: Int): List<DeviceItem> {
           val list = ArrayList<DeviceItem>()
           for (i in 0 until size) {
               val drawable = when (i % 3) {
                   0 -> R.drawable.ic_account
                   1 -> R.drawable.ic_account
                   else -> R.drawable.ic_account
               }
               val item = DeviceItem(drawable, "Devices $i", "Line 2")
               list += item
           }
           return list
       }*/
}