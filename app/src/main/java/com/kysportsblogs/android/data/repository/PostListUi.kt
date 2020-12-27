package com.kysportsblogs.android.data.repository

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.stores.PostsStore
import com.kysportsblogs.android.ui.UiState
import com.kysportsblogs.android.util.AppDispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostListUi @Inject constructor(private val postsStore: PostsStore, private val dispatchers: AppDispatchers) {
private val mutext = Mutex()
    private var postType: PostType? = null

    private var postsLoading by mutableStateOf(false)
    private var postList: List<BlogPost>? by mutableStateOf(null)
    private var error: Throwable? by mutableStateOf(null)

    val state = derivedStateOf {
        UiState(loading = postsLoading, data = postList, error = error)
    }

    suspend fun init(type: PostType) {
        Log.d("PostListUi", "Init called for ${type.displayName}.")
       if (postType == null) {
           postType = type
           Log.d("PostListUi", "Initializing list for ${type.displayName}.")

           refreshPosts(false)

           withContext(dispatchers.io) {
               postsStore.stream(StoreRequest.cached(type, false))
                   .onCompletion {
                       Log.d("PostListUi", "Stream completed for ${type.displayName}.")
                       postType = null
                   }
                   .collect {
                       Log.d("PostListUi", "Got some posts for ${type.displayName}. Showing ${it.dataOrNull()?.size ?: 0} posts.")
                       postList = it.dataOrNull()?.take(5)
                   }
           }
       }
    }

    suspend fun refreshPosts(force: Boolean) {
        postType?.let { type ->
            Log.d("PostListUi", "Refreshing posts for ${type.displayName}.")
            mutext.withLock {
                postsLoading = true
            }
            withContext(dispatchers.io) {
                if (force) {
                    postsStore.fresh(type)
                } else {
                    postsStore.get(type)
                }
            }
            mutext.withLock {
                postsLoading = false
            }
        }
    }

}