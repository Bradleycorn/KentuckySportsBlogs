package com.kysportsblogs.android.ui.screens

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.data.repository.PostListUi

class CategoryScreenViewModel @ViewModelInject constructor(private val postListUi: PostListUi): ViewModel() {
    val postsState
        get() = postListUi.state

    suspend fun loadPosts(postType: PostType) {
        postListUi.loadPosts(postType)
    }
}