package com.kysportsblogs.android.di

import android.content.Context
import com.kysportsblogs.android.BuildConfig
import com.kysportsblogs.android.data.network.KsrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        if (!BuildConfig.DEBUG) {
            return null
        }

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Singleton
    @Provides
    fun provideHttpEventListener(): LoggingEventListener.Factory? {
        val enabled = false

        if (!BuildConfig.DEBUG || !enabled) {
            return null
        }

        return LoggingEventListener.Factory()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor?,
            loggingEventListener: LoggingEventListener.Factory?,
            @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .apply {
                    if (httpLoggingInterceptor != null) {
                        addInterceptor(httpLoggingInterceptor)
                    }
                    if (loggingEventListener != null) {
                        eventListenerFactory(loggingEventListener)
                    }
                }
                // Around 4¢ worth of storage in 2020
                .cache(Cache(File(context.cacheDir, "api_cache"), 50 * 1024 * 1024))
                // Adjust the Connection pool to account for historical use of 3 separate clients
                // but reduce the keepAlive to 2 minutes to avoid keeping radio open.
                .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
                .dispatcher(
                        Dispatcher().apply {
                            // Allow for high number of concurrent image fetches on same host.
                            maxRequestsPerHost = 15
                        }
                )
                .build()
    }

    @Provides
    @Singleton
    fun provideKsrApi(okHttpClient: OkHttpClient): KsrApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(KsrApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(KsrApi::class.java)
    }
}