package com.kysportsblogs.android

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.repository.PostListUi
import com.kysportsblogs.android.ui.UiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HomeScreenViewModel @ViewModelInject constructor(
    private val footballUi: PostListUi,
    private val basketballUi: PostListUi,
    private val topStoriesUi: PostListUi,
    private val otherPostsUi: PostListUi
): ViewModel() {

    val homeScreenState by derivedStateOf {
        HomeScreenState(
            footballState = footballUi.state,
            basketballState = basketballUi.state,
            topStoriesState = topStoriesUi.state,
            otherPostsState = otherPostsUi.state
        )
    }

    suspend fun loadPosts() {
        supervisorScope {
            launch { topStoriesUi.loadPosts(PostType.TOP_STORIES) }
            launch { footballUi.loadPosts(PostType.FOOTBALL) }
            launch { basketballUi.loadPosts(PostType.BASKETBALL) }
            launch { otherPostsUi.loadPosts(PostType.OTHER) }
        }
    }


    fun onErrorDisplayed() {
//        footballError = null
    }

    fun onRefresh() {
        viewModelScope.launch {
            footballUi.refreshPosts(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MAIN_ACTIVITY_VM", "On Cleared!")
    }
}

data class HomeScreenState(
    val footballState: UiState<List<BlogPost>>,
    val basketballState: UiState<List<BlogPost>>,
    val topStoriesState: UiState<List<BlogPost>>,
    val otherPostsState: UiState<List<BlogPost>>,
) {
    val hasData: Boolean
        get() = footballState.hasData
                || basketballState.hasData
                || topStoriesState.hasData
                || otherPostsState.hasData

    val isLoadingAll: Boolean
        get() = footballState.loading
            && basketballState.loading
            && topStoriesState.loading
            && otherPostsState.loading

    val isLoadingAny: Boolean
        get() = footballState.loading
            || basketballState.loading
            || topStoriesState.loading
            || otherPostsState.loading

    val initialLoad: Boolean
        get() = footballState.initialLoad
                && basketballState.initialLoad
                && topStoriesState.initialLoad
                && otherPostsState.initialLoad

}