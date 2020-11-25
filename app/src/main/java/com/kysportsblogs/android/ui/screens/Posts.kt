package com.kysportsblogs.android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kysportsblogs.android.R
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.theme.primaryOnSurface
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage


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
        TopStory(blogPost = previewPost)
    }
}

@Preview("Top Story - Dark")
@Composable
fun PreviewTopStoryDark() {
    ThemedPreview(darkTheme = true) {
        TopStory(blogPost = previewPost)
    }
}



@Composable
fun PostExcerpt(blogPost: BlogPost,
                modifier: Modifier = Modifier.padding(16.dp),
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
                    PostMetaData(post = post)
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

@Preview("Post Excerpt")
@Composable
fun PreviewPostExcerpt() {
    ThemedPreview() {
        PostExcerpt(blogPost = previewPost)
    }
}

@Preview("Post Excerpt - Dark")
@Composable
fun PreviewPostExcerptDark() {
    ThemedPreview(darkTheme = true) {
        PostExcerpt(blogPost = previewPost)
    }
}

@Composable
fun PostTile(blogPost: BlogPost, onPostClick: (Post)->Unit = {}) {
    val post = blogPost.post
    val modifier = Modifier
        .clickable(onClick = { onPostClick(post) })
        .preferredSize(width = 170.dp, height = 200.dp)

    StoryCard(modifier = modifier) {
        Column {
            PostImage(url = post.imageUrl)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.title,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primaryOnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(text = post.author,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview("Post Tile")
@Composable
fun PreviewPostTile() {
    ThemedPreview {
        PostTile(blogPost = previewPost)
    }
}

@Preview("Post Tile - Dark")
@Composable
fun PreviewPostTileDark() {
    ThemedPreview(darkTheme = true) {
        PostTile(blogPost = previewPost)
    }
}



@Composable
fun FeaturedPost(blogPost: BlogPost, onClick: (Post) -> Unit = {}) {
    Column {
        PostImage(blogPost.post.imageUrl ?: "")
        PostExcerpt(blogPost = blogPost,
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


@Composable
fun PostImage(url: String?, modifier: Modifier = Modifier.fillMaxWidth().aspectRatio(1.78F)) {
    when (url) {
        null -> DefaultPostImage(modifier)
        else -> CoilImage(
            data = url,
            modifier = modifier) { imageState ->
            when (imageState) {
                is ImageLoadState.Success -> MaterialLoadingImage(
                    result = imageState,
                    fadeInEnabled = true,
                    fadeInDurationMs = 600,
                    contentScale = ContentScale.Crop
                )
                else -> DefaultPostImage(modifier)
            }
        }
    }
}

@Composable
private fun DefaultPostImage(modifier: Modifier = Modifier) {
    Image(
        asset = vectorResource(id = R.drawable.ic_uk_logo),
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun PostMetaData(post: Post) {
    Row {
        Providers(AmbientContentAlpha provides ContentAlpha.high) {
            Text(text = post.displayDate, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.width(16.dp))

        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
            Text(text = post.author, style = MaterialTheme.typography.body2)
        }
    }
}



