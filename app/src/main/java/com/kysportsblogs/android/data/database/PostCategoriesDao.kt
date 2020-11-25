package com.kysportsblogs.android.data.database

import androidx.room.Dao
import com.kysportsblogs.android.data.models.PostCategories

@Dao
abstract class PostCategoriesDao: BaseDao<PostCategories>()