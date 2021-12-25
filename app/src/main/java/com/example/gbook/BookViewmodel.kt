package com.example.gbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.data.ItemsItem
import com.example.gbook.network.BooksApi
import kotlinx.coroutines.launch

enum class BooksApiStatus { LOADING, ERROR, DONE }


class BookViewmodel : ViewModel(){


    var result = MutableLiveData<List<ItemsItem?>>()
    var secresult = MutableLiveData<List<ItemsItem?>>()
    var thieresult = MutableLiveData<List<ItemsItem?>>()
    var searchResult = MutableLiveData<List<ItemsItem?>>()


    private val _status = MutableLiveData<BooksApiStatus>()
    val status: LiveData<BooksApiStatus> = _status


    var qApi1 = "fantasy"
    var qApi2 = "classic"

    init {
        getBook()
    }


    private fun getBook(){
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {

                result.value = BooksApi.retrofitService.getBook(qApi1).items
                secresult.value = BooksApi.retrofitService.getBook( qApi2).items
                thieresult.value = BooksApi.retrofitService.getBook( "kids").items


                _status.value = BooksApiStatus.DONE

            }catch (e:Exception){
                _status.value = BooksApiStatus.ERROR
                result.value = listOf()
                secresult.value = listOf()
                thieresult.value = listOf()
            }

            //"inauthor:steve inauthor:jobs"

        }
    }

    fun getSearchBook(query : String?){

        Log.e("diplay" , "$query")
        _status.value = BooksApiStatus.LOADING
        viewModelScope.launch {
            try {
                searchResult.value = BooksApi.retrofitService.getBook(query!!).items
                Log.e("display" , "${searchResult.value}")

                _status.value = BooksApiStatus.DONE

            }catch (e:Exception){
                _status.value = BooksApiStatus.ERROR
//                searchResult.value = listOf()
            }
        }
    }


}