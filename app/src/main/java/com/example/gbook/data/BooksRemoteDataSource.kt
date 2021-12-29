package com.example.gbook.data

import com.example.gbook.network.BookApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRemoteDataSource(
    private val bookApiService: BookApiService ,
    private val ioDispatcher  : CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getBooks(category : String) :BooksData =
        withContext(ioDispatcher){
            bookApiService.getBook(category)
        }

}