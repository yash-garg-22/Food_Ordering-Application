package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.Database.OrderEntity
import com.yash.foodhere.Database.orderDatabase


data class MenuList(val id: Int,val name: String,val cost_for_one: Int,val restaurant_id: Int)
val information = arrayListOf<MenuList>()
var orderList = arrayListOf<fake>()
var AllDeleteData = arrayListOf<OrderEntity>()

class Menu : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    var MenuId: String? = "100"

    lateinit var Menurv: RecyclerView
    lateinit var bttnrv: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menurv)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        Menurv = findViewById(R.id.Menurv)
        bttnrv = findViewById(R.id.bttnrv)


        if(intent!=null){
            MenuId = intent.getStringExtra("ResId")
            sharedPreferences.edit().putString("user_rest_id",MenuId).apply()
        }
        else{
            Toast.makeText(this@Menu,"Some unexpected error occurred",Toast.LENGTH_LONG).show()
        }
        if(MenuId=="100"){
            Toast.makeText(this@Menu,"Some unexpected error occurred",Toast.LENGTH_LONG).show()
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        val jsonObjectRequest = object: JsonObjectRequest(com.android.volley.Request.Method.GET,url+MenuId,null,Response.Listener {
            val data = it.getJSONObject("data")
            val success = data.getBoolean("success")

            try{
            if(success){
                val array = data.getJSONArray("data")
                information.clear()
                for(i in 0 until array.length()){
                    val response = array.getJSONObject(i)
                    val objects = MenuList(
                        response.getInt("id"),
                        response.getString("name"),
                        response.getInt("cost_for_one"),
                        response.getInt("restaurant_id")
                    )
                    information.add(objects)
                }

                val allData = information

                bttnrv.visibility = View.GONE

                Menurv.adapter = MenuData(this,allData,object:

                MenuData.OnItemClickListener{
                    override fun onAddItemClick(foodItem: fake) {

                        orderList.add(foodItem)
                        if(orderList.size>0){
                            bttnrv.visibility = View.VISIBLE

                            Toast.makeText(this@Menu,"${orderList.size}",Toast.LENGTH_LONG).show()
                        }
                        else{
                            bttnrv.visibility = View.GONE
                        }
                    }

                    override fun onRemoveItemClick(foodItem: fake) {
                        orderList.remove(foodItem)
                        if(orderList.size > 0){
                            bttnrv.visibility = View.VISIBLE
                        }
                        else{
                            bttnrv.visibility = View.GONE
                        }

                    }

                })
                Menurv.layoutManager = LinearLayoutManager(this)

            }
                else{
                Toast.makeText(this@Menu,"Some unexpected error occurred",Toast.LENGTH_LONG).show()

            }
                }
            catch (e: Exception){
                Toast.makeText(this@Menu,"Some unexpected error occurred",Toast.LENGTH_LONG).show()
            }

        },Response.ErrorListener {
            Toast.makeText(this@Menu,"Some unexpected error occurred",Toast.LENGTH_LONG).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "594fce9074c737"
                return headers
            }

        }
        queue.add(jsonObjectRequest)

        bttnrv.setOnClickListener {
            val intent = Intent(this@Menu,Cart::class.java)
            startActivity(intent)
        }


    }

    override fun onBackPressed() {
        orderList.clear()
        AllDeleteData = RetereveData(this).execute().get() as ArrayList<OrderEntity>
        for(i in 0 until AllDeleteData.size){
            val orderEntity = OrderEntity(
                AllDeleteData[i].id,
                AllDeleteData[i].name,
                AllDeleteData[i].cost_for_one
            )
            val deleted = dbDelete(this,orderEntity).execute().get()
        }
        super.onBackPressed()
    }




    inner class dbDelete(val context: android.content.Context, val orderEntity: OrderEntity): AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, orderDatabase::class.java, "Order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().delete(orderEntity)
            db.close()
            return true
        }


    }
    class RetereveData(val context: Context): AsyncTask<Void, Void, List<OrderEntity>>()  {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderDao().getAll()
        }
    }
}
