package com.example.gbook


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gbook.data.BooksRepository

@Suppress("UNCHECKED_CAST")
class BookViewModelFactory constructor(
    private val booksRepository : BooksRepository ,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(BookViewmodel::class.java) ->
                BookViewmodel(booksRepository) as T

//            modelClass.isAssignableFrom(UserViewModel::class.java) ->
//                UserViewModel(booksRepository) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class")

        }


    }
}
