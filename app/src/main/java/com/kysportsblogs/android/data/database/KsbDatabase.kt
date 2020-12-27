package com.kysportsblogs.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kysportsblogs.android.data.database.daos.CategoriesDao
import com.kysportsblogs.android.data.database.daos.PostCategoriesDao
import com.kysportsblogs.android.data.database.daos.PostsDao
import com.kysportsblogs.android.data.database.daos.RequestLogDao
import com.kysportsblogs.android.data.models.Category
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.PostCategories
import com.kysportsblogs.android.data.models.RequestLogEntry

@Database(
    version = 1,
    entities = [
        Post::class,
        Category::class,
        PostCategories::class,
        RequestLogEntry::class,
    ]
)
@TypeConverters(KsbTypeConverters::class)
abstract class KsbDatabase: RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun postCategoriesDao(): PostCategoriesDao
    abstract fun requestLogDao(): RequestLogDao
}