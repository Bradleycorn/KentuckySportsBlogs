package com.kysportsblogs.android.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.kysportsblogs.android.data.database.AppDatabase
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.network.KsrApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KsrRepository @Inject constructor(private val ksrApi: KsrApi, private val db: AppDatabase) {
    fun getBlogPosts(): Flow<List<BlogPost>> = db.postsDao().getBlogPostsFlow().distinctUntilChanged()

    fun getPost(id: Long): Flow<BlogPost> = db.postsDao().getPost(id).distinctUntilChanged()

    suspend fun fetchPosts(page: Int = 1) {
        val response = ksrApi.getPosts(page)
        val headers = response.headers()

        val totalPosts = headers["X-WP-Total"]
        val totalPages = headers["X-WP-TotalPages"]
        val wpPosts = response.body()

        Log.d("WORDPRESS", "Total Posts: $totalPosts")
        Log.d("WORDPRESS", "Total Pages: $totalPages")
        Log.d("WORDPRESS", "Posts: ${wpPosts?.size}")

        val postList = Post.fromWordpress(wpPosts)

        val posts = postList.map { it.post }
        val categories = postList.flatMap { it.categories }.distinct()
        val postCategories = postList.flatMap { it.postCategories }.distinct()

        db.withTransaction {
            db.postsDao().upsert(posts)
            db.categoriesDao().upsert(categories)
            db.postCategoriesDao().upsert(postCategories)
        }
    }
}