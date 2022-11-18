package com.yash.foodhere.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Insert
    fun insert(orderEntity: OrderEntity)

    @Delete
    fun delete(orderEntity: OrderEntity)

    @Query("SELECT * FROM 'Order'")
    fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM 'Order' WHERE id = :id")
    fun getById(id: Int): OrderEntity

    @Query("DELETE FROM 'Order' WHERE id = :id")
    fun DeleteById(id:Int)
}