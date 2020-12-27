package com.kysportsblogs.android.di

import android.content.Context
import androidx.room.Room
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.network.KsrApi
import com.kysportsblogs.android.util.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Module()
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): KsbDatabase {
        return Room.databaseBuilder(context, KsbDatabase::class.java, "ksblogs.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideKsrApi(): KsrApi {
        return Retrofit.Builder()
            .baseUrl(KsrApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(KsrApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDispatchers() = AppDispatchers(
        io = Dispatchers.IO,
        main = Dispatchers.Main,
        default = Dispatchers.Default
    )
}