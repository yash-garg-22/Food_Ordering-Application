package com.yash.foodhere.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yash.foodhere.Cart
import com.yash.foodhere.Database.OrderEntity
import com.yash.foodhere.R


class CartDetails(val cart: Cart,val allData: List<OrderEntity>) : RecyclerView.Adapter<CartDetails.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context ).inflate(R.layout.activity_cart,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val allData = allData[position]
        holder.bind(allData)
    }

    override fun getItemCount(): Int {
        return allData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        val price = itemView.findViewById<TextView>(R.id.price)

        fun bind(allData: OrderEntity) {
            name.text = allData.name
            price.text = allData.cost_for_one.toString()
        }

    }


}
