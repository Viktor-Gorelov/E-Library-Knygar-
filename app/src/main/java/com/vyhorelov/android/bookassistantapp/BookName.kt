package com.vyhorelov.android.bookassistantapp

data class BookName (
    val id: String = "",
    val title: String = "",
    val genre: String = "",
    val author: String = "",
    val description: String = "",
    val review: String = "",
    var isFavorite: Boolean = false,
    var isComplete: Boolean = false
)