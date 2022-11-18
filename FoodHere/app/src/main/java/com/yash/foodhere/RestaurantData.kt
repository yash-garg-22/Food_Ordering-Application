package com.yash.foodhere

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.zip.Inflater

class RestaurantData(val context: Context, val restaurantList: List<RestaurantEntity>) : RecyclerView.Adapter<RestaurantData.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context ).inflate(R.layout.fragment_favorite,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantList = restaurantList[position]
        holder.bind(restaurantList)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val image = itemView.findViewById<ImageView>(R.id.image)
        val RestaurantName = itemView.findViewById<TextView>(R.id.RestaurantName)
        val price = itemView.findViewById<TextView>(R.id.price)
        val favourate = itemView.findViewById<ImageView>(R.id.favourate)
        val rating = itemView.findViewById<TextView>(R.id.rating)

        fun bind(restaurantList: RestaurantEntity) {

            val Image = restaurantList.image_url.toString()
            RestaurantName.text = restaurantList.name
            price.text = restaurantList.cost_for_one
            rating.text = restaurantList.rating
            Picasso.get().load(restaurantList.image_url).into(image)

            val restaurantEntity = RestaurantEntity(
                restaurantList.restaurant_id?.toInt() as Int,
                RestaurantName.text.toString(),
                rating.text.toString(),
                price.text.toString(),
                Image
            )
            val checkFav = ListOfRestaurants.dbasyn(context, restaurantEntity, 1).execute()
            val isFav = checkFav.get()

            if(isFav){
                favourate.setImageResource(R.drawable.ic_baseline_favorite_21)
            }
            else{
                favourate.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            favourate.setOnClickListener {

                if(!ListOfRestaurants.dbasyn(context, restaurantEntity, 1).execute().get()){
                    val async = ListOfRestaurants.dbasyn(context, restaurantEntity, 2).execute()
                    val result = async.get()
                    if(result){
                        Toast.makeText(context,"Added to Favourites", Toast.LENGTH_LONG).show()
                        favourate.setImageResource(R.drawable.ic_baseline_favorite_21)
                    }

                    else{
                        Toast.makeText(context,"Error occurred", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    val async = ListOfRestaurants.dbasyn(context, restaurantEntity, 3).execute()
                    val result = async.get()

                    if(result){
                        Toast.makeText(context,"Remove to Favourites", Toast.LENGTH_LONG).show()
                        favourate.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                    else{
                        Toast.makeText(context,"Error occurred", Toast.LENGTH_LONG).show()
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
