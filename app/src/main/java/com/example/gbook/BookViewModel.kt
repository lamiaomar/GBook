package com.example.gbook


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookCategoryUiState
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


enum class BooksApiStatus { LOADING, ERROR, DONE }

class BookViewmodel(
    private val booksRepository: BooksRepository
) : ViewModel() {


    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()

    private val _bookCategoryResultUi = MutableStateFlow(BookCategoryUiState())
    val bookCategoryResultUi: StateFlow<BookCategoryUiState> = _bookCategoryResultUi.asStateFlow()

    private val _bookShelfResultUi = MutableStateFlow(BooksDataUiState())
    val bookShelfResultUi: StateFlow<BooksDataUiState> = _bookShelfResultUi.asStateFlow()

    //region values to display books details
    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()
    //endregion

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status

    private var categories = listOf("Biography", "Fiction", "Comic")

    //    inauthor:Ann inauthor:M inauthor:Martin

    //region values for adding book to books list
    private var categoryNum = 0
    private var books = 0
    //endregion


    init {
        getBooksDetail()
    }

    //region Network

    private fun getBooksDetail() {
        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            try {
                val data = withContext(Dispatchers.IO) {
                    val list = mutableListOf<BooksDataUiState>()
                    for (category in categories) {
                        val singleResponse = async { booksRepository.getBooks(category) }

                        list.add(BooksDataUiState(category, setItemUiState(singleResponse.await())))
                    }
                    return@withContext list
                }
                _bookCategoryResultUi.update { it.copy(categoryList = data) }

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
            }
        }
    }

    private fun setItemUiState(volume: BooksData): List<BookDetailsUiState> {

        val data = volume.items!!.map { item ->
            BookDetailsUiState(
                title = item.volumeInfo!!.title!!,
                bookCover = item.volumeInfo.imageLinks?.thumbnail!!,
                description = item.volumeInfo.description.toString(),
                averageRating = item.volumeInfo.averageRating.toString(),
                pageCount = item.volumeInfo.pageCount.toString(),
                publishedDate = item.volumeInfo.publishedDate.toString(),
                bookmarked = false
            )
        }
        return data
    }

    fun displayBookDetails(displayPosition: Int, bookTitle: String, search: Int) {

        try {
            if (search == 1) {
                val item = searchResultUi.value.books.get(displayPosition)
                setBookDetails(item)
                books = displayPosition
            } else {
                for (num in 0..3) {
                    for (x in 0..displayPosition) {
                        if (bookCategoryResultUi.value.categoryList[num].books[x].title.contains(
                                bookTitle
                            )
                        ) {
                            val item =
                                bookCategoryResultUi.value.categoryList[num].books.get(
                                    displayPosition
                                )
                            categoryNum = num
                            books = displayPosition
                            setBookDetails(item)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _status.value = BooksApiStatus.LOADING
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
        viewModelScope.launch {
            try {
                val volume = booksRepository.getBooks(query!!)
                _searchResultUi.update { it.copy(books = setItemUiState(volume)) }
                _searchResultUi.value.books

            } catch (e: Exception) {
                Log.e("get search book", "Exception $e")
            }
        }
    }

    //endregion


    // region Firebase
    fun addBookToReadList(search: Int = 0) {

        viewModelScope.launch {
            try {

                if (search == 1) {
                    booksRepository.addBookToReadList(
                        searchResultUi.value.books.get(books)
                    )

                } else {
                    booksRepository.addBookToReadList(
                        bookCategoryResultUi.value.categoryList[categoryNum]
                            .books.get(books)
                    )
                }
            } catch (e: Exception) {
                Log.e("add book exception", "Exception $e")
            }
        }
    }

    fun getBooksToRead() {
        viewModelScope.launch {
            val data = booksRepository.getBooksToRead()
            _bookShelfResultUi.update { it ->
                it.copy(books = data.toReadList.map {
                    BookDetailsUiState(
                        title = it.title,
                        bookCover = it.bookCover,
                        description = it.description,
                        averageRating = it.averageRating,
                        pageCount = it.pageCount,
                        publishedDate = it.publishedDate
                    )
                })

            }
        }
    }

    fun displayBookDetailsFromList(position: Int) {
        title.value = _bookShelfResultUi.value.books[position].title
        description.value = _bookShelfResultUi.value.books[position].description
        bookCover.value = _bookShelfResultUi.value.books[position].bookCover
        averageRating.value = _bookShelfResultUi.value.books[position].averageRating
        pageCount.value = _bookShelfResultUi.value.books[position].pageCount
        publishedDate.value = _bookShelfResultUi.value.books[position].publishedDate
    }

    fun deleteBookFromList(book: BookDetailsUiState) {
        viewModelScope.launch {
            try {
                booksRepository.deleteBookFromList(book)
            } catch (e: Exception) {
                getBooksToRead()
            }
        }
    }

  suspend  fun isBookMarked(): Boolean
  = viewModelScope.async { booksRepository.isBookMarked(
      bookCategoryResultUi.value.categoryList[categoryNum]
          .books.get(books)
  ) }.await()


/*suspend fun getNumOfBookList(): Int = viewModelScope.async {
booksRepository.getNumOfBookList()
}.await()
*/


//endregion

}

