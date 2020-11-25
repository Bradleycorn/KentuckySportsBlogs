package com.kysportsblogs.android.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.kysportsblogs.android.MainViewModel
import com.kysportsblogs.android.ui.screens.HomeScreen
import com.kysportsblogs.android.ui.screens.PostScreen

sealed class Screen(val route: String, val title: String, val isTopLevel: Boolean = false) {
    object Home: Screen("home/", "Kentucky Sports Blogs", true)
    object Post: Screen("post/{postId}", "Blog Post") {
        fun buildRoute(postId: Long): String {
            return "post/$postId"
        }
    }
}

fun NavGraphBuilder.buildNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    composable(Screen.Home.route,
        arguments = listOf(
            navArgument("title") { defaultValue = Screen.Home.title },
            navArgument("isTopLevel") { defaultValue = true })
    ) {
        HomeScreen(viewModel, onPostClicked = { post ->
            navController.navigate(Screen.Post.buildRoute(post.postId))
        })
    }

    composable(Screen.Post.route,
    arguments = listOf(
        navArgument("postId") { type = NavType.LongType})
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getLong("postId") ?: 0
        PostScreen(viewModel, postId)
    }
}

fun NavHostController.navigateHome() { 
    popBackStack(graph.startDestination, false)
}