package com.kysportsblogs.android.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostsDao: BaseDao<Post>() {

    @Query("SELECT * FROM Posts ORDER BY datePosted DESC")
    abstract suspend fun getPosts(): List<Post>

    @Transaction
    @Query("SELECT * FROM Posts ORDER by datePosted DESC")
    abstract fun getBlogPostsFlow(): Flow<List<BlogPost>>

    @Transaction
    @Query("SELECT * FROM Posts p LEFT OUTER JOIN postcategories pc ON p.postId = pc.postId LEFT OUTER JOIN categories c ON pc.categoryId = c.categoryId WHERE c.name = :category ORDER by datePosted DESC")
    abstract fun getBlogPostsInCategory(category: String): Flow<List<BlogPost>>

    @Transaction
    @Query("SELECT * FROM Posts WHERE postId = :id")
    abstract fun getPost(id: Long): Flow<BlogPost>

}