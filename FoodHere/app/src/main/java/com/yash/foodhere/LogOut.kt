package com.yash.foodhere

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogOut.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogOut : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_log_out, container, false)

        sharedPreferences =
            (activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val builder = AlertDialog.Builder(activity as Context)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure you want to Log out")
        builder.setPositiveButton("Yes"){_,_ ->
            sharedPreferences.edit().clear().apply()
            val intent = Intent(activity as Context,Login::class.java)
            startActivity(intent)
            ActivityCompat.finishAffinity(activity as Activity)
        }
        builder.setNegativeButton("No"){_,_->
            val intent = Intent(activity as Context,DrawerHeader::class.java)
            startActivity(intent)
            ActivityCompat.finishAffinity(activity as Activity)
        }
        builder.create()
        builder.show()
        return view
    }

}