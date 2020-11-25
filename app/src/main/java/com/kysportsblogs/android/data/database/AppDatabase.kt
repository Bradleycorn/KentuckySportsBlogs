package com.kysportsblogs.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kysportsblogs.android.data.database.converters.DateConverter
import com.kysportsblogs.android.data.models.Category
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.PostCategories

@Database(version = 1, entities = [ Post::class, Category::class, PostCategories::class ])
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun postCategoriesDao(): PostCategoriesDao
}