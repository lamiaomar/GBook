package com.example.gbook.overview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gbook.BookViewmodel
import com.example.gbook.data.ItemsItem


class BookDetailsViewModel : ViewModel() {

    var bookViewModel = BookViewmodel()


    var title = MutableLiveData<String?>()
    var bookCover = MutableLiveData<String?>()
    var description = MutableLiveData<String?>()
    var averageRating = MutableLiveData<String?>()


    fun displayBookDetails(displayPosition: Int, listNum: Int) {
        try {
            if (listNum == 1) {
                val item = bookViewModel.result.value?.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 2) {
                val item = bookViewModel.secresult.value?.get(displayPosition)
                setBookDetails(item)

            } else if (listNum == 3) {
                val item = bookViewModel.thieresult.value?.get(displayPosition)
                setBookDetails(item)
            } else if (listNum == 4){
                val item = bookViewModel.searchResult.value?.get(displayPosition)
                setBookDetails(item)
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
    }

    override fun onCleared() {
        super.onCleared()
        title.value

    }
}
