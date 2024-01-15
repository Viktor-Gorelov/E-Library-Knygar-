package com.vyhorelov.android.bookassistantapp

import androidx.lifecycle.ViewModel

class BooksScreenViewModel: ViewModel() {
    private val bookRepository = BookRepository.get()
    val bookLiveData = bookRepository.getBooks()

    fun delBook(book: Book) {
        bookRepository.delBook(book)
    }

}