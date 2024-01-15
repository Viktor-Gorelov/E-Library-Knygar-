package com.vyhorelov.android.bookassistantapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID


class AddBookFragment: Fragment() {
    private var callbacks: MainMenuFragment.Callbacks? = null
    private lateinit var addBookTitle: AutoCompleteTextView
    private lateinit var addBookToLibrary: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainMenuFragment.Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_book, container, false)
        addBookTitle = view.findViewById(R.id.add_book_title) as AutoCompleteTextView
        addBookToLibrary = view.findViewById(R.id.add_book_to_library) as Button
        return view
    }

    override fun onStart() {
        super.onStart()
        val listOfBooks = callbacks!!.readCSVFIle()

        val booksNamesArray = Array(listOfBooks.size) { "" }

        for (i in 0 until listOfBooks.size) {
            booksNamesArray[i] = listOfBooks[i].title
        }

        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, booksNamesArray)
        addBookTitle.setAdapter(arrayAdapter)

        addBookToLibrary.setOnClickListener{view: View ->
            val book: Book = Book(
                UUID.randomUUID(),
                addBookTitle.text.toString(),
                "",
                "",
                "",
                "",
                false,
                false
            )

            for(i in 0 until listOfBooks.size) {
                if(book.title == listOfBooks[i].title) {
                    book.genre = listOfBooks[i].genre
                    book.author = listOfBooks[i].author
                    book.description = listOfBooks[i].description
                    book.review = listOfBooks[i].review
                    book.isFavorite = listOfBooks[i].isFavorite
                    book.isComplete = listOfBooks[i].isComplete

                    val bookRepository = BookRepository.get()
                    val handler = Handler(Looper.getMainLooper())

                    GlobalScope.launch(Dispatchers.IO) {
                        val items = bookRepository.getBookByTitle(book.title)

                        if(items.isNotEmpty()) {
                            handler.post {
                                Toast.makeText(requireContext(), R.string.copy_book, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            bookRepository.addBook(book)
                            callbacks?.booksScreenChosen()
                        }
                    }
                }
            }
            if(book.author == ""){
                Toast.makeText(requireContext(), R.string.no_book, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): AddBookFragment{
            return AddBookFragment()
        }
    }
}