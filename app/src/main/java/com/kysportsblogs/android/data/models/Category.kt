package com.kysportsblogs.android.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kysportsblogs.android.data.network.responseModels.WpTerm

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey
    val categoryId: Long,
    val name: String
) {

    companion object {
        fun fromWpTerms(terms: List<List<WpTerm>>?): List<Category> {
            return terms?.flatten()?.mapNotNull { fromWpTerm(it) } ?: listOf()
        }

        fun fromWpTerm(term: WpTerm): Category? {
            return when (term.taxonomy) {
                "category" -> Category(term.id, term.name)
                else -> null
            }
        }
    }
}


fun List<Category>.contains(name: String): Boolean = (find { it.name == name } != null)
