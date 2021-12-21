package com.example.gbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gbook.data.ItemsItem
import com.example.gbook.network.BooksApi
import kotlinx.coroutines.launch

class BookViewmodel : ViewModel(){


    var result = MutableLiveData<List<ItemsItem?>>()
    var secresult = MutableLiveData<List<ItemsItem?>>()
    var thieresult = MutableLiveData<List<ItemsItem?>>()
    var searchResult = MutableLiveData<List<ItemsItem?>>()


    var qApi1 = "fantasy"
    var qApi2 = "classic"

    init {
        getBook()
    }


    private fun getBook(){

        viewModelScope.launch {
            result.value = BooksApi.retrofitService.getBook(qApi1).items
            secresult.value = BooksApi.retrofitService.getBook( qApi2).items
            thieresult.value = BooksApi.retrofitService.getBook( "kids").items

            //"inauthor:steve inauthor:jobs"

        }
    }

    fun getSearchBook(query : String?){

        viewModelScope.launch {
            try {
                searchResult.value = BooksApi.retrofitService.getBook(query!!).items

                Log.e("search" , "search ${searchResult.value}")
            }catch (e : Exception){

            }
        }
    }

    fun homePageNavigation(){

    }



}