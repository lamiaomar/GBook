package com.example.gbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.data.BooksRepository
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksDataUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class UserViewModel(
    private val usersRepository: BooksRepository
) : ViewModel() {

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status

    private val _bookShelfResultUi = MutableStateFlow(BooksDataUiState())
    val bookShelfResultUi : StateFlow<BooksDataUiState> = _bookShelfResultUi.asStateFlow()


    init {
        getBooksToRead()
    }


    fun getBooksToRead() {
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {
                val volume = usersRepository.getBooksToRead()
                Log.e("shelf" , "$volume")

                _bookShelfResultUi.update {
                    it.copy(
                        books = volume.toReadList.map {
                            BookDetailsUiState(
                                title = it.title,
                                bookCover = it.bookCover,
                                description = it.description,
                                averageRating = it.averageRating,
                                pageCount = it.pageCount,
                                publishedDate = it.publishedDate
                            )
                        }
                    )
                }
                _bookShelfResultUi.value.books
                _status.value = BooksApiStatus.DONE

                Log.e("shelf1" , "${_bookShelfResultUi.value.books}")

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
            }
        }

    }
}