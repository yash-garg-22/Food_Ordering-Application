package com.yash.foodhere.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
class OrderEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val cost_for_one: Int
)


