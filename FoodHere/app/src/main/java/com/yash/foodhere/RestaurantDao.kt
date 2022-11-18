package com.yash.foodhere

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun delete(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM Restaurants")
    fun getAllList(): List<RestaurantEntity>

    @Query("SELECT * FROM Restaurants WHERE restaurant_id=:id")
    fun getById(id: String): RestaurantEntity

}