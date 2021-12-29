package com.example.gbook

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.authentication.BookList
import com.example.gbook.authentication.User
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookItemUiState
import com.example.gbook.ui.BooksDataUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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


    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status

//     var itemUiState: BookItemUiState  =



    var qApi1 = "inauthor:Ann inauthor:M inauthor:Martin"
    var qApi2 = "classic"


    init {
        getBook()
    }

    private fun getBook() {
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {

                val volume = booksRepository.getBooks(qApi1)
                _firstResultUi.update { it.copy(volume = setItemUiState(volume)) }

                val secVolume = booksRepository.getBooks(qApi2)
//                    BooksApi.retrofitService.getBook(qApi2)
                _secondResultUi.update { it.copy(volume = setItemUiState(secVolume)) }

                val thirdVolume = booksRepository.getBooks("kids")
                _thirdResultUi.update { it.copy(volume = setItemUiState(thirdVolume)) }

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR

            }

        }
    }

    fun displayBookDetails(displayPosition: Int, listNum: Int) {

        try {
            if (listNum == 1) {

                val item = firstResultUi.value.volume.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 2) {

                val item = secondResultUi.value.volume.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 3) {

                val item = thirdResultUi.value.volume.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 4) {

                val item = searchResultUi.value.volume.get(displayPosition)
                setBookDetails(item)

            }

        } catch (e: Exception) {
            _status.value = BooksApiStatus.ERROR
        }

    }

    private fun setBookDetails(item: BookItemUiState?) {
        title.value = item?.title
        bookCover.value = item?.bookCover
        description.value = item?.description
        averageRating.value = item?.averageRating
        pageCount.value = item?.pageCount
        publishedDate.value = item?.publishedDate

    }

    fun getSearchBook(query: String?) {

        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {
                val volume = booksRepository.getBooks(query!!)
                _searchResultUi.update { it.copy(volume = setItemUiState(volume)) }

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
            }
        }
    }

    private fun setItemUiState(volume: BooksData): List<BookItemUiState> {
        val data = volume.items!!.map { item ->
            BookItemUiState(
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
