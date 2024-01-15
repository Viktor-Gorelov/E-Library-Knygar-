package com.vyhorelov.android.bookassistantapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

private const val ARG_BOOK_ID = "book_id"
private const val TAG = "BookDetailInfoFragment"

class BookDetailInfoFragment: Fragment(){

    private lateinit var book: Book
    private lateinit var detailInfoTitleBook: TextView
    private lateinit var detailInfoGenreBook: TextView
    private lateinit var detailInfoAuthorBook: TextView
    private lateinit var detailInfoDescriptionBook: TextView
    private lateinit var detailInfoReviewBook: EditText
    private lateinit var detailInfoAddReviewBook: Button
    private lateinit var detailInfoCheckFavoriteBook: CheckBox
    private lateinit var detailInfoCheckCompleteBook: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail_info, container, false)
        detailInfoTitleBook = view.findViewById(R.id.detail_info_title_book) as TextView
        detailInfoGenreBook = view.findViewById(R.id.detail_info_genre_book) as TextView
        detailInfoAuthorBook = view.findViewById(R.id.detail_info_author_book) as TextView
        detailInfoDescriptionBook = view.findViewById(R.id.detail_info_description_book) as TextView
        detailInfoReviewBook = view.findViewById(R.id.detail_info_review_book) as EditText
        detailInfoAddReviewBook = view.findViewById(R.id.detail_info_add_review_book) as Button
        detailInfoCheckFavoriteBook = view.findViewById(R.id.detail_info_check_favorite_book) as CheckBox
        detailInfoCheckCompleteBook = view.findViewById(R.id.detail_info_check_complete_book) as CheckBox
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        book = Book()
        val bookId: UUID = arguments?.getSerializable(ARG_BOOK_ID) as UUID
        GlobalScope.launch(Dispatchers.IO) {
            val book: Book? = BookRepository.get().getBookById(bookId)
            detailInfoTitleBook.setText("${getString(R.string.detail_info_title_book)} ${book?.title}")
            detailInfoGenreBook.setText("${getString(R.string.detail_info_genre_book)} ${book?.genre}")
            detailInfoAuthorBook.setText("${getString(R.string.detail_info_author_book)} ${book?.author}")
            detailInfoDescriptionBook.setText("${getString(R.string.detail_info_description_book)} ${book?.description}")

            requireActivity().runOnUiThread{
                detailInfoReviewBook.setText(book?.review)
            }

            detailInfoAddReviewBook.setOnClickListener{view: View->
                if(book != null){
                    book.review = detailInfoReviewBook.text.toString()
                    BookRepository.get().updateBook(book)
                }
            }

            detailInfoCheckFavoriteBook.apply {
                setOnCheckedChangeListener{_, isChecked ->
                    book?.isFavorite = isChecked
                    if (book != null) {
                        BookRepository.get().updateBook(book)
                    }
                }
            }

            detailInfoCheckFavoriteBook.apply {
                if (book != null) {
                    isChecked = book.isFavorite
                }
                jumpDrawablesToCurrentState()
            }

            detailInfoCheckCompleteBook.apply {
                setOnCheckedChangeListener{_, isChecked ->
                    book?.isComplete = isChecked
                    if (book != null) {
                        BookRepository.get().updateBook(book)
                    }
                }
            }

            detailInfoCheckCompleteBook.apply {
                if (book != null) {
                    isChecked = book.isComplete
                }
                jumpDrawablesToCurrentState()
            }

            Log.i(TAG, "Got books ${book?.id}")
            Log.i(TAG, "Got books ${book?.title}")
            Log.i(TAG, "Got books ${book?.author}")
            Log.i(TAG, "Got books ${book?.description}")
            Log.i(TAG, "Got books ${book?.isFavorite}")
            Log.i(TAG, "Got books ${book?.isComplete}")
        }
    }

    companion object {
        fun newInstance(bookId: UUID): BookDetailInfoFragment{
            val args = Bundle().apply {
                putSerializable(ARG_BOOK_ID, bookId)
            }
            return BookDetailInfoFragment().apply {
                arguments = args
            }
        }
    }
}