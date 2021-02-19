package com.kysportsblogs.android.ui

import android.os.Bundle
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.navigation.*
import androidx.navigation.compose.*
import com.kysportsblogs.android.HomeScreenViewModel
import com.kysportsblogs.android.data.models.PostType
import com.kysportsblogs.android.ui.screens.*

sealed class Screen(val route: String, val title: String, val isTopLevel: Boolean = false) {
    object Home: Screen("home/", "Kentucky Sports Blogs", true)
    object Post: Screen("post/{postId}", "Blog Post") {
        fun buildRoute(postId: Long): String {
            return "post/$postId"
        }
    }
    object Category: Screen("category/{postType}?title={title}", "Category") {
        val postTypeArgType = NavType.EnumType(PostType::class.java)

        fun buildRoute(postType: PostType): String {
            return "category/$postType?title=${postType.displayName}"
        }
    }
}

fun NavGraphBuilder.buildNavGraph(navController: NavHostController) {
    composable(Screen.Home.route,
        arguments = listOf(
            navArgument("title") { defaultValue = Screen.Home.title },
            navArgument("isTopLevel") { defaultValue = Screen.Home.isTopLevel })
    ) {

        val vm: HomeScreenViewModel = viewModel(factory = HiltViewModelFactory(AmbientContext.current, it))

        HomeScreen(vm,
            onPostClicked = { post ->
                navController.navigate(Screen.Post.buildRoute(post.postId))
            },
            onCategoryClicked = { postType ->
                navController.navigate(Screen.Category.buildRoute(postType))
            }
        )
    }

    composable(Screen.Post.route,
        arguments = listOf(
                navArgument("postId") { type = NavType.LongType}
        )

    ) { backStackEntry ->
        val vm: PostScreenViewModel = viewModel(factory = HiltViewModelFactory(AmbientContext.current, backStackEntry))
        val postId = backStackEntry.arguments?.getLong("postId") ?: 0
        PostScreen(vm, postId)
    }

    composable(Screen.Category.route,
        arguments = listOf(
            navArgument("postType") { type = Screen.Category.postTypeArgType },
            navArgument("title") { defaultValue = "Unknown Category" }
        )
    ) { backStackEntry ->
        val vm: CategoryScreenViewModel = viewModel(factory = HiltViewModelFactory(AmbientContext.current, backStackEntry))
        val postType = backStackEntry.arguments?.getSerializable("postType") as? PostType

        postType?.let {
            CategoryScreen(vm, postType, onPostClicked = { post ->
                navController.navigate(Screen.Post.buildRoute(post.postId))
            })
        } ?: throw IllegalArgumentException("Invalid PostType when trying to navigate to CategoryScreen.")
    }

}

fun NavHostController.navigateHome() { 
    popBackStack(graph.startDestination, false)
}