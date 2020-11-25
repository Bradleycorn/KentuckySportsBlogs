package com.kysportsblogs.android.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.ui.tooling.preview.Preview
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import coil.transform.CircleCropTransformation
import com.kysportsblogs.android.AppScreen
import com.kysportsblogs.android.MainViewModel
import com.kysportsblogs.android.R
import com.kysportsblogs.android.ThemedPreview
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.ui.theme.primaryOnSurface
import com.kysportsblogs.android.util.extensions.getColorFromAttr
import com.kysportsblogs.android.util.extensions.toString
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun PostScreen(viewModel: MainViewModel, postId: Long) {
    val post by viewModel.getPost(postId).collectAsState(null)
    post?.let { blogPost ->
        AppScreen {
            val postContent by viewModel.formatPostContent(blogPost.post.content).observeAsState(initial = null)
            PostContent(blogPost.post, postContent)
        }
    }
}

@Composable
private fun PostContent(post: Post, postContent: String?) {
   Column {
        PostImage(url = post.imageUrl)
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            PostDetails(post)
            postContent?.let {
                PostBody(postContent)
            }
        }
    }
}

@Composable
private fun PostDetails(post: Post) {
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
            modifier = Modifier.width(36.dp).height(36.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = post.author)
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(text = post.datePosted.toString("MMMM dd, h:mm a"))
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PostBody(html: String, modifier: Modifier = Modifier) {
    val darkModeEnabled = !MaterialTheme.colors.isLight

    AndroidView(viewBlock = { context ->
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
        PostContent(post = previewPost.post, previewPost.post.content)
    }
}