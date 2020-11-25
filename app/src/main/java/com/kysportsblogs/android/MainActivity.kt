package com.kysportsblogs.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kysportsblogs.android.ui.Screen
import com.kysportsblogs.android.ui.theme.KsbTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.*
import com.kysportsblogs.android.ui.buildNavGraph


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchPosts()

        setContent {
            val navController = rememberNavController()
            KsbTheme {
                Scaffold(
                    topBar = {
                        val destination by navController.currentBackStackEntryAsState()
                        val isTopLevel = destination?.arguments?.getBoolean("isTopLevel") ?: false
                        val title = destination?.arguments?.getString("title") ?: "Kentucky Sports Blogs"
                        AppBar(title = title, !isTopLevel, onBackClick = { navController.navigateUp() })
                    }
                ) {
                    val posts by viewModel.posts.collectAsState(initial = listOf())

                    if (posts.isNotEmpty()) {
                        NavHost(navController = navController, startDestination = Screen.Home.route) {
                            buildNavGraph(navController, viewModel)
                        }
                    } else {
                        AppScreen {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(title: String, showBackButton: Boolean, onBackClick: ()->Unit = {}) {
    val label = @Composable { Text(text = title) }
    val icon = (@Composable {
        IconButton(onClick = onBackClick) {
            Icon(asset = Icons.Filled.ArrowBack)
        }
    }).takeIf { showBackButton }

    TopAppBar(
        title = label,
        navigationIcon = icon
    )
}


@Composable
fun AppScreen(content: @Composable () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
        content()
    }
}

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    KsbTheme(darkTheme = darkTheme) {
        Surface {
            content()
        }
    }
}