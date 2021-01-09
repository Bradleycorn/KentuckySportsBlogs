package com.kysportsblogs.android.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.get
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.network.NetworkException
import com.kysportsblogs.android.data.stores.PostStore
import com.kysportsblogs.android.ui.UiState
import kotlinx.coroutines.launch


class PostScreenViewModel @ViewModelInject constructor(private val postStore: PostStore): ViewModel() {
    var postState by mutableStateOf(UiState<BlogPost>())
        private set

    fun loadPost(id: Long) {
        viewModelScope.launch {
            postState = postState.copy(loading = true)
            postState = try {
                postState.copy(data = postStore.get(id))
            } catch (ex: NetworkException) {
                postState.copy(error = ex)
            }
            postState = postState.copy(loading = false)
        }
    }

    override fun onCleared() {
        super.onCleared()

        Log.d("VM_POSTSCREEN", "Post Screen VM Cleared!")
    }
}