package com.cliffdevops.tronics

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

private const val sharedPrefFile = "kotlinsharedpreference"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userIdET: EditText = findViewById(R.id.txtUserID)
        val userPassCodeET: EditText = findViewById(R.id.txtPasscode)
        val startBtn: Button = findViewById(R.id.LogInBtn)


        startBtn.setOnClickListener(View.OnClickListener {
            val userId: String = userIdET.text.toString()
            val userSecret: String = userPassCodeET.text.toString()
            if (validateInputs()) {
                postVolley(userId, userSecret)
            }

        })

    }

    private fun validateInputs(): Boolean {
        validateUser()
        validateUser()
        return validateUser() && validateSecret()
    }

    private fun validateUser(): Boolean {
        val input: EditText = findViewById(R.id.txtUserID)
        if (input.text.toString().isEmpty()) {
            input.error = "user id required"
            return false
        }
        return true
    }

    private fun validateSecret(): Boolean {
        val input: EditText = findViewById(R.id.txtPasscode)
        if (input.text.toString().isEmpty()) {
            input.error = "user id required"
            return false
        }
        return true
    }


    private fun postVolley(user_id: String, user_code: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://myloanapp.000webhostapp.com/DUFleet/dufleet_login.php"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )

        var progress = ProgressDialog(this)
        progress.setMessage("Please Wait...")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.show()

        val stringReq: StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    // response
                    progress.hide()
                    val strResp = response.toString()
                    Log.d("API_M", strResp)

                    val obj = JSONObject(response)
                    val success = obj.getString("success")
                    if (success == "1") {
                        val userId = obj.getString("user_id")
                        val name = obj.getString("name")

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("staff_id", userId.toString())
                        editor.putString("username", name.toString())
                        editor.apply()
                        editor.commit()

                        startActivity(Intent(this, HomeActivity::class.java))
                        overridePendingTransition(0, 0)

                    }else if(success == "0"){
                        Toast.makeText(this,"Invalid username/password",Toast.LENGTH_SHORT).show()
                    }

                },
                Response.ErrorListener { error ->
                    progress.hide()
                    Log.d("API_M", "error => $error")
                    Toast.makeText(this, "Invalid username/password \n error: $error",
                        Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    var map = HashMap<String, String>()
                    map["staff_id"] = user_id
                    map["password"] = user_code

                    return map
                }
            }
        queue.add(stringReq)
    }

}