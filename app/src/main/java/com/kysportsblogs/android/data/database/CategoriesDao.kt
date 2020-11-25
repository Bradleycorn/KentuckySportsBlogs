package com.kysportsblogs.android.data.database

import androidx.room.Dao
import com.kysportsblogs.android.data.models.Category

@Dao
abstract class CategoriesDao: BaseDao<Category>() {
}