package com.kysportsblogs.android

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kysportsblogs.android.data.database.KsbDatabase
import com.kysportsblogs.android.data.models.RecentPosts
import com.kysportsblogs.android.data.repository.KsrRepository
import com.kysportsblogs.android.ui.UiState

class CategoryViewModel @ViewModelInject constructor(private val db: KsbDatabase, private val ksrRepo: KsrRepository): ViewModel() {

//    private var fetchingPosts by mutableStateOf(false)
//
//    private var recentPosts: RecentPosts? by mutableStateOf(null)
//
//    val uiState by derivedStateOf {
//        recentPosts?.let { model ->
//            when {
//                model.hasPosts -> UiState.Data(model)
//                fetchingPosts -> UiState.Loading()
//                else -> UiState.Error("No posts found.")
//            }
//        } ?: UiState(loading = true)
//    }

//    suspend fun updatePosts() {
//        fetchingPosts = true
//        ksrRepo.updatePosts()
//        updateRecentPosts()
//        fetchingPosts = false
//    }

}