package com.example.gbook

//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.gbook.authentication.User
//import com.example.gbook.authentication.utils.FirebaseUtils
//import com.example.gbook.data.BooksRepository
//import com.example.gbook.ui.BookDetailsUiState
//import com.example.gbook.ui.BooksDataUiState
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import kotlinx.android.synthetic.main.fragment_log_in.*
//import kotlinx.android.synthetic.main.fragment_registration.*
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//
//class UserViewModel(
//    private val booksRepository: BooksRepository
//) : ViewModel() {
//
//    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
//    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()
//
//    var operationState = false
//
//    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private lateinit var databaseReference: DatabaseReference
//    var currentUser = User()
//
//
//    // region Functions related to Firebase Realtime database
//
//    // * Add book to user to-read list in the database
//    fun addBookToReadList(search: Int = 0) {
//        viewModelScope.launch {
//            try {
//                if (search == 1) {
//                    booksRepository.addBookToReadList(
//                        searchResultUi.value.books.get(books)
//                    )
//
//                } else {
//                    booksRepository.addBookToReadList(
//                        bookCategoryResultUi.value.categoryList[categoryNum]
//                            .books.get(books)
//                    )
//                }
//            } catch (e: Exception) {
//                Log.e("add book exception", "Exception $e")
//            }
//        }
//    }
//
//    // * Get all books in to-read list
//    fun getBooksToRead() {
//        viewModelScope.launch {
//            val data = booksRepository.getBooksToRead()
//            _userResultUi.update { it ->
//                it.copy(toReadList = data.toReadList.map {
//                    BookDetailsUiState(
//                        title = it.title,
//                        bookCover = it.bookCover,
//                        description = it.description,
//                        averageRating = it.averageRating,
//                        pageCount = it.pageCount,
//                        publishedDate = it.publishedDate
//                    )
//                } as MutableList<BookDetailsUiState>)
//            }
//        }
//    }
//
//    fun deleteBookFromList(book: BookDetailsUiState) {
//        viewModelScope.launch {
//            try {
//                booksRepository.deleteBookFromList(book)
//            } catch (e: Exception) {
//                getBooksToRead()
//            }
//        }
//    }
//
//    fun editUserProfile(userEdit: User) {
//        viewModelScope.launch {
//            booksRepository.editUserProfile(userEdit)
//        }
//    }
//
//
//    var operationState = false
//
//    // * User registration by passing password and all user information
//    fun signIn(newUser: User, password: String) {
//        viewModelScope.launch {
//            booksRepository.signIn(newUser, password)
//            operationState = true
//            sendEmailVerification()
//
//        }
//
//    }
//
//
//    private fun sendEmailVerification() {
//        FirebaseUtils.firebaseUser?.sendEmailVerification()
//    }
//
//
//    fun signInUser(signInEmail: String, signInPassword: String) {
//        viewModelScope.launch {
//            booksRepository.signInUser(signInEmail, signInPassword)
//        }
//    }
//
//
//    fun onSuccesses(): Boolean {
//        return operationState
//    }
//
//    // * Get user data and set to userResultUi
//    fun getUserData() {
//        viewModelScope.launch {
//            val user = booksRepository.getBooksToRead()
//            _userResultUi.update {
//                it.copy(
//                    firstName = user.firstName,
//                    lastName = user.lastName,
//                    day = user.day,
//                    month = user.month,
//                    year = user.year,
//                    email = user.email,
//                    gender = user.gender,
//                    booksNumberInList = user.booksNumberInList,
//                    booksChallenge = user.booksChallenge,
//                    maxBooksChallenge = user.maxBooksChallenge
//                )
//            }
//        }
//    }
//
//    //endregion
//}
//
//
//
//
