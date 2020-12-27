package com.kysportsblogs.android.ui.composables.posts

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.ui.theme.surfaceSecondary

@Composable
fun PostListItem(post: BlogPost, index: Int, onPostClicked: (Post) -> Unit = {}) {
    if (index == 0) {
        FeaturedPost(blogPost = post, onClick = onPostClicked)
    } else {
        val background = when (index.rem(2)) {
            1 -> MaterialTheme.colors.surfaceSecondary
            else -> MaterialTheme.colors.surface
        }
        Surface(color = background) {
            PostExcerpt(blogPost = post, modifier = Modifier.padding(16.dp), onClick = onPostClicked)
        }
    }
}