package com.yash.foodhere

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {

    lateinit var name: TextView
    lateinit var phone: TextView
    lateinit var Email: TextView
    lateinit var delivery: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences =
            (activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        name = view.findViewById(R.id.name)
        phone = view.findViewById(R.id.phone)
        Email = view.findViewById(R.id.Email)
        delivery = view.findViewById(R.id.delivery)

        val username = sharedPreferences.getString("user_name",null)
        val email = sharedPreferences.getString("user_email",null)
        val mobile = sharedPreferences.getString("user_number",null)
        val Address = sharedPreferences.getString("user_address",null)


        name.text = username.toString()
        phone.text = mobile.toString()
        Email.text = email.toString()
        delivery.text = Address.toString()

        return view
    }

}