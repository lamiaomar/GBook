package com.example.gbook


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookCategoryUiState
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import com.example.gbook.ui.UserUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


enum class BooksApiStatus { LOADING, ERROR, DONE }

class BookViewmodel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var auth = FirebaseAuth.getInstance()

    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()


    private val _bookCategoryResultUi = MutableStateFlow(BookCategoryUiState())
    val bookCategoryResultUi: StateFlow<BookCategoryUiState> = _bookCategoryResultUi.asStateFlow()


    private val _userResultUi = MutableStateFlow(UserUiState())
    val userResultUi: StateFlow<UserUiState> = _userResultUi.asStateFlow()


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
            _userResultUi.update { it ->
                it.copy(toReadList = data.toReadList.map {
                    BookDetailsUiState(
                        title = it.title,
                        bookCover = it.bookCover,
                        description = it.description,
                        averageRating = it.averageRating,
                        pageCount = it.pageCount,
                        publishedDate = it.publishedDate
                    )
                } as MutableList<BookDetailsUiState>)
            }
        }
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


    fun editUserProfile(userEdit: User) {
        viewModelScope.launch {
            booksRepository.editUserProfile(userEdit)
        }
    }


    var operationState = false


    fun signIn(newUser: User, password: String) {
        viewModelScope.launch {
            booksRepository.signIn(newUser, password)
            operationState = true
            sendEmailVerification()

        }

    }


    private fun sendEmailVerification() {
        FirebaseUtils.firebaseUser?.sendEmailVerification()
    }


    fun signInUser(signInEmail: String, signInPassword: String) {
        viewModelScope.launch {
            booksRepository.signInUser(signInEmail, signInPassword)
        }
    }


    fun onSuccesses(): Boolean {
        return operationState
    }


    fun getUserData() {
        viewModelScope.launch {
            val user = booksRepository.getBooksToRead()
            _userResultUi.update {
                it.copy(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    day = user.day,
                    month = user.month,
                    year = user.year,
                    email = user.email,
                    gender = user.gender,
                    booksNumberInList = user.booksNumberInList,
                    booksChallenge = user.booksChallenge,
                    maxBooksChallenge = user.maxBooksChallenge
                )
            }
        }
    }


    //endregion

}

