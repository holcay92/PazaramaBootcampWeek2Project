package com.example.week2homework.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Update
    suspend fun updateProduct(product: Product): Int

    @Query("SELECT * FROM product_table ORDER BY id ASC")
    fun getAllItems(): LiveData<List<Product>>

    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getProduct(id: Int): LiveData<Product>
}
