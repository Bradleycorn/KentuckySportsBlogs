package com.kysportsblogs.android.data.network

import com.kysportsblogs.android.data.network.responseModels.RSSFeed
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KsrApi {
    companion object {
        const val BASE_URL = "https://kentuckysportsradio.com/"
        const val US_DATE_CHECKED_FORMAT = "yyyy-MM-dd" //""2020-06-27T00:00:00Z"
        const val STATE_DATE_CHECKED_FORMAT = "M/d/yyyy" // 6/9/2020 00:00
        const val DATE_INT_FORMAT = "yyyyMMdd"
        private const val POSTS_PER_PAGE = 25
    }

    @GET("feed/")
    suspend fun getKsrFeed(): RSSFeed


    @GET("wp-json/wp/v2/posts?per_page=$POSTS_PER_PAGE&_embed=author,wp:term,wp:featuredmedia")
    suspend fun getPosts(@Query("page") page: Int = 1): Response<List<WordpressPost>>

}