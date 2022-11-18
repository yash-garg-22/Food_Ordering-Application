package com.yash.foodhere.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OrderEntity::class], version = 1)
abstract class orderDatabase: RoomDatabase() {

    abstract fun orderDao(): OrderDao
}