package com.yash.foodhere

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import javax.xml.validation.Validator

data class AllRestaurants(val id: String, val name: String, val rating: String, val cost_for_one: Int,
                          val image_url: String)

val Alldata = arrayListOf<AllRestaurants>()


class Home : Fragment() {

    lateinit var rv: RecyclerView
    lateinit var layout: RelativeLayout
    lateinit var bar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.rv , container, false)

        rv = view.findViewById(R.id.rv)
        layout = view.findViewById(R.id.layout)
        bar = view.findViewById(R.id.bar)

        var con = rv.context

        if (ConnectionManager().checkConnectivity(con)) {


            layout.visibility = View.VISIBLE

            val queue = Volley.newRequestQueue(con)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    try {
                        if (success) {
                            layout.visibility = View.GONE
                            Alldata.clear()
                            val resArray = data.getJSONArray("data")

                            for (i in 0 until resArray.length()) {
                                val jsonobjects = resArray.getJSONObject(i)
                                val objects = AllRestaurants(
                                    jsonobjects.getString("id"),
                                    jsonobjects.getString("name"),
                                    jsonobjects.getString("rating"),
                                    jsonobjects.getInt("cost_for_one"),
                                    jsonobjects.getString("image_url")

                                )
                                Alldata.add(objects)
                            }
                            val information = Alldata
                            rv.adapter = ListOfRestaurants(con, information)
                            rv.layoutManager = LinearLayoutManager(con)

                        } else {
                            Toast.makeText(
                                con,
                                "Error in fetching data $it",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }


                    } catch (e: JSONException) {
                            Toast.makeText(con, "Json exception", Toast.LENGTH_LONG)
                                .show()

                    }

                },
                Response.ErrorListener {

                    if (con != null) {
                        Toast.makeText(
                            con,
                            "Some error occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
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
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet not found")
            dialog.setPositiveButton("Open Setting") { text, Listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

}