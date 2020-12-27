package com.kysportsblogs.android.data.network

import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import com.kysportsblogs.android.extensions.toString
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface KsrApi {
    companion object {
        const val BASE_URL = "https://kentuckysportsradio.com/"
        private const val POSTS_PER_PAGE = 25

        const val QUERY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

        const val CATEGORY_FOOTBALL = 618L
        const val CATEGORY_BASKETBALL = 615L
        const val CATEGORY_TOP_STORIES = 624L

        fun getCategory(postType: PostType): Long? = when (postType) {
            PostType.FOOTBALL -> CATEGORY_FOOTBALL
            PostType.BASKETBALL -> CATEGORY_BASKETBALL
            PostType.TOP_STORIES -> CATEGORY_TOP_STORIES
            PostType.OTHER -> null
        }
    }

    @GET("wp-json/wp/v2/posts?per_page=$POSTS_PER_PAGE&_embed=author,wp:term,wp:featuredmedia")
    suspend fun getPosts(@Query("after") after: String? = null, @Query("page") page: Int = 1): Response<List<WordpressPost>>

    @GET("wp-json/wp/v2/posts?per_page=$POSTS_PER_PAGE&_embed=author,wp:term,wp:featuredmedia")
    suspend fun getPostsInCategory(@Query("categories") category: Long, @Query("after") after: String? = null, @Query("page") page: Int = 1): Response<List<WordpressPost>>

    @GET("wp-json/wp/v2/posts?per_page=$POSTS_PER_PAGE&_embed=author,wp:term,wp:featuredmedia")
    suspend fun getPostsInCategories(@Query("categories") category: List<Long>, @Query("after") after: String? = null, @Query("page") page: Int = 1): Response<List<WordpressPost>>

    @GET("wp-json/wp/v2/posts?per_page=$POSTS_PER_PAGE&_embed=author,wp:term,wp:featuredmedia")
    suspend fun getPostsNotInCategories(@Query("categories") category: List<Long>, @Query("after") after: String? = null, @Query("page") page: Int = 1): Response<List<WordpressPost>>

}


suspend fun KsrApi.getPostsByType(postType: PostType, postedAfter: Date? = null): Response<List<WordpressPost>> {
    val after = postedAfter?.toString(KsrApi.QUERY_DATE_FORMAT)

    return when (postType) {
        PostType.FOOTBALL -> getPostsInCategory(KsrApi.CATEGORY_FOOTBALL, after)
        PostType.BASKETBALL -> getPostsInCategory(KsrApi.CATEGORY_BASKETBALL, after)
        PostType.TOP_STORIES -> getPostsInCategory(KsrApi.CATEGORY_TOP_STORIES, after)
        PostType.OTHER -> getPostsNotInCategories(listOf(KsrApi.CATEGORY_FOOTBALL, KsrApi.CATEGORY_BASKETBALL, KsrApi.CATEGORY_TOP_STORIES), after)
    }
}