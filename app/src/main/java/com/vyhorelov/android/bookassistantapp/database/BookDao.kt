package com.vyhorelov.android.bookassistantapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vyhorelov.android.bookassistantapp.Book
import java.util.UUID

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE id=(:id)")
    fun getBook(id: UUID): LiveData<Book?>

    @Query("SELECT * FROM book WHERE id=(:id)")
    fun getBookById(id: UUID): Book?

    @Query("SELECT * FROM book WHERE title=(:title)")
    fun getBookByTitle(title: String): List<Book?>

    @Query("SELECT * FROM Book WHERE isFavorite = 1")
    fun getFavoriteBooks(): List<Book>

    @Query("SELECT * FROM Book WHERE isComplete = 1")
    fun getCompleteBooks(): List<Book>

    @Update
    fun updateBook(book: Book)

    @Insert
    fun addBook(book: Book)

    @Delete
    fun delBook(book: Book)
}