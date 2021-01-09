package com.kysportsblogs.android.data.repository

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dropbox.android.external.store4.*
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.models.RequestLog
import com.kysportsblogs.android.data.network.NetworkException
import com.kysportsblogs.android.data.network.NoDataException
import com.kysportsblogs.android.data.stores.PostListStore
import com.kysportsblogs.android.extensions.exception
import com.kysportsblogs.android.ui.UiState
import com.kysportsblogs.android.util.AppDispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostListUi @Inject constructor(private val postListStore: PostListStore, private val requestLog: RequestLog, private val db: KsbDatabase, private val dispatchers: AppDispatchers) {
    private var postType: PostType? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    private var postsLoading by mutableStateOf(false)
    private var postList: List<BlogPost>? by mutableStateOf(null)
    private var error: Throwable? by mutableStateOf(null)

    val state by derivedStateOf {
        UiState(loading = postsLoading, data = postList, error = error)
    }

    suspend fun loadPosts(type: PostType) {
        postType = type

        val dataExpired = requestLog.isRequestExpired(type)

        observePosts(dataExpired)
    }

    private suspend fun observePosts(refresh: Boolean = false) {
        postListStore
            .stream(StoreRequest.cached(postType!!, refresh))
            .distinctUntilChanged()
            .collect { response ->
                postsLoading = response is StoreResponse.Loading

                when (response) {
                    is StoreResponse.Data -> postList = response.dataOrNull() ?: listOf()
                    is StoreResponse.Error -> error = response.exception
                    else -> { /* noop */ }
                }
            }
    }

    suspend fun refreshPosts(force: Boolean) {
        postType?.let { type ->
            try {
                postsLoading = true
                when {
                    force -> postListStore.fresh(type)
                    else ->  postListStore.get(type)
                }
            } catch (ex: NetworkException) {
                Log.d("PostListUi", "Network Exception for ${type.displayName}")
            } finally {
                postsLoading = false
            }
        }
    }
}