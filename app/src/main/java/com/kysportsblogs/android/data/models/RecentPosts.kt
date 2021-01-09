package com.kysportsblogs.android.data.models

import com.kysportsblogs.android.ui.UiState

class RecentPosts(
    val topPosts: List<BlogPost>,
    val footballPosts: List<BlogPost>,
    val basketballPosts: List<BlogPost>,
    val otherPosts: List<BlogPost>,
    val basketballCategory: Category?,
    val footballCategory: Category?
) {
    val hasPosts: Boolean
        get() = topPosts.isNotEmpty()
            || footballPosts.isNotEmpty()
            || basketballPosts.isNotEmpty()
            || otherPosts.isNotEmpty()
}



//class RecentPosts(
//    val topPosts: UiState<List<BlogPost>>,
//    val footballPosts: UiState<List<BlogPost>>,
//    val basketballPosts: UiState<List<BlogPost>>,
//    val otherPosts: UiState<List<BlogPost>>
//) {
//    val isLoading: Boolean
//        get() = topPosts.loading
//            || footballPosts.loading
//            || basketballPosts.loading
//            || otherPosts.loading
//}