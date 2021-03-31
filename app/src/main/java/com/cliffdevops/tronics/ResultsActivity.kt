package com.cliffdevops.tronics

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
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
import com.bumptech.glide.Glide
import org.json.JSONObject


class ResultsActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userMajor: TextView
    private lateinit var userID: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val inputCode = intent.getStringExtra("codeValue")
        val exampleList = generateDummyList(500)

        userName = findViewById(R.id.lblName)
        userID = findViewById(R.id.lblStudentId)
        userMajor = findViewById(R.id.lblMajor)
        val recycler_view: RecyclerView = findViewById(R.id.recycler_view)

        recycler_view.adapter = DeviceAdapter(exampleList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        Toast.makeText(this, "data: $inputCode", Toast.LENGTH_SHORT).show()
        userDetails(inputCode)

    }

    private fun userDetails(user_code: String) {
        val sharedPrefFile = "kotlinsharedpreference"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )

        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/DUFleet/dufleet_Userdetails.php"


        var progress = ProgressDialog(this)
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
                    }

                },
                Response.ErrorListener { error ->
                    progress.hide()
                    Log.d("API_M", "error => $error")
                    Toast.makeText(
                        this, "error: $error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    var map = HashMap<String, String>()
                    map["code"] = user_code
                    return map
                }
            }
        queue.add(stringReq)
    }

    private fun userPhoto(profilePic: String) {
        val imageView: ImageView = findViewById(R.id.profilePic)
        Glide.with(this)
            .load(profilePic)
            .placeholder(R.drawable.ic_account)
            .into(imageView);
    }

    private fun generateDummyList(size: Int): List<DeviceItem> {
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
    }
}