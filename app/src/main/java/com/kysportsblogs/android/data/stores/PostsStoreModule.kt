package com.kysportsblogs.android.data.stores

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dropbox.android.external.store4.*
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.models.*
import com.kysportsblogs.android.data.network.KsrApi
import com.kysportsblogs.android.data.network.NetworkException
import com.kysportsblogs.android.data.network.NoDataException
import com.kysportsblogs.android.data.network.getPostsByType
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import com.kysportsblogs.android.extensions.toInt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

private const val TAG = "PostsStore"

typealias PostListStore = Store<PostType, List<BlogPost>>
typealias PostStore = Store<Long, BlogPost>

@InstallIn(ActivityComponent::class)
@Module
object PostsStoreModule {

    @OptIn(ExperimentalTime::class)
    @Provides
    @ActivityScoped
    fun postListStore(
        db: KsbDatabase,
        ksrApi: KsrApi,
        requestLog: RequestLog,
        @ActivityContext context: Context
    ): PostListStore = StoreBuilder.from(
        fetcher = Fetcher.of { postType: PostType ->
            Log.d(TAG, "Fetching posts for ${postType.displayName}")
            val lastRequest = requestLog.getRequestTimestamp(postType)
            val response = ksrApi.getPostsByType(postType, lastRequest)

            if (response.isSuccessful) {
                db.requestLogDao().insert(RequestLogEntry(postType = postType, timestamp = Instant.now()))

                if (lastRequest != null && response.headers()["X-WP-TotalPages"].toInt() > 1) {
                    db.postsDao().deletePostsByType(postType)
                    Log.d(TAG, "Deleted existing posts for ${postType.displayName}.")
                }

               Post.fromWordpress(response.body())
            } else throw NetworkException(response, response.message())
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { postType ->
                db.postsDao().postsByTypeObservable(postType).distinctUntilChanged().map { entries ->
                    val dataExpired = requestLog.isRequestExpired(postType)

                    if (dataExpired) {
                        Log.d(TAG, "Reader found ${entries.size} expired ${postType.displayName} entries.")
                    }

                    val result = when {
                        dataExpired -> null
//                        entries.isEmpty() -> null
                        else -> entries
                    }

                    result
                }
            },
            writer = { postType, postList ->
                Log.d(TAG, "Writing ${postList.size} ${postType.displayName} entries")
                db.postsDao().withTransaction {
                    val posts = postList.map { it.post }
                    val categories = postList.flatMap { it.categories }.distinct()
                    val postCategories = postList.flatMap { it.postCategories }.distinct()

                    db.postsDao().upsert(posts)
                    db.categoriesDao().upsert(categories)
                    db.postCategoriesDao().upsert(postCategories)
                }
            },
            delete = db.postsDao()::deletePostsByType,
            deleteAll = db.postsDao()::deleteAllPosts
        )
    )
    .scope((context as AppCompatActivity).lifecycleScope)
    .cachePolicy(MemoryPolicy.builder<PostType, List<BlogPost>>()
        .setMaxSize(PostType.values().size * 2L)
        .setExpireAfterWrite(5.minutes)
        .build())
    .build()

    @OptIn(ExperimentalTime::class)
    @Provides
    @ActivityScoped
    fun postStore(
        db: KsbDatabase,
        ksrApi: KsrApi
    ): PostStore = StoreBuilder.from(
        fetcher = Fetcher.of { id: Long ->
            val response = ksrApi.getPost(id)

            if (response.isSuccessful && response.body() != null) {
                Post.fromWordpress(response.body()!!)
            } else throw NetworkException(response, response.message())
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = db.postsDao()::postObservable,
            writer = { id, blogPost ->
                db.postsDao().withTransaction {
                    db.postsDao().upsert(blogPost.post)
                    db.categoriesDao().upsert(blogPost.categories)
                    db.postCategoriesDao().upsert(blogPost.postCategories)
                }
            },
            delete = db.postsDao()::delete,
            deleteAll = db.postsDao()::deleteAllPosts
        )
    )
    .cachePolicy(MemoryPolicy.builder<Long, BlogPost>()
        .setMaxSize(10L)
        .setExpireAfterWrite(5.minutes)
        .build())
    .build()

}