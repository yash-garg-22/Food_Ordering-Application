package com.yash.foodhere

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.yash.foodhere.Database.OrderEntity
import com.yash.foodhere.Database.orderDatabase
import android.content.Context as applicationContext

data class fake(val id: Int, val name: String, val costForOne:Int)

class MenuData(val menu: Menu, val allData: ArrayList<MenuList>, val listener: OnItemClickListener) : RecyclerView.Adapter<MenuData.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context ).inflate(R.layout.activity_menu,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allData = allData[position]
        holder.bind(allData)
    }

    override fun getItemCount(): Int {
       return allData.size
    }


    interface OnItemClickListener {
        fun onAddItemClick(foodItem: fake)
        fun onRemoveItemClick(foodItem: fake)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name = itemView.findViewById<TextView>(R.id.name)
        val price = itemView.findViewById<TextView>(R.id.price)
        val bttn = itemView.findViewById<Button>(R.id.bttn)
        val bttn2 = itemView.findViewById<Button>(R.id.bttn2)

        fun bind(allData: MenuList) {
            name.text = allData.name
            price.text = allData.cost_for_one.toString()

            bttn.visibility = View.VISIBLE
            bttn2.visibility = View.GONE

            bttn.setOnClickListener {

                val foodItem = fake(allData.id,allData.name,allData.cost_for_one)
                val orderEntity = OrderEntity(allData.id,allData.name,allData.cost_for_one)
                if(!Dbasyn(bttn2.context,orderEntity,1).execute().get()){
                    Dbasyn(bttn2.context,orderEntity,2).execute()
                }
                else{
                    Dbasyn(bttn2.context,orderEntity,3).execute()
                }
                bttn.visibility = View.GONE
                bttn2.visibility = View.VISIBLE
                listener.onAddItemClick(foodItem)
            }

            bttn2.setOnClickListener {
                val foodItem = fake(allData.id,allData.name,allData.cost_for_one)
                val orderEntity = OrderEntity(allData.id,allData.name,allData.cost_for_one)
                if(!Dbasyn(bttn2.context,orderEntity,1).execute().get()){
                    Dbasyn(bttn2.context,orderEntity,2).execute()
                }
                else{
                    Dbasyn(bttn2.context,orderEntity,3).execute()
                }
                bttn.visibility = View.VISIBLE
                bttn2.visibility = View.GONE
                listener.onRemoveItemClick(foodItem)
            }


        }

    }

}

class Dbasyn(val context: android.content.Context, val orderEntity: OrderEntity, val mode: Int): AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {
        val db = Room.databaseBuilder(context,orderDatabase::class.java,"Order-db").build()

        when(mode){
          1 -> {
              val order: OrderEntity? = db.orderDao().getById(orderEntity.id)
              db.close()
              return order!=null
          }
            2 -> {
                db.orderDao().insert(orderEntity)
                db.close()
                return true
            }

            3 -> {
                db.orderDao().delete(orderEntity)
                db.close()
                return true
            }
        }
        return false

    }


}
