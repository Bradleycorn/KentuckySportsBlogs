package com.kysportsblogs.android.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.models.RequestLogEntry

@Dao
abstract class RequestLogDao: BaseDao<RequestLogEntry>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(obj: RequestLogEntry): Long

    @Query("SELECT * FROM RequestLog WHERE postType = :postType ORDER BY id DESC LIMIT 1")
    abstract suspend fun getRequest(postType: PostType): RequestLogEntry?
}