package com.kysportsblogs.android.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.ui.tooling.preview.Preview
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.*
import com.kysportsblogs.android.ui.composables.LoadingIndicator
import com.kysportsblogs.android.ui.composables.posts.PostListItem
import com.kysportsblogs.android.ui.theme.KsbTheme
import dev.chrisbanes.accompanist.imageloading.ImageLoadState

@Composable
fun CategoryScreen(viewModel: CategoryScreenViewModel, postType: PostType, onPostClicked: (Post) -> Unit = {}) {
    val uiState = viewModel.postsState
    LaunchedEffect(postType, viewModel) { viewModel.loadPosts(postType) }

    when {
        uiState.hasData -> CategoryPosts(posts = uiState.data!!, onPostClicked = onPostClicked)
        uiState.loading -> LoadingIndicator()
    }
}


@Composable
fun CategoryPosts(posts: List<BlogPost>, onPostClicked: (Post) -> Unit = {}) {
    LazyColumnForIndexed(items = posts) { index, post ->
        PostListItem(post = post, index = index, onPostClicked = onPostClicked)
    }
}


@Preview("Light Theme")
@Composable
fun CategoryPreview() {
    ThemedPreview {
        val posts = (1..10).map { previewPost }

        CategoryPosts(posts)
    }
}

@Preview("Dark Theme")
@Composable
fun CategoryPreviewDark() {
    ThemedPreview(darkTheme = true) {
        val posts = (1..10).map { previewPost }

        CategoryPosts(posts)
    }
}