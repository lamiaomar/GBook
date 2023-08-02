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
import com.example.gbook.enums.BooksApiStatus
import com.example.gbook.ui.BookCategoryUiState
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import com.example.gbook.ui.UserUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class BookViewmodel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var auth = FirebaseAuth.getInstance()

    //region StateFlow
    // * stream flow for Search list
    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()

    // * stream flow for Books list
    private val _bookCategoryResultUi = MutableStateFlow(BookCategoryUiState())
    val bookCategoryResultUi: StateFlow<BookCategoryUiState> = _bookCategoryResultUi.asStateFlow()

    // * stream flow for User Information
    private val _userResultUi = MutableStateFlow(UserUiState())
    val userResultUi: StateFlow<UserUiState> = _userResultUi.asStateFlow()
    //endregion

    //region values to display books details
    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()
    //endregion

    //region UI state
    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status
    //endregion

    // region Categories Lists
    private var categories = listOf("Biography", "Cinema", "Comic")
    // ? example in case to search by author
    // ? (inauthor:Ann inauthor:M inauthor:Martin)
    // endregion

    //region values for adding book to user books list
    private var categoryNum = 0
    private var books = 0
    //endregion


    init {
        getBooksDetail()
    }

    //region Functions related to Google API database

    // * Get all books from google API network
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

    // * Set the object from the network to BookDetailsUiState
    // * and return list of Books UI State
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

    // * To display book details
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

    // * Set the details to values as live data
    private fun setBookDetails(details: BookDetailsUiState?) {
        title.value = details?.title
        bookCover.value = details?.bookCover

        if (details!!.description == "null") {
            description.value = "No Description"
        } else {
            description.value = details.description
        }
        if (details.averageRating == "null") {
            averageRating.value = "No rate"
        } else {
            averageRating.value = details.averageRating
        }
        if (details.pageCount == "null") {
            pageCount.value = "No num"
        } else {
            pageCount.value = details.pageCount
        }
        if (details.publishedDate == "null") {
            publishedDate.value = "No date"
        } else {
            publishedDate.value = details.publishedDate
        }


    }

    // * To get the book that user search for
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


    // region Functions related to Firebase Realtime database

    // * Add book to user to-read list in the database
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

    // * Get all books in to-read list
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

    // * User registration by passing password and all user information
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

    // * Get user data and set to userResultUi
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

