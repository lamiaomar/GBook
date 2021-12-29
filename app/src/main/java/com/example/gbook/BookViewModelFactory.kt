package com.example.gbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository


class BookViewModelFactory(private val bookApiService: BooksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewmodel(bookApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}