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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HomeScreenViewModel @ViewModelInject constructor(
    private val footballUi: PostListUi,
    private val basketballUi: PostListUi,
    private val topStoriesUi: PostListUi,
    private val otherPostsUi: PostListUi
): ViewModel() {

    private val mutex = Mutex()
    val homeScreenState by derivedStateOf {
        Log.d("HomeScreenViewModel", "Setting HomeScreenState.")

        HomeScreenState(
            footballState = footballUi.state,
            basketballState = basketballUi.state,
            topStoriesState = topStoriesUi.state,
            otherPostsState = otherPostsUi.state
        )
    }

    init {
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Initializing State for Top_Stories.")
            topStoriesUi.init(PostType.TOP_STORIES)
        }
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Initializing State for Football.")
            footballUi.init(PostType.FOOTBALL)
        }
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Initializing State for Basketball.")
            basketballUi.init(PostType.BASKETBALL)
        }
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Initializing State for Other.")
            otherPostsUi.init(PostType.OTHER)
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



//        footballPosts?.let { model ->
//            when {
//                model.isNotEmpty() -> UiState.Data(model)
//                fetchingPosts -> UiState.Loading()
//                else -> UiState.Error("No posts found.")
//            }
//        } ?: UiState(loading = true)



    override fun onCleared() {
        super.onCleared()
        Log.d("MAIN_ACTIVITY_VM", "On Cleared!")
    }
}

data class HomeScreenState(
    val footballState: State<UiState<List<BlogPost>>>,
    val basketballState: State<UiState<List<BlogPost>>>,
    val topStoriesState: State<UiState<List<BlogPost>>>,
    val otherPostsState: State<UiState<List<BlogPost>>>,
) {
    val isLoadingAll: Boolean
        get() = footballState.value.loading
            && basketballState.value.loading
            && topStoriesState.value.loading
            && otherPostsState.value.loading

    val isLoadingAny: Boolean
        get() = footballState.value.loading
            || basketballState.value.loading
            || topStoriesState.value.loading
            || otherPostsState.value.loading
}