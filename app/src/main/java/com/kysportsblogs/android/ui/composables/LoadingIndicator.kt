package com.kysportsblogs.android.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize()) { CircularProgressIndicator(modifier = Modifier.align(
        Alignment.Center)) }
}