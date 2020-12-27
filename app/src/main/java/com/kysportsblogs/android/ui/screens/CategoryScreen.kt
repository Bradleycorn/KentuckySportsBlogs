package com.kysportsblogs.android.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kysportsblogs.android.MainViewModel
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Category
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.composables.posts.PostListItem
import com.kysportsblogs.android.ui.theme.KsbTheme

@Composable
fun CategoryScreen(viewModel: MainViewModel, categoryId: Int, onPostClicked: (Post) -> Unit = {}) {

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