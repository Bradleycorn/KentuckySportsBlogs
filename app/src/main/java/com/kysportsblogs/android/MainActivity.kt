package com.kysportsblogs.android

import android.app.Application
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.platform.setContent
import androidx.hilt.lifecycle.HiltViewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kysportsblogs.android.ui.Screen
import com.kysportsblogs.android.ui.theme.KsbTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.*
import com.kysportsblogs.android.di.ProvideNavigationViewModelFactoryMap
import com.kysportsblogs.android.ui.buildNavGraph

val AmbientApplication = staticAmbientOf<Application>()
val AmbientNavController = staticAmbientOf<NavController>()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            Providers(
                AmbientApplication provides application,
                AmbientNavController provides navController
            ) {


                KsbTheme {
                    Scaffold(
                        topBar = {
                            val destination by navController.currentBackStackEntryAsState()

                            val isTopLevel =
                                destination?.arguments?.getBoolean("isTopLevel") ?: false
                            val title = destination?.arguments?.getString("title")
                                ?: "Kentucky Sports Blogs"
                            AppBar(
                                title = title,
                                !isTopLevel,
                                onBackClick = { navController.navigateUp() })
                        }
                    ) {
                        ProvideNavigationViewModelFactoryMap(factory = defaultViewModelProviderFactory as HiltViewModelFactory) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Home.route
                            ) {
                                buildNavGraph(navController)
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
            Icon(imageVector = Icons.Filled.ArrowBack)
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
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}