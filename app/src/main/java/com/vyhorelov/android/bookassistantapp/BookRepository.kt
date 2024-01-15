package com.vyhorelov.android.bookassistantapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.vyhorelov.android.bookassistantapp.database.BookDao
import com.vyhorelov.android.bookassistantapp.database.BookDataBase
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "book-database"
class BookRepository private constructor(context: Context){

    private val database : BookDataBase = Room.databaseBuilder(
        context.applicationContext,
        BookDataBase::class.java,
        DATABASE_NAME
    ).build()
    private val bookDao : BookDao = database.BookDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getBooks(): LiveData<List<Book>> = bookDao.getBooks()
    fun getBookByTitle(title: String): List<Book?> = bookDao.getBookByTitle(title)
    fun getBookById(id: UUID): Book? = bookDao.getBookById(id)
    fun getFavoriteBooks(): List<Book> = bookDao.getFavoriteBooks()
    fun getCompleteBooks(): List<Book> = bookDao.getCompleteBooks()


    fun updateBook(book: Book){
        executor.execute{
            bookDao.updateBook(book)
        }
    }

    fun addBook(book: Book){
        executor.execute {
            bookDao.addBook(book)
        }
    }

    fun delBook(book: Book){
        executor.execute {
            bookDao.delBook(book)
        }
    }

    companion object{
        private var INSTANCE: BookRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = BookRepository(context)
            }
        }

        fun get(): BookRepository {
            return INSTANCE ?: throw IllegalStateException("BookRepository must be initialized")
        }
    }
}