package com.kysportsblogs.android.data.models

import android.text.Html
import androidx.room.*
import com.kysportsblogs.android.data.network.responseModels.WordpressPost
import com.kysportsblogs.android.data.network.responseModels.WpMediaDetails
import com.kysportsblogs.android.util.extensions.*
import java.util.*


@Entity(tableName = "Posts")
data class Post(
    @PrimaryKey
    val postId: Long,
    val url: String,
    val datePosted: Date,
    val dateModified: Date?,
    val title: String,
    val content: String,
    val description: String,
    val author: String,
    val authorImage: String?,
    val imageUrl: String?
) {
    val displayDate: String
        get() {

            return when {
                datePosted.isToday -> datePosted.toString(DATE_FORMAT_TIME)
                datePosted.isYesterday -> "Yesterday"
                else -> datePosted.toString(DATE_FORMAT_DAY_OF_MONTH)
            }
        }

    companion object {
//        fun fromRss(rss: RssItem): Post {
//            val timestamp = rss.pubDate.toDate(RssItem.PUB_DATE_FORMAT)?.time ?: 0
//
//            val html = Jsoup.parse(rss.content)
//            val imageUrl = try { html.select("img.used-as-featured").first().attr("src") } catch(ex: Exception) { null }
//
//            html.outputSettings().prettyPrint(false)
//            val content = html.html()
//            return Post(rss.guid, imageUrl, rss.title, rss.description, rss.creator, timestamp, content)
//        }

        fun fromWordpress(posts: List<WordpressPost>?): List<BlogPost> = posts?.map { fromWordpress(it) } ?: listOf()

        fun fromWordpress(wpPost: WordpressPost): BlogPost {
            val description = Html.fromHtml(wpPost.excerpt.rendered, Html.FROM_HTML_MODE_LEGACY).toString()

            val author = wpPost.embedded.author?.find { it.id == wpPost.author }
            val authorName = author?.name ?: ""
            val authorImage = author?.avatar_urls?.large ?: author?.avatar_urls?.medium

            val image = getImageUrl(wpPost.embedded.media?.find { it.id == wpPost.featured_media }?.media_details)

            val categories = Category.fromWpTerms(wpPost.embedded.terms)

            val post = Post(
                postId = wpPost.id,
                url = wpPost.link,
                datePosted = wpPost.postDate ?: Date(),
                dateModified = wpPost.modifiedDate,
                title = Html.fromHtml(wpPost.title.rendered, Html.FROM_HTML_MODE_COMPACT).toString(),
                content = wpPost.content.rendered,
                description = description,
                author = authorName,
                authorImage = authorImage,
                imageUrl = image
            )

            return BlogPost(post, categories)
        }

        private fun getImageUrl(media: WpMediaDetails?): String {
            return when (media) {
                null -> ""
                else -> media.sizes.medium_large?.source_url ?: media.sizes.large?.source_url ?: media.sizes.full?.source_url ?: ""
            }
        }
    }
}

@Entity(primaryKeys = ["postId","categoryId"])
data class PostCategories(
    val postId: Long,
    val categoryId: Long
)

data class BlogPost(
   @Embedded val post: Post,
   @Relation(
       parentColumn = "postId",
       entityColumn = "categoryId",
       associateBy = Junction(PostCategories::class)
   )
   val categories: List<Category>
) {
    val postCategories: List<PostCategories>
        get() = categories.map { category -> PostCategories(post.postId, category.categoryId) }
}

val previewPost = BlogPost(
    Post(
        postId = 1,
        url="https://www.kentuckysportsradio.com/?p=1234",
        datePosted = Date(),
        dateModified = null,
        title = "Kentucky wins the Gator Bowl over Penn State",
        content = "<html><body><h1>Kentucky Wins</h1><p>Kentucky wins the game and everyone celebrates.",
        description = "It was a great game, and Lynn Bowden and the Kentucky Wildcats prevailed over the Penn State Nitany Lions in a hard fought game in which Kentucky was clearly the better team.",
        author = "Drew Franklin",
        authorImage = null,
        imageUrl = null
    ),
    listOf(
        Category(1, "Football"),
        Category(2, "Main")
    )
)