package com.yash.foodhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.validation.Validations
import org.json.JSONObject
import java.util.HashMap

class ForgetPage : AppCompatActivity() {

    lateinit var mobile: EditText
    lateinit var password: EditText
    lateinit var bttn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_page)

        mobile = findViewById(R.id.mobile)
        password = findViewById(R.id.password)
        bttn = findViewById(R.id.bttn)

        bttn.setOnClickListener {
            if(Validations.Validatephone(mobile.text.toString())){
                mobile.error = null
                if(Validations.ValidateEmail(password.text.toString())){
                    password.error = null
                    SendApi(mobile.text.toString(),password.text.toString())
                }
                else{
                    password.error = "Invalid password"
                }
            }
            else{
                mobile.error = "Invalid username"
            }
        }

    }

    private fun SendApi(mobile: String, Password: String){
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
        val params = JSONObject()
        params.put("mobile_number",mobile)
        params.put("email",Password)

        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.POST,url,params,Response.Listener {

            try{
            val data = it.getJSONObject("data")
            val success = data.getBoolean("success")

                if(success){
                    val firstTry = data.getBoolean("first_try")
                    if(firstTry){
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Information")
                        builder.setMessage("Please check your registered Email for the OTP.")
                        builder.setPositiveButton("OK") { _,_ ->
                            val intent = Intent(this@ForgetPage,OTPpage::class.java)
                            intent.putExtra("Mobile",mobile)
                            startActivity(intent)
                        }
                        builder.create()
                        builder.show()
                    }
                    else{
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Information")
                        builder.setMessage("Please refer to the previous email for the OTP.")
                        builder.setPositiveButton("OK"){_,_ ->
                            val intent = Intent(this@ForgetPage,OTPpage::class.java)
                            intent.putExtra("Mobile",mobile)
                            startActivity(intent)
                        }
                        builder.create()
                        builder.show()
                    }
                }
                else{
                    Toast.makeText(this@ForgetPage,"Mobile number not registered",Toast.LENGTH_LONG)
                        .show()
                }
            }
            catch (e: Exception){
                Toast.makeText(this@ForgetPage,"Some error occurred",Toast.LENGTH_LONG)
                    .show()
            }


        },
        Response.ErrorListener {
            Toast.makeText(this@ForgetPage,"Some error occurred",Toast.LENGTH_LONG).show()
        }){
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