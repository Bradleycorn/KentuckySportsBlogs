package com.kysportsblogs.android.data.network.responseModels

import androidx.navigation.Navigator
import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
@NamespaceList(
    Namespace(reference = "http://purl.org/dc/elements/1.1/", prefix = "dc"),
    Namespace(reference = "http://purl.org/rss/1.0/modules/content/", prefix = "content"),
    Namespace(reference = "http://wellformedweb.org/CommentAPI/", prefix = "wfw"),
    Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
    Namespace(reference = "http://purl.org/rss/1.0/modules/syndication/", prefix = "sy"),
    Namespace(reference = "http://purl.org/rss/1.0/modules/slash/", prefix = "slash")
)
data class RSSFeed(
    @field:Element(name = "channel")
    var channel: RssChannel? = null
)

@Root(name= "channel", strict = false)
data class RssChannel(
    @field:Element(name="title")
    var title: String = "",

    @field:ElementList(
        name = "item",
        inline = true,
        required = false
    ) var items: List<RssItem>? = null
)

@Root(name="item", strict = false)
data class RssItem(
    @field:Element(name = "title") var title: String = "",
    @field:Element(name = "description") var description: String = "",
    @field:Element(name = "link") var link: String = "",

    @field:Element(name="creator", data = true)
    @Namespace(reference = "http://purl.org/dc/elements/1.1/")
    var creator: String = "",

    @field:Element(name="pubDate")
    var pubDate: String = "",

    @field:ElementList(name="category", inline = true, required = false, data = true)
    var categories: List<String> = listOf(),

    @field:Element(name="guid")
    var guid: String = "",

    @field:Element(name = "encoded", data = true)
    @Namespace(reference = "http://purl.org/rss/1.0/modules/content/")
    var content: String = ""
) {
    companion object {
        val PUB_DATE_FORMAT = "EEE, dd MMM yyyy kk:mm:ss Z" //Tue, 17 Nov 2020 00:00:50 +0000

    }
}