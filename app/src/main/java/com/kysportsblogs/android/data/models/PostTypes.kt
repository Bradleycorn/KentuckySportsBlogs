package com.kysportsblogs.android.data.models

enum class PostType(val displayName: String, val recentCount: Int) {
    BASKETBALL("Basketball", 10),
    FOOTBALL("Football", 10),
    TOP_STORIES("Top Stories", 5),
    OTHER("More Stories", -1);
}
