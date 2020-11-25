package com.kysportsblogs.android.ui.screens

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kysportsblogs.android.AppScreen
import com.kysportsblogs.android.MainViewModel
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.contains
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.theme.surfaceSecondary


@Composable
fun HomeScreen(viewModel: MainViewModel, onPostClicked: (Post) -> Unit = {}) {
    AppScreen {
        val posts by viewModel.posts.collectAsState(initial = null)
        posts?.let { postList ->
            Home(posts = postList, onPostClicked = onPostClicked)
        }
    }
}

@Composable
private fun Home(posts: List<BlogPost>, onPostClicked: (Post) -> Unit = {}) {
    val topPosts = posts.filter { it.categories.contains("Top Story") }
    val footballPosts = posts.filter { it.categories.contains("Football") }
    val basketballPosts = posts.filter { it.categories.contains("Basketball") }
    val otherPosts = posts.filterNot {
        it.categories.contains("Top Story")
                || it.categories.contains("Football")
                || it.categories.contains("Basketball")
    }

    val scroll = rememberScrollState(0F)

    ScrollableColumn(scrollState = scroll) {
        SectionTitle("Top Stories")
        topPosts.forEachIndexed { index, blogPost ->
            TopStory(blogPost = blogPost, onClick = onPostClicked)
        }

        SectionTitle(text = "Football")
        LazyRowForIndexed(items = footballPosts) { index, blogPost ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            PostTile(blogPost = blogPost, onPostClick = onPostClicked)
        }

        SectionTitle(text = "Basketball")
        LazyRowForIndexed(items = basketballPosts) { index, blogPost ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            PostTile(blogPost = blogPost, onPostClick = onPostClicked)
        }

        SectionTitle(text = "More Stories")
        MoreStoriesList(posts = otherPosts, onPostClicked = onPostClicked)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun StoryCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp),
        elevation = 4.dp,
        content = content
    )
}

@Composable
fun MoreStoriesList(posts: List<BlogPost>, onPostClicked: (Post)->Unit) {
    posts.forEachIndexed { index, post ->
        if (index == 0) {
            FeaturedPost(blogPost = post, onClick = onPostClicked)
        } else {
            val background = when (index.rem(2)) {
                1 -> MaterialTheme.colors.surfaceSecondary
                else -> MaterialTheme.colors.surface
            }
            Surface(color = background) {
                PostExcerpt(blogPost = post, onClick = onPostClicked)
            }
        }
    }
}



@Preview
@Composable
fun newHomePreview() {
    val posts = (1..10).map { previewPost }


    ThemedPreview(darkTheme = true) {
        Home(posts)
    }
}