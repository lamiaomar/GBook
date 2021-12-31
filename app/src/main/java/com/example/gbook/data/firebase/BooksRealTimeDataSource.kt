package com.example.gbook.data.firebase

import com.example.gbook.authentication.User
import com.example.gbook.firebase.BookRealTimeDBService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRealTimeDataSource(
    private val bookRealTimeDBService: BookRealTimeDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun setUserToDB(user: User) {
        withContext(ioDispatcher) {
            bookRealTimeDBService.insertUser(user)
        }
    }


//    suspend fun getToReadBooksList() : Book

}