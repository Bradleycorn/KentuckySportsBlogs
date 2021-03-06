package com.kysportsblogs.android.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.extensions.addDays
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
abstract class PostsDao: BaseDao<Post>() {

    @Transaction
    @Query("SELECT * FROM Posts WHERE type = :postType ORDER by datePosted DESC")
    abstract fun postsByTypeObservable(postType: PostType): Flow<List<BlogPost>>

    @Query("DELETE FROM Posts WHERE type = :postType")
    abstract suspend fun deletePostsByType(postType: PostType)

    @Transaction
    @Query("SELECT * FROM Posts WHERE postId = :id")
    abstract fun postObservable(id: Long): Flow<BlogPost>

    @Transaction
    @Query("DELETE FROM Posts WHERE postId = :id")
    abstract suspend fun delete(id: Long)


    @Query("SELECT * FROM Posts ORDER BY datePosted DESC LIMIT 1")
    abstract suspend fun getNewestPost(): Post?

    @Transaction
    @Query("SELECT * FROM Posts WHERE datePosted > :after ORDER by datePosted DESC")
    abstract fun getPosts(after: Date = Date(0)): Flow<List<BlogPost>>

    @Transaction
    @Query("SELECT * FROM Posts WHERE datePosted > :after ORDER by datePosted DESC")
    abstract suspend fun getRecentPosts(after: Date = Date(0)): List<BlogPost>

    @Transaction
    @Query("SELECT p.* FROM Posts p LEFT OUTER JOIN postcategories pc ON p.postId = pc.postId and categoryId = :categoryId WHERE p.datePosted > :after ORDER by datePosted DESC")
    abstract suspend fun getPostsInCategory(categoryId: Int, after: Date = Date().addDays(-1)): List<BlogPost>

    @Transaction
    @Query("DELETE FROM Posts WHERE postId NOT in (:posts)")
    abstract suspend fun deleteOthers(posts: List<Long>)

    @Query("DELETE FROM POSTS")
    abstract suspend fun deleteAllPosts()

    @Query("DELETE FROM Posts WHERE datePosted < :date")
    abstract suspend fun deletePostsOlderThan(date: Date)
}