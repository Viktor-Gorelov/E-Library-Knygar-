package com.vyhorelov.android.bookassistantapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "BookListFragment"
class BooksScreenFragment: Fragment() {

    private var callbacks: Callbacks? = null
    private lateinit var addBooksButton: Button
    private lateinit var sortFavoriteButton: ImageButton
    private lateinit var sortCompleteButton: ImageButton
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var getFavoriteBook:List<Book>
    private lateinit var getCompleteBook:List<Book>
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    interface Callbacks{
        fun addingBookScreenChoosen()
        fun onBookSelected(bookId: UUID)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    private val booksScreenViewModel: BooksScreenViewModel by lazy {
        ViewModelProviders.of(this).get(BooksScreenViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_books_screen, container, false)

        booksRecyclerView = view.findViewById(R.id.books_recycler_view) as RecyclerView
        addBooksButton = view.findViewById(R.id.add_book_btn) as Button
        sortFavoriteButton = view.findViewById(R.id.sort_favorite_btn) as ImageButton
        sortCompleteButton = view.findViewById(R.id.sort_complete_btn) as ImageButton
        booksRecyclerView.layoutManager = LinearLayoutManager(context)
        booksRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksScreenViewModel.bookLiveData.observe(
            viewLifecycleOwner,
            Observer { books ->
                books?.let {
                    Log.i(TAG, "Got books ${books.size}")
                    updateUI(books)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        addBooksButton.setOnClickListener{view: View ->
            Log.i(TAG, "Add books")
            callbacks?.addingBookScreenChoosen()
        }

        sortFavoriteButton.setOnClickListener{view: View ->
            GlobalScope.launch(Dispatchers.IO) {
                getFavoriteBook = BookRepository.get().getFavoriteBooks()
                activity?.runOnUiThread {
                    adapter?.updateBooks(getFavoriteBook)
                    updateUI(getFavoriteBook)
                }
            }
        }

        sortCompleteButton.setOnClickListener{view: View ->
            GlobalScope.launch(Dispatchers.IO) {
                getCompleteBook = BookRepository.get().getCompleteBooks()
                activity?.runOnUiThread {
                    adapter?.updateBooks(getCompleteBook)
                    updateUI(getCompleteBook)
                }
            }
        }
    }

    private fun updateUI(books: List<Book>){
        adapter = BookAdapter(books)
        booksRecyclerView.adapter = adapter
    }

    private inner class BookHolder(view:View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var book: Book
        private val titleTextView: TextView = itemView.findViewById(R.id.book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.book_author)
        private var delButton: ImageButton = view.findViewById(R.id.del_btn)
        private var bookFavorite: ImageView = view.findViewById(R.id.book_favorite)
        private var bookComplete: ImageView = view.findViewById(R.id.book_complete)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(book: Book){
            this.book = book
            titleTextView.text = this.book.title
            authorTextView.text = this.book.author
            delButton.setOnClickListener{view: View ->
                booksScreenViewModel.delBook(book)
                Log.i("Del Btn PRESSED", "PRESSED ${book.id}")
            }
            bookFavorite.visibility = if (book.isFavorite){
                View.VISIBLE
            } else {
                View.GONE
            }
            bookComplete.visibility = if (book.isComplete){
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {
            callbacks?.onBookSelected(book.id)
        }
    }

    private inner class BookAdapter(var books: List<Book>) : RecyclerView.Adapter<BookHolder>(){

        override fun getItemViewType (position: Int) : Int{
            return 0
        }

        fun updateBooks(books: List<Book>) {
            this.books = books
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val view: View = layoutInflater.inflate(R.layout.list_item_book, parent, false) // list item layout
            return BookHolder(view)
        }

        override fun getItemCount() = books.size

        override fun onBindViewHolder(holder: BookHolder, position: Int) {
            val book = books[position]
            holder.bind(book)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): BooksScreenFragment{
            return BooksScreenFragment()
        }
    }
}