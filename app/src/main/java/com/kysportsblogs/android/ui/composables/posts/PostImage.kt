package com.kysportsblogs.android.ui.composables.posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kysportsblogs.android.R
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage


@Composable
fun PostImage(url: String?, modifier: Modifier = Modifier) {

    val hasLayout = modifier.any { it is LayoutModifier }
    val imageModifier = when {
        hasLayout -> modifier
        else -> modifier.fillMaxWidth().aspectRatio(1.78F)
    }

    when (url) {
        null -> DefaultPostImage(imageModifier)
        else -> CoilImage(
            data = url,
            modifier = imageModifier) { imageState ->
            when (imageState) {
                is ImageLoadState.Loading -> Box(modifier = imageModifier)
                is ImageLoadState.Success -> MaterialLoadingImage(
                    result = imageState,
                    fadeInEnabled = true,
                    fadeInDurationMs = 600,
                    contentScale = ContentScale.Crop
                )
                else -> DefaultPostImage(imageModifier)
            }
        }
    }
}

@Composable
private fun DefaultPostImage(modifier: Modifier = Modifier) {
    Image(
        imageVector = vectorResource(id = R.drawable.ic_uk_logo),
        modifier = modifier.padding(4.dp),
        contentScale = ContentScale.Fit
    )
}