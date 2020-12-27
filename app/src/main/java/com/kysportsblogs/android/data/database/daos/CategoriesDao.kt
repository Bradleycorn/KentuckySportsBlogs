package com.kysportsblogs.android.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.kysportsblogs.android.data.models.Category

@Dao
abstract class CategoriesDao: BaseDao<Category>() {

    @Query("SELECT * FROM Categories ORDER BY name")
    abstract suspend fun getCategories(): List<Category>

    @Query("SELECT * FROM Categories WHERE name = :name")
    abstract suspend fun getCategory(name: String): Category?

}