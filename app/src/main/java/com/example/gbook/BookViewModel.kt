package com.example.gbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.authentication.User
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookCategoryUiState
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


enum class BooksApiStatus { LOADING, ERROR, DONE }
enum class SearchStatus{SEARCHING , DONE}


class BookViewmodel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    //region Firebase values
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var user: User
    private var uid: String =  auth.currentUser?.uid.toString()
    //endregion

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

     val _searchStatus = MutableLiveData<SearchStatus>()
    val searchStatus: LiveData<SearchStatus> = _searchStatus

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
                publishedDate = item.volumeInfo.publishedDate.toString()
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
        _searchStatus.value = SearchStatus.SEARCHING
        viewModelScope.launch {
            try {
                val volume = booksRepository.getBooks(query!!)
                _searchResultUi.update { it.copy(books = setItemUiState(volume)) }
                _searchResultUi.value.books
                _searchStatus.value = SearchStatus.DONE

            } catch (e: Exception) {
                _searchStatus.value = SearchStatus.SEARCHING
            }
        }
    }

    //endregion



    // region Firebase
    fun addBookToReadList(search: Int = 0) {

        try {
            val x = getNumOfBookList() + 1
            if (search == 1) {
                databaseReference.child(uid).child("toReadList").child(x.toString()).setValue(
                    searchResultUi.value.books.get(books)
                )
            } else {
                databaseReference.child(uid).child("toReadList").child(x.toString())
                    .setValue(
                        bookCategoryResultUi.value.categoryList[categoryNum].books.get(
                            books
                        )
                    )
            }


            databaseReference.child(uid).child("booksNumberInList").setValue(x)

        } catch (e: Exception) {

        }
    }

    private fun getNumOfBookList(): Int {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    user = snapshot.getValue(User::class.java)!!

                    user.booksNumberInList
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
        return user.booksNumberInList
    }

    fun getBooksToRead() {
        viewModelScope.launch {
         var x = booksRepository.getBooksToRead()
            Log.e("dataSource" , "$x")
            _bookShelfResultUi.update { it ->
                it.copy(books = x.toReadList.map {
                    BookDetailsUiState(
                        title = it.title,
                        bookCover = it.bookCover,
                        description = it.description,
                        averageRating = it.averageRating,
                        pageCount = it.pageCount,
                        publishedDate = it.publishedDate)
                })

            }
        }

//        databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (item in snapshot.children) {
//                    user = snapshot.getValue(User::class.java)!!
//
//                    _bookShelfResultUi.update { it ->
//                        it.copy(books = user.toReadList.map {
//                            BookDetailsUiState(
//                                title = it.title,
//                                bookCover = it.bookCover,
//                                description = it.description,
//                                averageRating = it.averageRating,
//                                pageCount = it.pageCount,
//                                publishedDate = it.publishedDate
//                            )
//                        })
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                _status.value = BooksApiStatus.DONE
//            }
//        }
 //       )
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
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    user = snapshot.getValue(User::class.java)!!
                    try {
                        val localList = user.toReadList

                        for (item in 0..user.booksNumberInList) {
                            if (book.title == user.toReadList[item].title) {
                                localList.removeAt(item)
                                databaseReference.child(uid).child("toReadList")
                                    .removeValue()

                                for (it in 0..localList.size) {
                                    databaseReference.child(uid).child("toReadList")
                                        .child(it.toString()).setValue(
                                            localList[it]
                                        )
                                    databaseReference.child(uid).child("booksNumberInList")
                                        .setValue(localList.size - 1)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        getBooksToRead()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
    }

    //endregion

}

