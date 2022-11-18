package com.yash.foodhere

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestaurantEntity(
    @PrimaryKey val restaurant_id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val rating: String,
    @ColumnInfo val cost_for_one: String,
    @ColumnInfo val image_url: String

)
