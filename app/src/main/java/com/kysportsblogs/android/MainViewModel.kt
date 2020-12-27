package com.kysportsblogs.android

import androidx.compose.runtime.*
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Category
import com.kysportsblogs.android.data.models.RecentPosts
import com.kysportsblogs.android.data.repository.KsrRepository
import com.kysportsblogs.android.ui.UiState
import kotlinx.coroutines.flow.*
import org.jsoup.Jsoup

class MainViewModel @ViewModelInject constructor(private val ksrRepo: KsrRepository): ViewModel() {

//    private var fetchingPosts by mutableStateOf(false)
//
//    private var recentPosts: RecentPosts? by mutableStateOf(null)
//
//    val uiState by derivedStateOf {
//        recentPosts?.let { model ->
//            when {
//                model.hasPosts -> UiState.Data(model)
//                fetchingPosts -> UiState.Loading()
//                else -> UiState.Error("No posts found.")
//            }
//        } ?: UiState(loading = true)
//    }

//    suspend fun updatePosts() {
//        fetchingPosts = true
//        ksrRepo.updatePosts()
//        updateRecentPosts()
//        fetchingPosts = false
//    }
//
//    private suspend fun updateRecentPosts() {
//        val posts = ksrRepo.getRecentPosts()
//
//        val categories = posts.flatMap { it.categories }
//        val topStoriesCategory = categories.find { it.name == Category.CATEGORY_TOP_STORY }
//        val footballCategory = categories.find { it.name == Category.CATEGORY_FOOTBALL }
//        val basketballCategory = categories.find { it.name == Category.CATEGORY_BASKETBALL }
//
//        val topPosts = posts.filter { it.categories.contains(topStoriesCategory) }
//        val footballPosts = posts.filter { it.categories.contains(footballCategory) && !it.categories.contains(topStoriesCategory) }
//        val basketballPosts = posts.filter { it.categories.contains(basketballCategory) && !it.categories.contains(topStoriesCategory) }
//        val otherPosts = posts.filterNot {
//            it.categories.contains(topStoriesCategory)
//                || it.categories.contains(footballCategory)
//                || it.categories.contains(basketballCategory)
//        }
//
//        recentPosts = RecentPosts(
//            topPosts = topPosts,
//            footballPosts = footballPosts,
//            basketballPosts = basketballPosts,
//            otherPosts = otherPosts,
//            footballCategory = footballCategory,
//            basketballCategory = basketballCategory
//        )
//    }
//
//    fun getPost(postId: Long): Flow<UiState<BlogPost>> {
//        return try {
//            ksrRepo.getPost(postId).map { UiState.Data(it) }
//        } catch (ex: Exception) {
//            flowOf(UiState.Error("Could not load post."))
//        }
//    }
//
//    fun formatPostContent(html: String) = liveData {
//        val doc = Jsoup.parse(html).apply {
//            val body = select("body").first()
//            select("head").first().apply {
//                appendElement("meta").apply {
//                    attr("name", "viewport")
//                    attr("content", "width=device-width,user-scalable=yes")
//                }
//                appendElement("link").apply {
//                    attr("rel", "stylesheet")
//                    attr("type", "text/css")
//                    attr("href", "post-css.css")
//                }
//            }
//
//            val img = select("img.used-as-featured").first()
//            val parent = img.parent()
//
//            if (parent.hasClass("wp-caption")) {
//                parent.remove()
//            } else {
//                img.remove()
//            }
//            body.appendElement("script").apply {
//                attr("src", "post-scripts.js")
//                attr("type", "text/javascript")
//            }
//        }
//
//        emit(doc.html())
//    }

}