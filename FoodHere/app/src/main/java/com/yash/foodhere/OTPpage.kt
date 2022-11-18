package com.yash.foodhere

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.validation.Validations
import org.json.JSONObject
import java.util.HashMap

class OTPpage : AppCompatActivity() {
    var mobileNumber: String? = "123"
    lateinit var otp: EditText
    lateinit var pass: EditText
    lateinit var confirmPass: EditText
    lateinit var bttn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otppage)

        otp = findViewById(R.id.otp)
        pass = findViewById(R.id.pass)
        confirmPass = findViewById(R.id.confirmPass)
        bttn = findViewById(R.id.bttn)

        if(intent!=null){
            mobileNumber = intent.getStringExtra("Mobile")
        }

        bttn.setOnClickListener {
            if(Validations.otp(otp.text.toString())){
                otp.error = null
                if(Validations.Validatepassword(pass.text.toString())){
                    pass.error = null
                    if(Validations.Validatepassword(confirmPass.text.toString()) &&
                        confirmPass.text.toString() == pass.text.toString()){
                        confirmPass.error = null
                        OTPchange(mobileNumber.toString(),pass.text.toString(),otp.text.toString())
                    }
                    else{
                        confirmPass.error = "Invalid"
                    }
                }
                else{
                    pass.error = "Invalid"
                }
            }
            else{
                otp.error = "Invalid OTP"
            }
        }

    }

    private fun OTPchange(mobile: String, pass: String, OTP: String){
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
        val params = JSONObject()
        params.put("mobile_number",mobile)
        params.put("password",pass)
        params.put("otp",OTP)
        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST,url,params,Response.Listener {
            val data = it.getJSONObject("data")
            val success = data.getBoolean("success")
            try{
                if(success){
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Your password has been successfully changed")
                    dialog.setPositiveButton("OK"){_,_ ->
                        val intent = Intent(this@OTPpage,Login::class.java)
                        startActivity(intent)
                    }
                    ActivityCompat.finishAffinity(this)
                    dialog.create()
                    dialog.show()
                }
                else{
                    Toast.makeText(this@OTPpage,"Incorrect Data filled $it",Toast.LENGTH_LONG).show()

                }

            }
            catch (e: Exception){
                Toast.makeText(this@OTPpage,"Some error occurred",Toast.LENGTH_LONG).show()
            }

        },Response.ErrorListener {
            Toast.makeText(this@OTPpage,"Some error occurred",Toast.LENGTH_LONG).show()
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