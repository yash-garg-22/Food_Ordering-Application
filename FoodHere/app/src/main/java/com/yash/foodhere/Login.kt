package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.validation.Validations
import org.json.JSONObject
import java.util.HashMap

class Login : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var bttn: Button
    lateinit var Register: TextView
    lateinit var forget: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val Loggedin = sharedPreferences.getBoolean("isLoggedin",false)
        if(Loggedin){
            val intent = Intent(this@Login,DrawerHeader::class.java)
            startActivity(intent)
            finish()
        }
        else{
            setContentView(R.layout.activity_login)
            username = findViewById(R.id.username)
            password = findViewById(R.id.password)
            bttn = findViewById(R.id.bttn)
            Register = findViewById(R.id.Register)
            forget = findViewById(R.id.forget)




            bttn.setOnClickListener {
                if(Validations.Validatephone(username.text.toString())){
                    username.error = null

                    if(Validations.Validatepassword(password.text.toString())){
                        password.error = null
                        sendLoginRequest(username.text.toString(),password.text.toString())
                    }
                    else{
                        password.error = "Invalid password"
                    }
                }
                else{
                    username.error = "Invalid username"
                }
            }

            Register.setOnClickListener {
                val intent = Intent(this@Login,Registration::class.java)
                startActivity(intent)
            }

            forget.setOnClickListener {
                val intent = Intent(this@Login,ForgetPage::class.java)
                startActivity(intent)
            }


        }
        }






    private fun sendLoginRequest(Mobile:String,Password:String){
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/login/fetch_result"
        val params = JSONObject()
        params.put("mobile_number",Mobile)
        params.put("password",Password)

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST,url,params,
            Response.Listener {
                              val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success){
                    val response = data.getJSONObject("data")
                    sharedPreferences.edit().putBoolean("isLoggedin",true).apply()
                    sharedPreferences.edit().putString("user_id",response.getString("user_id")).apply()
                    sharedPreferences.edit().putString("user_name",response.getString("name")).apply()
                    sharedPreferences.edit().putString("user_email",response.getString("email")).apply()
                    sharedPreferences.edit().putString("user_number",response.getString("mobile_number")).apply()
                    sharedPreferences.edit().putString("user_address",response.getString("address")).apply()

                    val intent = Intent(this@Login,DrawerHeader::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this@Login,"You are not Registered",Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this@Login,"Some Error occurred",Toast.LENGTH_LONG).show()

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "594fce9074c737"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }
}

