package com.example.gbook.firebase

import com.example.gbook.authentication.User
import com.example.gbook.data.BooksRealTimeDataSource
import kotlinx.coroutines.withContext

class UsersRepository(
    private val booksRealTimeDataSource: BooksRealTimeDataSource
) {

    suspend fun setUserToDB(user: User) =
            booksRealTimeDataSource.setUserToDB(user)

}