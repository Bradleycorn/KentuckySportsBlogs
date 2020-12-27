package com.kysportsblogs.android.data.repository

import androidx.room.withTransaction
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.network.KsrApi
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import com.kysportsblogs.android.data.stores.PostsStore
import com.kysportsblogs.android.extensions.add
import com.kysportsblogs.android.extensions.addDays
import com.kysportsblogs.android.extensions.toString
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KsrRepository @Inject constructor(private val ksrApi: KsrApi, private val db: KsbDatabase, private val postsStore: PostsStore) {
    suspend fun refreshAllPostTypes(force: Boolean = false) {
        PostType.values().forEach { postType ->
            coroutineScope {
                if (force) {
                    postsStore.fresh(postType)
                } else {
                    postsStore.get(postType)
                }
            }
        }
    }

    fun getPost(id: Long): Flow<BlogPost> = db.postsDao().getPost(id).distinctUntilChanged()





//    suspend fun getRecentPosts(): List<BlogPost> = db.postsDao().getRecentPosts(after = Date().addDays(-1))
//
//    suspend fun fetchCategoryPosts(categoryId: Int, after: Date = Date(0), page: Int = 1): List<BlogPost> {
//        val queryDate = after.toString(KsrApi.QUERY_DATE_FORMAT)
//        val response = ksrApi.getPostsInCategories(categoryId, queryDate, page)
//        return response.body()?.map { Post.fromWordpress(it) } ?: listOf()
//    }
//
//    suspend fun updateCategory(categoryId: Int) {
//        val newestPost = db.postsDao().getNewestPostInCategory(categoryId)
//        val minimumDate = Date().add(-1, Calendar.WEEK_OF_YEAR)
//
//        val afterDate = newestPost?.wordpressDate ?: minimumDate.toString(WordpressPost.DATE_FORMAT)
//
//        val response = ksrApi.getPostsInCategories(categoryId, afterDate)
//
//        val headers = response.headers()
//
//        val totalPosts = headers["X-WP-Total"]
//        val totalPages = headers["X-WP-TotalPages"]
//        val wpPosts = response.body()
//
//        val postList = Post.fromWordpress(wpPosts)
//
//        val posts = postList.map { it.post }
//        val categories = postList.flatMap { it.categories }.distinct()
//        val postCategories = postList.flatMap { it.postCategories }.distinct()
//
//        db.withTransaction {
//            db.postsDao().upsert(posts)
//            db.categoriesDao().upsert(categories)
//            db.postCategoriesDao().upsert(postCategories)
//        }
//    }
//
//
//    suspend fun updatePosts() {
//        val newestPost = db.postsDao().getNewestPost()
//        val minimumDate = Date().add(-1, Calendar.WEEK_OF_YEAR)
//
//        val response = newestPost?.let {
//            db.postsDao().deletePostsOlderThan(minimumDate)
//            ksrApi.getRecentPosts(newestPost.wordpressDate)
//        } ?: ksrApi.getPosts()
//
//        val headers = response.headers()
//
//        val totalPosts = headers["X-WP-Total"]
//        val totalPages = headers["X-WP-TotalPages"]
//        val wpPosts = response.body()
//
//        val postList = Post.fromWordpress(wpPosts)
//
//        val posts = postList.map { it.post }
//        val categories = postList.flatMap { it.categories }.distinct()
//        val postCategories = postList.flatMap { it.postCategories }.distinct()
//
//        db.withTransaction {
//            db.postsDao().upsert(posts)
//            db.categoriesDao().upsert(categories)
//            db.postCategoriesDao().upsert(postCategories)
//        }
//    }
}