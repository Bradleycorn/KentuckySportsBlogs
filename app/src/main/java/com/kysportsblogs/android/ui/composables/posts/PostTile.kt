package com.kysportsblogs.android.ui.composables.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.composables.StoryCard
import com.kysportsblogs.android.ui.theme.primaryOnSurface

@Composable
fun PostTile(blogPost: BlogPost, onPostClick: (Post)->Unit = {}) {
    val post = blogPost.post
    val modifier = Modifier
        .clickable(onClick = { onPostClick(post) })
        .preferredSize(width = 172.dp, height = 208.dp)

    StoryCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxHeight()) {
            PostImage(url = post.imageUrl)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.title,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primaryOnSurface
            )
            Spacer(modifier = Modifier.weight(1F))
            Column(modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)) {
                PostMetaData(post = post)
            }
        }

    }
}

@Preview("Post Tile")
@Composable
fun PreviewPostTile() {
    ThemedPreview {
        Box(modifier = Modifier.padding(8.dp)) {
            PostTile(blogPost = previewPost)
        }
    }
}

@Preview("Post Tile - Dark")
@Composable
fun PreviewPostTileDark() {
    ThemedPreview(darkTheme = true) {
        Box(modifier = Modifier.padding(8.dp)) {
            PostTile(blogPost = previewPost)
        }
    }
}