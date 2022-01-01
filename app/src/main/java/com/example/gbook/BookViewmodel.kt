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
import kotlin.reflect.jvm.internal.ReflectProperties


enum class BooksApiStatus { LOADING, ERROR, DONE }


class BookViewmodel(
    private val booksRepository: BooksRepository

) : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String


    private val _searchResultUi = MutableStateFlow(BooksDataUiState())
    val searchResultUi: StateFlow<BooksDataUiState> = _searchResultUi.asStateFlow()

    private val _bookCategoryResultUi = MutableStateFlow(BookCategoryUiState())
    val bookCategoryResultUi: StateFlow<BookCategoryUiState> = _bookCategoryResultUi.asStateFlow()

    private val _bookShelfResultUi = MutableStateFlow(BooksDataUiState())
    val bookShelfResultUi: StateFlow<BooksDataUiState> = _bookShelfResultUi.asStateFlow()

    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status

    var category = listOf("Biography", "Music", "Art")

//    inauthor:Ann inauthor:M inauthor:Martin

    var categoryNum = 0
    var books = 0

    init {
        getBooksDetail()
    }


    fun getBooksDetail() {
        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            try {

                val data = withContext(Dispatchers.IO) {
                    val list = mutableListOf<BooksDataUiState>()
                    for (categor in category) {
                        val singlda = async { booksRepository.getBooks(categor) }

                        list.add(BooksDataUiState(categor, setItemUiState(singlda.await())))
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
/*        val id = volume.items!!.map {
//            IndustryIdentifiersItem(
//                identifier = it.volumeInfo?.industryIdentifiers!![0]!!.identifier!!
//            )
//        }
//
 */

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
//            _status.value = BooksApiStatus.LOADING
            try {
                val volume = booksRepository.getBooks(query!!)
                _searchResultUi.update { it.copy(books = setItemUiState(volume)) }
                _searchResultUi.value.books
                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.DONE
            }
        }
    }

    fun addBookToReadList() {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        try {
            val x = getNumOfBookList() + 1

            databaseReference.child(uid).child("toReadList").child(x.toString())
                .setValue(
                    bookCategoryResultUi.value.categoryList[categoryNum].books.get(
                        books
                    )
                )
            databaseReference.child(uid).child("booksNumberInList").setValue(x)

        } catch (e: Exception) {

        }
    }

    fun getNumOfBookList(): Int {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    user = snapshot.getValue(User::class.java)!!

                    user.booksNumberInList
                    Log.e("booksNumber1", "${user.booksNumberInList}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
        return user.booksNumberInList
    }


//    fun getBooksToRead() {
//
//        auth = FirebaseAuth.getInstance()
//        uid = auth.currentUser?.uid.toString()
//        databaseReference = FirebaseDatabase.getInstance().getReference("users")
//
//        try {
//            databaseReference.child(uid).child("toReadList").addValueEventListener(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (item in snapshot.children) {
//                        user = snapshot.getValue(User::class.java)!!
//
//                        _bookShelfResultUi.update {
//                            it?.copy(books = user?.toReadList?.map {
//                                    BookDetailsUiState(
//                                        title = it?.title,
//                                        bookCover = it?.bookCover,
//                                        description = it?.description,
//                                        averageRating = it?.averageRating,
//                                        pageCount = it?.pageCount,
//                                        publishedDate = it?.publishedDate
//                                    )!!
//                                })
//                        }
//                        Log.e("data", "${_bookShelfResultUi.value}")
////                    val data = user?.toReadList?.map {
////                        BookDetailsUiState(
////                            title = it?.title!!,
////                            bookCover = it.bookCover,
////                            description = it.description,
////                            averageRating = it.averageRating,
////                            pageCount = it.pageCount,
////                            publishedDate = it.publishedDate)
////                    }
////                    Log.e("data" ,"$data")
////
////                    _bookShelfResultUi.update {
////                        it.copy(books = data)
////                    }
////                    _bookShelfResultUi.value.books
////
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            }
//
//
//            )
//        } catch (e: Exception) {
//            TODO("")
//        }
//    }

}

