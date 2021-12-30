package com.example.gbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.authentication.User
import com.example.gbook.data.Book
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookCategoryUiState
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class BooksApiStatus { LOADING, ERROR, DONE }

class BookViewmodel(

    private val booksRepository: BooksRepository

) : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String


    private val _firstResultUi = MutableStateFlow(BooksDataUiState())
    val firstResultUi: StateFlow<BooksDataUiState> = _firstResultUi.asStateFlow()

    private val _secondResultUi = MutableStateFlow(BooksDataUiState())
    val secondResultUi: StateFlow<BooksDataUiState> = _secondResultUi.asStateFlow()

    private val _thirdResultUi = MutableStateFlow(BooksDataUiState())
    val thirdResultUi: StateFlow<BooksDataUiState> = _thirdResultUi.asStateFlow()

    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()


    private val _bookCategoryResultUi = MutableStateFlow(BookCategoryUiState())
    val bookCategoryResultUi: StateFlow<BookCategoryUiState> = _bookCategoryResultUi.asStateFlow()


    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status


    var categorcy1 = listOf("inauthor:Ann inauthor:M inauthor:Martin", "classic", "kids")

    init {
        getBooksDetail()
        //   getBook()
        // getBookData()
    }

//    fun getBookData(): BookCategoryUiState {
//        var x = listOf<MutableStateFlow<BookDetailsUiState>>(
//            getBooksDetail(category1),
//            getBooksDetail(category2),
//            getBooksDetail(category3)
//        )
//        return BookCategoryUiState(x)
//    }


    fun getBooksDetail() {
        viewModelScope.launch {
            try {

                //  val volume =

                var data = withContext(Dispatchers.IO) {
                    var list = mutableListOf<BooksDataUiState>()
                    for (categor in categorcy1) {
                        var singlda = async { booksRepository.getBooks(categor) }


                        list.add(BooksDataUiState(categor, setItemUiState(singlda.await())))
                    }
                    return@withContext list

                }

                Log.d("TAG", "getBooksDetail: ${data.toString()}")
                _bookCategoryResultUi.update { it.copy(categoryList = data) }


                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
            }
        }
        //return "_bookDetailResultUi"
    }

    private fun setItemUiState(volume: BooksData): List<BookDetailsUiState> {
        val data = volume.items!!.map { item ->
            BookDetailsUiState(
                title = item.volumeInfo!!.title!!,
                bookCover = item.volumeInfo.imageLinks?.thumbnail!!,
                description = item.volumeInfo.description.toString(),
                averageRating = item.volumeInfo.averageRating.toString(),
                pageCount = item.volumeInfo.pageCount.toString(),
                publishedDate = item.volumeInfo.publishedDate.toString()
            )
        }
        return data
    }

//    private fun getBook() {
//        _status.value = BooksApiStatus.LOADING
//        viewModelScope.launch {
//            try {
//
//                val volume = booksRepository.getBooks(category1)
//                _firstResultUi.update { it.copy(books = setItemUiState(volume)) }
//
//                val secVolume = booksRepository.getBooks(category2)
////                    BooksApi.retrofitService.getBook(qApi2)
//                _secondResultUi.update { it.copy(books = setItemUiState(secVolume)) }
//
//                val thirdVolume = booksRepository.getBooks(category3)
//                _thirdResultUi.update { it.copy(books = setItemUiState(thirdVolume)) }
//
//                _status.value = BooksApiStatus.DONE
//
//            } catch (e: Exception) {
//                _status.value = BooksApiStatus.ERROR
//
//            }
//
//        }
//    }

    fun displayBookDetails(displayPosition: Int, listNum: Int) {

        try {
            if (listNum == 1) {

                val item = firstResultUi.value.books.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 2) {

                val item = secondResultUi.value.books.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 3) {

                val item = thirdResultUi.value.books.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 4) {

                val item = searchResultUi.value.books.get(displayPosition)
                setBookDetails(item)

            }

        } catch (e: Exception) {
            _status.value = BooksApiStatus.ERROR
        }

    }

    private fun setBookDetails(details: BookDetailsUiState?) {
        title.value = details?.title
        bookCover.value = details?.bookCover
        description.value = details?.description
        averageRating.value = details?.averageRating
        pageCount.value = details?.pageCount
        publishedDate.value = details?.publishedDate

    }

    fun getSearchBook(query: String?) {

        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {
                val volume = booksRepository.getBooks(query!!)
                _searchResultUi.update { it.copy(books = setItemUiState(volume)) }

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
            }
        }
    }

    fun addBookToReadList(displayPosition: Int, listNum: Int) {

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        try {
            databaseReference.child(uid).child("userLists").push().setValue({

            })
        } catch (e: Exception) {

        }
    }
}
