package com.vyhorelov.android.bookassistantapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Book (
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var title : String = "",
    var genre : String = "",
    var author: String= "",
    var description: String = "",
    var review: String = "",
    var isFavorite: Boolean = false,
    var isComplete: Boolean = false
)