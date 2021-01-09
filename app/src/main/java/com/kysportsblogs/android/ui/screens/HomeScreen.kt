package com.kysportsblogs.android.ui.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.AppScreen
import com.kysportsblogs.android.HomeScreenState
import com.kysportsblogs.android.HomeScreenViewModel
import com.kysportsblogs.android.data.models.*
import com.kysportsblogs.android.ui.UiState
import com.kysportsblogs.android.ui.composables.LoadingIndicator
import com.kysportsblogs.android.ui.composables.posts.*

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, onPostClicked: (Post) -> Unit = {}, onCategoryClicked: (PostType) -> Unit = {}) {
    val state = viewModel.homeScreenState
    LaunchedEffect(subject = viewModel) { viewModel.loadPosts() }

    AppScreen {
        when {
            state.initialLoad -> LoadingIndicator()
            else -> Home(state, onPostClicked = onPostClicked, onCategoryClicked = onCategoryClicked)
        }
    }
}


@Composable
private fun Home(uiState: HomeScreenState, onPostClicked: (Post) -> Unit = {}, onCategoryClicked: (PostType) -> Unit = {}) {

    val scroll = rememberScrollState(0F)

    ScrollableColumn(scrollState = scroll) {
        TopStoriesList(state = uiState.topStoriesState, onPostClicked = onPostClicked)

        CategoryStoriesList(
            state = uiState.footballState,
            type = PostType.FOOTBALL,
            onPostClicked = onPostClicked,
            onMoreClicked = onCategoryClicked
        )

        CategoryStoriesList(
            state = uiState.basketballState,
            type = PostType.BASKETBALL,
            onPostClicked = onPostClicked,
            onMoreClicked = onCategoryClicked
        )

        MoreStoriesList(state = uiState.otherPostsState, onPostClicked = onPostClicked, onMoreClicked = onCategoryClicked)
    }
}

@Composable
private fun SectionTitle(text: String, showMoreIcon: Boolean = false, showLoadingIcon: Boolean = false, onIconClicked: ()->Unit = {}) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showLoadingIcon) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(24.dp))
        }
        if (showMoreIcon) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
                    .clickable(onClick = onIconClicked)
                    .padding(4.dp)
                )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun TopStoriesList(state: UiState<List<BlogPost>>, onPostClicked: (Post) -> Unit) {

    if (state.initialLoad || state.hasData) {
        SectionTitle(
            text = "Top Stories",
            showLoadingIcon = state.loading
        )
        state.data?.let { posts ->
            posts.take(5).forEach { blogPost ->
                Row(modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 4.dp)) {
                    TopStory(blogPost = blogPost, onClick = onPostClicked)
                }
            }
        }
    }
}

@Composable
fun CategoryStoriesList(state: UiState<List<BlogPost>>, type: PostType, onPostClicked: (Post) -> Unit, onMoreClicked: (PostType) -> Unit = {}) {
    if (state.initialLoad || state.hasData) {
        SectionTitle(
            text = type.displayName,
            showLoadingIcon = state.loading,
            showMoreIcon = !state.loading,
            onIconClicked = { onMoreClicked(type) },
        )
        state.data?.let { posts ->
            LazyRowForIndexed(items = posts.take(5)) { index, blogPost ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Box(modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp)) {
                    PostTile(blogPost = blogPost, onPostClick = onPostClicked)
                }
            }
        }
    }
}

@Composable
fun MoreStoriesList(state: UiState<List<BlogPost>>, onPostClicked: (Post)->Unit, onMoreClicked: (PostType) -> Unit = {}) {
    if (state.initialLoad || state.hasData) {
        if (state.loading || !state.data.isNullOrEmpty()) {
            SectionTitle(
                text = PostType.OTHER.displayName,
                showLoadingIcon = state.loading,
                showMoreIcon = !state.loading,
                onIconClicked = { onMoreClicked(PostType.OTHER) })
            state.data?.let { posts ->
                posts.forEachIndexed { index, post ->
                    PostListItem(post = post, index = index, onPostClicked = onPostClicked)
                }
            }
        }
    }
}



//@Preview("Light Theme")
//@Composable
//fun HomePreview() {
//    val posts = (1..10).map { previewPost }
//    val recentPosts = RecentPosts(
//        topPosts = posts.take(3),
//        footballPosts = posts.take(5),
//        basketballPosts = posts.take(4),
//        otherPosts = posts,
//        footballCategory = null,
//        basketballCategory = null
//    )
//
//    ThemedPreview {
//        Home(recentPosts)
//    }
//}
//
//@Preview("Dark Theme")
//@Composable
//fun HomePreviewDark() {
//    val posts = (1..10).map { previewPost }
//    val recentPosts = RecentPosts(
//        topPosts = posts.take(3),
//        footballPosts = posts.take(5),
//        basketballPosts = posts.take(4),
//        otherPosts = posts,
//        footballCategory = null,
//        basketballCategory = null
//    )
//
//    ThemedPreview(darkTheme = true) {
//        Home(recentPosts)
//    }
//}