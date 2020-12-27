package com.kysportsblogs.android.data.database

import androidx.room.TypeConverter
import com.kysportsblogs.android.data.models.PostType
import java.time.Instant
import java.util.*

object KsbTypeConverters {

    private val postTypeValues by lazy(LazyThreadSafetyMode.NONE) { PostType.values() }

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?) = date?.time

    @TypeConverter
    @JvmStatic
    fun fromPostType(postType: PostType) = postType.name

    @TypeConverter
    @JvmStatic
    fun toPostType(value: String) = postTypeValues.firstOrNull { it.name == value }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

}

