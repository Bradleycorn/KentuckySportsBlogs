package com.kysportsblogs.android.data.network.responseModels

import com.google.gson.annotations.SerializedName
import com.kysportsblogs.android.util.extensions.toDate
import java.util.*

data class WordpressPost(
    val id: Long,
    val date_gmt: String,
    val modified_gmt: String,
    val status: String,
    val type: String,
    val link: String,
    val title: WpRendered,
    val content: WpRendered,
    val excerpt: WpRendered,
    val author: Long,
    val featured_media: Long,
    @SerializedName("_embedded")
    val embedded: WpEmbedded

) {

    companion object {
        val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    }

    val postDate: Date?
        get() = date_gmt.toDate(DATE_FORMAT, true)

    val modifiedDate: Date?
        get() = modified_gmt.toDate(DATE_FORMAT, true)
}

data class WpEmbedded(
    val author: List<WpAuthor>?,

    @SerializedName("wp:featuredmedia")
    val media: List<WpMedia>?,

    @SerializedName("wp:term")
    val terms: List<List<WpTerm>>?
)

data class WpRendered(val rendered: String)

data class WpAuthor(
    val id: Long,
    val name: String,
    val avatar_urls: WpIconList?,

)

data class WpMedia(
    val id: Long,
    val type: String,
    val media_type: String,
    val mime_type: String,
    val media_details: WpMediaDetails
)

data class WpIconList(
    @SerializedName("24")
    val small: String?,

    @SerializedName("48")
    val medium: String?,

    @SerializedName("96")
    val large: String?
)

data class WpMediaDetails(
    val width: Int,
    val height: Int,
    val sizes: WpImageList
)

data class WpImageSize(
    val file: String,
    val width: Int,
    val height: Int,
    val mime_type: String,
    val source_url: String
)

data class WpImageList(
    val medium: WpImageSize?,
    val large: WpImageSize?,
    val thumbnail: WpImageSize?,
    val medium_large: WpImageSize?,
    val full: WpImageSize?,

    @SerializedName("menu-24x24")
    val square24: WpImageSize?,

    @SerializedName("menu-36x36")
    val square36: WpImageSize?,

    @SerializedName("menu-48x48")
    val square48: WpImageSize?
)

data class WpTerm(
    val id: Long,
    val link: String,
    val name: String,
    val slug: String,
    val taxonomy: String
)

