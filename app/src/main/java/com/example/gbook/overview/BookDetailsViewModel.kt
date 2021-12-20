package com.example.gbook.overview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gbook.BookViewmodel


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
                title.value = item?.volumeInfo?.title
                bookCover.value = item?.volumeInfo?.imageLinks?.thumbnail
                description.value = item?.volumeInfo?.description
                averageRating.value = item?.volumeInfo?.averageRating.toString()

            } else if (listNum == 2) {
                val item = bookViewModel.secresult.value?.get(displayPosition)
                title.value = item?.volumeInfo?.title
                bookCover.value = item?.volumeInfo?.imageLinks?.thumbnail
                description.value = item?.volumeInfo?.description
                averageRating.value = item?.volumeInfo?.averageRating.toString()

            }else if (listNum == 3){
                val item = bookViewModel.thieresult.value?.get(displayPosition)
                title.value = item?.volumeInfo?.title
                bookCover.value = item?.volumeInfo?.imageLinks?.thumbnail
                description.value = item?.volumeInfo?.description
                averageRating.value = item?.volumeInfo?.averageRating.toString()
            }

        } catch (e: Exception) {
            title.value = "${e.message}"
        }

    }


    override fun onCleared() {
        super.onCleared()
        title.value = "Empty title"
    }
}
