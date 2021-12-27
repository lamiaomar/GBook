package com.example.gbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.data.ItemsItem
import com.example.gbook.network.BooksApi
import com.example.gbook.ui.BookItemUiState
import com.example.gbook.ui.BooksDataUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BooksApiStatus { LOADING, ERROR, DONE }


class BookViewmodel : ViewModel() {


    private val _firstResultUi = MutableStateFlow(BooksDataUiState())
    val firstResultUi: StateFlow<BooksDataUiState> = _firstResultUi.asStateFlow()

    private val _secondResultUi = MutableStateFlow(BooksDataUiState())
    val secondResultUi: StateFlow<BooksDataUiState> = _secondResultUi.asStateFlow()

    private val _thirdResultUi = MutableStateFlow(BooksDataUiState())
    val thirdResultUi: StateFlow<BooksDataUiState> = _thirdResultUi.asStateFlow()

    var result = MutableLiveData<List<ItemsItem?>>()
    var secresult = MutableLiveData<List<ItemsItem?>>()
    var thieresult = MutableLiveData<List<ItemsItem?>>()
    var searchResult = MutableLiveData<List<ItemsItem?>>()

    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()
    var pageCount = MutableLiveData<String?>()
    var publishedDate = MutableLiveData<String?>()

    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status


    var qApi1 = "inauthor:Ann inauthor:M inauthor:Martin"
    var qApi2 = "classic"


    init {
        getBook()
    }


    private fun getBook() {
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {

                //  result.value = BooksApi.retrofitService.getBook(qApi1).items

                val volume = BooksApi.retrofitService.getBook(qApi1)
                val data = volume.items!!.map { item ->
                    BookItemUiState(
                        title = item.volumeInfo!!.title!!,
                        bookCover = item.volumeInfo.imageLinks?.thumbnail!!,
                        description = item.volumeInfo.description.toString(),
                        averageRating = item.volumeInfo.averageRating.toString(),
                        pageCount = item.volumeInfo.pageCount.toString(),
                        publishedDate = item.volumeInfo.publishedDate!!
                    )
                }
                _firstResultUi.update { it.copy(volume = data) }



//                secresult.value = BooksApi.retrofitService.getBook(qApi2).items
                val secVolume = BooksApi.retrofitService.getBook(qApi2)
                val secData = secVolume.items!!.map { item ->
                    BookItemUiState(
                        title = item.volumeInfo!!.title!!,
                        bookCover = item.volumeInfo.imageLinks?.thumbnail!!,
                        description = item.volumeInfo.description.toString(),
                        averageRating = item.volumeInfo.averageRating.toString(),
                        pageCount = item.volumeInfo.pageCount.toString(),
                        publishedDate = item.volumeInfo.publishedDate.toString()
                    )
                }
                _secondResultUi.update { it.copy(volume = secData) }

//                thieresult.value = BooksApi.retrofitService.getBook("kids").items
                val thirdVolume = BooksApi.retrofitService.getBook("kids")
                val thirData = thirdVolume.items!!.map { item ->
                    BookItemUiState(
                        title = item.volumeInfo!!.title!!,
                        bookCover = item.volumeInfo.imageLinks?.thumbnail!!,
                        description = item.volumeInfo.description.toString(),
                        averageRating = item.volumeInfo.averageRating.toString(),
                        pageCount = item.volumeInfo.pageCount.toString(),
                        publishedDate = item.volumeInfo.publishedDate.toString()
                    )
                }
                _thirdResultUi.update { it.copy(volume = thirData) }

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
                result.value = listOf()
                secresult.value = listOf()
                thieresult.value = listOf()
            }

            //"inauthor:steve inauthor:jobs"

        }
    }

    fun displayBookDetails(displayPosition: Int, listNum: Int) {
        Log.e("display", "${displayPosition}")
        Log.e("display", "${listNum}")

        try {
            if (listNum == 1) {
                val item = result.value?.get(displayPosition)
                Log.e("link", "$item")
                setBookDetails(item)

            } else if (listNum == 2) {
                val item = secresult.value?.get(displayPosition)
                setBookDetails(item)
                Log.e("link", "$item")

            } else if (listNum == 3) {
                val item = thieresult.value?.get(displayPosition)
                setBookDetails(item)
                Log.e("link", "$item")


            } else if (listNum == 4) {
                val item = searchResult.value?.get(displayPosition)
                Log.e("display", "${item}")
                setBookDetails(item)
                Log.e("link", "$item")

            }

        } catch (e: Exception) {
            title.value = "${e.message}"
        }

    }

    fun setBookDetails(item: ItemsItem?) {
        title.value = item?.volumeInfo?.title
        bookCover.value = item?.volumeInfo?.imageLinks?.thumbnail
        description.value = item?.volumeInfo?.description
        averageRating.value = item?.volumeInfo?.averageRating.toString()
        pageCount.value = item?.volumeInfo?.pageCount.toString()
        publishedDate.value = item?.volumeInfo?.publishedDate.toString()

    }

    fun getSearchBook(query: String?) {

        Log.e("diplay", "$query")
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {
                searchResult.value = BooksApi.retrofitService.getBook(query!!).items
                Log.e("display", "${searchResult.value}")

                _status.value = BooksApiStatus.DONE

            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
//                searchResult.value = listOf()
            }
        }
    }


}