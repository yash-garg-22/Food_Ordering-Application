package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.validation.Validations
import org.json.JSONObject
import java.util.*

class Registration : AppCompatActivity() {

    lateinit var edt1: EditText
    lateinit var edt2: EditText
    lateinit var edt3: EditText
    lateinit var edt4: EditText
    lateinit var edt5: EditText
    lateinit var txtRegister: TextView
    lateinit var confirm: EditText
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sharedPreferences =
            this@Registration.getSharedPreferences(getString(R.string.registration_file_name), Context.MODE_PRIVATE)

        edt1 = findViewById(R.id.edt1)
        edt2 = findViewById(R.id.edt2)
        edt3 = findViewById(R.id.edt3)
        edt4 = findViewById(R.id.edt4)
        edt5 = findViewById(R.id.edt5)
        confirm = findViewById(R.id.confirm)

        txtRegister = findViewById(R.id.txtRegister)

        txtRegister.setOnClickListener {

            if(Validations.validateNameLength(edt1.text.toString())){
                edt1.error = null

                if(Validations.Validatephone(edt2.text.toString())){
                    edt2.error = null

                    if(Validations.ValidateEmail(edt3.text.toString())){
                        edt3.error = null

                        if(Validations.Validatepassword(edt5.text.toString())){
                            edt5.error = null

                            if(Validations.ValidateAddress(edt4.text.toString())){
                                edt4.error = null

                                if(Validations.ValidatematchPassword(edt5.text.toString(),confirm.text.toString())){
                                    confirm.error = null

                                    sendRegisterRequest(edt1.text.toString(),edt2.text.toString(),edt5.text.toString(),
                                    edt4.text.toString(),edt3.text.toString())
                                }
                                else{
                                    confirm.error = "Password mismatch"
                                }
                            }
                            else{
                                edt4.error = "Invalid address"
                            }
                        }
                        else{
                            edt5.error = "Enter strong password"
                        }
                    }
                    else{
                        edt3.error = "Invalid email"
                    }
                }
                else{
                    edt2.error = "Invalid phone number"
                }
            }
            else{
                edt1.error = "Invalid name"
            }
        }
    }

    private fun sendRegisterRequest(Name: String,mobileNumber: String,passwoed: String,Add:String,Email:String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/register/fetch_result"
        val params = JSONObject()
        params.put("name",Name)
        params.put("mobile_number",mobileNumber)
        params.put("password",passwoed)
        params.put("address",Add)
        params.put("email",Email)

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST,url,params,Response.Listener {
                   val data = it.getJSONObject("data")
            val success = data.getBoolean("success")
            if(success){
                val response = data.getJSONObject("data")
                sharedPreferences.edit().putString("user_id",response.getString("user_id")).apply()
                sharedPreferences.edit().putString("user_name",response.getString("name")).apply()
                sharedPreferences.edit().putString("user_email",response.getString("email")).apply()
                sharedPreferences.edit().putString("user_mobile",response.getString("mobile_number")).apply()
                sharedPreferences.edit().putString("user_address",response.getString("address")).apply()

                Toast.makeText(this@Registration,"Successfully Registered",Toast.LENGTH_LONG).show()

                val intent = Intent(this@Registration,Login::class.java)
                startActivity(intent)
                finish()

            }
            else{
                Toast.makeText(this@Registration,"Some unexpected error occurred",Toast.LENGTH_LONG).show()
            }
        },
        Response.ErrorListener {
            Toast.makeText(this@Registration,"Some error occurred",Toast.LENGTH_LONG).show()

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
