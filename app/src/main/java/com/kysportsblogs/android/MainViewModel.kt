package com.kysportsblogs.android

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kysportsblogs.android.data.models.BlogPost
import com.kysportsblogs.android.data.models.Post
import com.kysportsblogs.android.data.models.previewPost
import com.kysportsblogs.android.data.repository.KsrRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainViewModel @ViewModelInject constructor(private val ksrRepo: KsrRepository): ViewModel() {

    fun fetchPosts() {
        viewModelScope.launch {
            ksrRepo.fetchPosts()
        }
    }

    val posts: Flow<List<BlogPost>>
        get() = ksrRepo.getBlogPosts()


    fun getPost(postId: Long): Flow<BlogPost> {
        return ksrRepo.getPost(postId)
    }

    fun formatPostContent(html: String) = liveData {
        val doc = Jsoup.parse(html).apply {
            val body = select("body").first()
            select("head").first().apply {
                appendElement("meta").apply {
                    attr("name", "viewport")
                    attr("content", "width=device-width,user-scalable=yes")
                }
                appendElement("link").apply {
                    attr("rel", "stylesheet")
                    attr("type", "text/css")
                    attr("href", "post-css.css")
                }
            }

            val img = select("img.used-as-featured").first()
            val parent = img.parent()

            if (parent.hasClass("wp-caption")) {
                parent.remove()
            } else {
                img.remove()
            }
            body.appendElement("script").apply {
                attr("src", "post-scripts.js")
                attr("type", "text/javascript")
            }
        }

        emit(doc.html())
    }

}