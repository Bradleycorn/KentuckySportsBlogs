package com.kysportsblogs.android.data.stores

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.models.*
import com.kysportsblogs.android.data.network.KsrApi
import com.kysportsblogs.android.data.network.getPostsByType
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import com.kysportsblogs.android.extensions.toInt
import com.kysportsblogs.android.extensions.toString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.time.Instant
import java.util.*
import javax.inject.Singleton

typealias PostsStore = Store<PostType, List<BlogPost>>


@InstallIn(ApplicationComponent::class)
@Module
object PostsStoreModule {
    @Provides
    @Singleton
    fun postListStore(
        db: KsbDatabase,
        ksrApi: KsrApi,
        requestLog: RequestLog
    ): PostsStore = StoreBuilder.from(
        fetcher = Fetcher.of { postType: PostType ->
            val after = db.postsDao().getLatestPostDate(postType)
            val response = ksrApi.getPostsByType(postType, after)

            when {
                response.isSuccessful -> {
                    db.requestLogDao().insert(RequestLogEntry(postType = postType, timestamp = Instant.now()))

                    if (after!= null && response.headers()["X-WP-TotalPages"].toInt() > 1) {
                        db.postsDao().deletePostsByType(postType)
                    }

                    Post.fromWordpress(response.body())
                }
                else -> listOf()
            }
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { postType ->
                db.postsDao().postTypeObservable(postType).map { entries ->
                    when {
                        entries.isEmpty() -> null
                        requestLog.isRequestExpired(postType) -> null
                        else -> entries
                    }
                }
            },
            writer = { postType, postList ->
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
    ).build()
}