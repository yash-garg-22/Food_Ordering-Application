package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yash.foodhere.Database.OrderEntity
import com.yash.foodhere.Database.orderDatabase
import org.json.JSONArray
import org.json.JSONObject

class OrderPlaced : AppCompatActivity() {

    lateinit var bttn: Button
    lateinit var sharedPreferences: SharedPreferences
    var TAG = "done"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        bttn = findViewById(R.id.bttn)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)



        var totalcost: String? = "12"

        val userId = sharedPreferences.getString("user_id",null)
        val restId = sharedPreferences.getString("user_rest_id",null)

        if(intent!=null){
            totalcost = intent.getStringExtra("Totalcost")
        }
        val params = JSONObject()
        Log.i(TAG,"user_id = $userId")
        params.put("user_id",userId.toString())
        Log.i(TAG,"user_id = $restId")
        params.put("restaurant_id",restId.toString())
        Log.i(TAG,"user_id = $totalcost")
        params.put("total_cost",totalcost.toString())
        val ALLData = RetereveData(this).execute().get()


        val FoodArray = JSONArray()
            for (i in 0 until ALLData.size) {
                val FoodId = JSONObject()
                FoodId.put("food_item_id",ALLData[i].id)
                Log.i(TAG,"user_id = ${Alldata[i]}")
                FoodArray.put(i,FoodId)
            }
        Log.i(TAG,"user_id = $FoodArray")
        params.put("food",FoodArray)

        fun placingorder(){
            val queue = Volley.newRequestQueue(this)
            val url ="http://13.235.250.119/v2/place_order/fetch_result/"

            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST,url,params,Response.Listener {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                try{
                    if(success){
                        val intent = Intent(this@OrderPlaced,DrawerHeader::class.java)

                        val AllData = RetereveData(this).execute().get()


                        for(i in 0 until AllData.size){
                            val orderEntity = OrderEntity(
                                AllData[i].id,
                                AllData[i].name,
                                AllData[i].cost_for_one
                            )

                            val clearCart = dbDelete(this, orderEntity).execute().get()
                        }
                        startActivity(intent)
                        ActivityCompat.finishAffinity(this@OrderPlaced)

                        Toast.makeText(this,"Order Places successfully",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@OrderPlaced,"$it",Toast.LENGTH_LONG).show()
                    }
                }
                catch (e:Exception){
                    Toast.makeText(this@OrderPlaced,"Some error occurred $it",Toast.LENGTH_LONG).show()
                }


            },
                Response.ErrorListener {
                    Toast.makeText(this@OrderPlaced,"Some error occurred $it",Toast.LENGTH_LONG).show()
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


        bttn.setOnClickListener {
            placingorder()
        }
    }


    override fun onBackPressed() {
    }

    inner class dbDelete(val context: Context, val orderEntity: OrderEntity): AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().delete(orderEntity)
            db.close()
            return true
        }
    }

    inner class RetereveData(val context: Context): AsyncTask<Void, Void, List<OrderEntity>>()  {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            var I = db.orderDao().getAll()
            Log.i(TAG,"$I")
            return I
        }
    }

}