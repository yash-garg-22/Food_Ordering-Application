package com.yash.foodhere

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class Favorite : Fragment() {

    lateinit var favRv: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.favouriterv, container, false)

        favRv = view.findViewById(R.id.favRv)





        val RestaurantList = retrive(activity as Context).execute().get()

        favRv.adapter = RestaurantData(activity as Context, RestaurantList)
        favRv.layoutManager = LinearLayoutManager(activity)



        return view
    }

    class retrive(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity>>(){
        val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"Restaurant-db").build()
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            return db.restaurantDao().getAllList()
        }

    }

}