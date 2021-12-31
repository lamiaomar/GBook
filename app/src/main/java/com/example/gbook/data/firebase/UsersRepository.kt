package com.example.gbook.data.firebase

import com.example.gbook.authentication.User
import com.example.gbook.data.firebase.BooksRealTimeDataSource

class UsersRepository(
    private val booksRealTimeDataSource: BooksRealTimeDataSource
) {

    suspend fun setUserToDB(user: User) =
            booksRealTimeDataSource.setUserToDB(user)

}