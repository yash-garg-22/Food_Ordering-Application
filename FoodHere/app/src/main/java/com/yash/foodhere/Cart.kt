package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yash.foodhere.Database.OrderEntity
import com.yash.foodhere.Database.orderDatabase
import com.yash.foodhere.classes.CartDetails
import org.json.JSONObject

class Cart : AppCompatActivity() {

    lateinit var cartRv: RecyclerView
    lateinit var orderrv: Button
    lateinit var AllDatabase: List<OrderEntity>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cartrv)
        cartRv = findViewById(R.id.cartRv)
        orderrv = findViewById(R.id.orderrv)
        var total = "0"
        var sum = 0


         AllDatabase = RetereveData(this).execute().get()
        cartRv.adapter = CartDetails(this,AllDatabase)
        cartRv.layoutManager = LinearLayoutManager(this)


            for(i in 0 until AllDatabase.size){
                sum+=AllDatabase[i].cost_for_one
            }
            total = "Place Order(Total: Rs. $sum)"
            orderrv.text = total


        orderrv.setOnClickListener {
            val intent = Intent(this@Cart,OrderPlaced::class.java)
            intent.putExtra("Totalcost",total)
            startActivity(intent)
        }


    }
    class RetereveData(val context: Context): AsyncTask<Void, Void, List<OrderEntity>>()  {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderDao().getAll()
        }
    }

    inner class dbDelete(val context: Context, val orderEntity: OrderEntity): AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().delete(orderEntity)
            db.close()
            return true
        }
    }


}


