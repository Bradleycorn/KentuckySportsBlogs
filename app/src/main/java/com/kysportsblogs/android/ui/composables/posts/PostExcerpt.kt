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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.theme.primaryOnSurface

@Composable
fun PostExcerpt(blogPost: BlogPost,
                modifier: Modifier = Modifier,
                showDescription: Boolean = true,
                titleMaxLines: Int = Int.MAX_VALUE,
                onClick: (Post)->Unit = {}) {
    val post = blogPost.post

    Column(modifier = modifier.clickable(onClick = { onClick(post) })) {
        Row {
            Column {
                Providers(AmbientContentAlpha provides ContentAlpha.high) {
                    Text(text = post.title,
                        color = MaterialTheme.colors.primaryOnSurface,
                        maxLines = titleMaxLines,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.subtitle1)
                }
                Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                    Row {
                        PostMetaData(post = post)
                    }
                }
            }
        }
        if (showDescription) {
            Spacer(Modifier.height(8.dp))
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(text = post.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun PostMetaData(post: Post, modifier: Modifier = Modifier) {
        Providers(AmbientContentAlpha provides ContentAlpha.high) {
            Text(text = post.displayDate, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.width(16.dp))

        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
            Text(text = post.author, style = MaterialTheme.typography.body2)
        }
//    Row {
//        Providers(AmbientContentAlpha provides ContentAlpha.high) {
//            Text(text = post.displayDate, style = MaterialTheme.typography.body2)
//        }
//        Spacer(modifier = Modifier.width(16.dp))
//
//        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
//            Text(text = post.author, style = MaterialTheme.typography.body2)
//        }
//    }
}


@Preview("Post Excerpt")
@Composable
fun PreviewPostExcerpt() {
    ThemedPreview() {
        PostExcerpt(blogPost = previewPost, modifier = Modifier.padding(16.dp))
    }
}

@Preview("Post Excerpt - Dark")
@Composable
fun PreviewPostExcerptDark() {
    ThemedPreview(darkTheme = true) {
        PostExcerpt(blogPost = previewPost, modifier = Modifier.padding(16.dp))
    }
}