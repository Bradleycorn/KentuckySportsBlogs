package com.kysportsblogs.android.ui.composables.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.composables.StoryCard

@Composable
fun TopStory(blogPost: BlogPost, onClick: (Post) -> Unit = {}) {
    val post = blogPost.post
    val height = 85.dp
    StoryCard(modifier = Modifier.clickable(onClick = { onClick(post) })) {
        Row(modifier = Modifier.height(height).fillMaxWidth()) {
            PostImage(url = post.imageUrl, modifier = Modifier.preferredSize(height))
            PostExcerpt(
                blogPost = blogPost,
                modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.CenterVertically),
                titleMaxLines = 2,
                showDescription = false,
                onClick = onClick)
        }
    }
}

@Preview("Top Story")
@Composable
fun PreviewTopStory() {
    ThemedPreview {
        Box(modifier = Modifier.padding(8.dp)) {
            TopStory(blogPost = previewPost)
        }
    }
}

@Preview("Top Story - Dark")
@Composable
fun PreviewTopStoryDark() {
    ThemedPreview(darkTheme = true) {
        Box(modifier = Modifier.padding(8.dp)) {
            TopStory(blogPost = previewPost)
        }
    }
}