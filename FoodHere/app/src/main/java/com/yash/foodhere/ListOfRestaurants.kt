package com.yash.foodhere

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.squareup.picasso.Picasso


class ListOfRestaurants(val context: Context,val information: List<AllRestaurants>) : RecyclerView.Adapter<ListOfRestaurants.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context ).inflate(R.layout.fragment_home,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val information = information[position]
        holder.bind(information)
        holder.all.setOnClickListener {
            val intent = Intent(context,Menu::class.java)
            intent.putExtra("ResId",information.id)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return information.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val image = itemView.findViewById<ImageView>(R.id.image)
        val RestaurantName = itemView.findViewById<TextView>(R.id.RestaurantName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val rating = itemView.findViewById<TextView>(R.id.rating)
        val favourate = itemView.findViewById<ImageView>(R.id.favourate)
        val all = itemView.findViewById<CardView>(R.id.all)

        fun bind(information: AllRestaurants) {
            val Image = information.image_url.toString()
            RestaurantName.text = information.name
            price.text = information.cost_for_one.toString()
            rating.text = information.rating
            Picasso.get().load(information.image_url).into(image)

            val restaurantEntity = RestaurantEntity(
                information.id?.toInt() as Int,
                RestaurantName.text.toString(),
                rating.text.toString(),
                price.text.toString(),
                Image
            )
            val checkFav = dbasyn(context,restaurantEntity,1).execute()
            val isFav = checkFav.get()

            if(isFav){
                favourate.setImageResource(R.drawable.ic_baseline_favorite_21)
            }
            else{
                favourate.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            favourate.setOnClickListener {

                if(!dbasyn(context,restaurantEntity,1).execute().get()){
                    val async = dbasyn(context,restaurantEntity,2).execute()
                    val result = async.get()
                    if(result){
                        Toast.makeText(context,"Added to Favourites",Toast.LENGTH_LONG).show()
                        favourate.setImageResource(R.drawable.ic_baseline_favorite_21)
                    }

                    else{
                        Toast.makeText(context,"Error occurred",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    val async = dbasyn(context,restaurantEntity,3).execute()
                    val result = async.get()

                    if(result){
                        Toast.makeText(context,"Remove to Favourites",Toast.LENGTH_LONG).show()
                        favourate.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                    else{
                        Toast.makeText(context,"Error occurred",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }


    }

    class dbasyn(val context: Context,val restaurantEntity: RestaurantEntity,val mode: Int): AsyncTask<Void, Void, Boolean>(){

        val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"Restaurant-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1 ->
                {
                    val restaurant: RestaurantEntity? = db.restaurantDao().getById(restaurantEntity.restaurant_id.toString())
                    db.close()
                    return restaurant!=null
                }

                2 -> {
                    db.restaurantDao().insert(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().delete(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }


}


