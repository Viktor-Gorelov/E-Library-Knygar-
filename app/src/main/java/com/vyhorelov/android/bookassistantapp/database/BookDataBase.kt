package com.vyhorelov.android.bookassistantapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vyhorelov.android.bookassistantapp.Book

@Database(entities = [ Book::class ], version = 1)

@TypeConverters(BookTypeConverters::class)

abstract class BookDataBase: RoomDatabase() {
   abstract fun BookDao(): BookDao
}