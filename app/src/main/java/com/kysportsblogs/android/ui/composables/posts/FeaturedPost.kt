package com.kysportsblogs.android.ui.composables.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost

@Composable
fun FeaturedPost(blogPost: BlogPost, onClick: (Post) -> Unit = {}) {
    Column {
        PostImage(blogPost.post.imageUrl ?: "")
        PostExcerpt(blogPost = blogPost,
            modifier = Modifier.padding(16.dp),
            showDescription = true,
            onClick = onClick)
    }
}


@Preview("Featured Post")
@Composable
fun PreviewPost() {
    ThemedPreview() {
        FeaturedPost(previewPost)
    }
}

@Preview("Featured Post - Dark")
@Composable
fun PreviewPostDark() {
    ThemedPreview(darkTheme = true) {
        FeaturedPost(previewPost)
    }
}