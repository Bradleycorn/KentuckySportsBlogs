package com.kysportsblogs.android.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.extensions.Ago
import com.kysportsblogs.android.extensions.Minutes
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Entity(
    tableName = "RequestLog",
    indices = [Index("postType")]
)
data class RequestLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postType: PostType,
    val timestamp: Instant,
)

class RequestLog @Inject constructor(private val db: KsbDatabase)  {

    suspend fun getRequestTimestamp(postType: PostType): Instant? {


        return db.requestLogDao().getRequest(postType)?.timestamp
    }

    suspend fun isLastRequestBefore(postType: PostType, timestamp: Instant): Boolean {
        return getRequestTimestamp(postType)?.isBefore(timestamp) ?: true
    }

    suspend fun isRequestExpired(postType: PostType): Boolean {
        val expiration = 15.Minutes.Ago
        return isLastRequestBefore(postType, expiration)
    }
}
