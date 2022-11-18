package com.yash.foodhere.validation

import android.util.Patterns

object Validations {
    fun Validatephone(phone:String): Boolean{
        return phone.length == 10
    }

    fun ValidateEmail(email:String): Boolean{
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    fun Validatepassword(pass:String): Boolean{
        return (pass.length>4)
    }

    fun validateNameLength(name: String): Boolean {
        return name.length >= 3
    }

    fun ValidatematchPassword(pass: String, confirmPass: String): Boolean {
        return pass == confirmPass
    }

    fun ValidateAddress(Address: String): Boolean {
        return Address.isNotEmpty()
    }

    fun otp(Otp: String): Boolean{
        return Otp.length == 4
    }


}