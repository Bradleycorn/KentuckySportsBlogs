package com.kysportsblogs.android.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import coil.transform.CircleCropTransformation
import com.kysportsblogs.android.AppScreen
import com.kysportsblogs.android.R
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.theme.primaryOnSurface
import com.kysportsblogs.android.ui.UiState
import com.kysportsblogs.android.extensions.getColorFromAttr
import com.kysportsblogs.android.extensions.toString
import com.kysportsblogs.android.ui.composables.LoadingIndicator
import com.kysportsblogs.android.ui.composables.posts.PostImage
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun PostScreen(viewModel: PostScreenViewModel, postId: Long,) {
    val post = viewModel.postState
    LaunchedEffect(viewModel, postId) { viewModel.loadPost(postId) }

    when {
        post.loading -> LoadingIndicator()
        post.hasError -> Text("Uh Oh!")
        post.hasData -> PostContent(blogPost = post.data!!)
    }
}

@Composable
private fun PostContent(blogPost: BlogPost) {
    val post = blogPost.post
    val postContent by produceState(initialValue = UiState.Loading<String>(), key1 = post) {
        val content = post.formatContent()
        value = value.copy(loading = false, data = content)
    }

    AppScreen {
        Column {
            // This is a "workaround" for a defect with WebViews in Compose (See: https://issuetracker.google.com/issues/174233728)
            // Because of the defect, you can see a flash of the post image and title and then it goes away when the post content loads.
            // This code shows a loading indicator while the content is loading, and then shows all screen elements at the same time.
            // The defect still persists, the image and title don't show up until you scroll. But at least they don't flash.
            // When the defect is resolved, this can be updated to show the image and title right away and only delay the content composable.
            when {
                postContent.loading -> LoadingIndicator()
                postContent.data != null -> {
                    LazyColumn {
                        item {
                            PostImage(url = post.imageUrl)
                        }

                        item {
                            PostDetails(post, modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp))
                        }
                        item {
                            PostBody(postContent.data!!, modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp))

                        }
                    }

//                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//                        PostDetails(post)
//                        PostBody(postContent.data!!)
//                    }



                }
            }
        }
    }
}

@Composable
private fun PostDetails(post: Post, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Providers(AmbientContentAlpha provides ContentAlpha.high) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primaryOnSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            CoilImage(
                data = post.authorImage ?: "",
                requestBuilder = {
                    transformations(CircleCropTransformation())
                },
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Fit,
                contentDescription = "Post Thumbnail"
            )

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(text = post.author)
                Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                    Text(text = post.datePosted.toString("MMMM dd, h:mm a"))
                }
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PostBody(html: String, modifier: Modifier = Modifier) {
    val darkModeEnabled = !MaterialTheme.colors.isLight

    AndroidView(modifier = modifier, viewBlock = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            isVerticalScrollBarEnabled = false
            setBackgroundColor(context.getColorFromAttr(R.attr.colorSurface))
            if (darkModeEnabled) {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON);
                }
            }
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(settings, WebSettingsCompat.DARK_STRATEGY_USER_AGENT_DARKENING_ONLY);
            }
        }
    },
    update = {
        it.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "base64", null)
    })
}

@Preview
@Composable
fun PreviewPostScreen() {
    ThemedPreview {
        PostContent(blogPost = previewPost)
    }
}