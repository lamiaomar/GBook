package com.example.gbook.data

import com.example.gbook.authentication.User
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.ui.BookDetailsUiState
import kotlinx.coroutines.withContext

class BooksRepository(
    private val booksRemoteDataSource: BooksRemoteDataSource,
    private val booksRealTimeDataSource: BooksRealTimeDataSource
) {


    suspend fun getBooks(category: String): BooksData =
        booksRemoteDataSource.getBooks(category)


    suspend fun getBooksToRead(): User =
        booksRealTimeDataSource.getBooksToRead()


    suspend fun deleteBookFromList(book: BookDetailsUiState) =
        booksRealTimeDataSource.deleteBookFromList(book)


    suspend fun addBookToReadList(book: BookDetailsUiState) =
        booksRealTimeDataSource.addBookToReadList(book)


    suspend fun editUserProfile(userEdit: User) =
        booksRealTimeDataSource.editUserProfile(userEdit)


    suspend fun signInUser(signInEmail: String, signInPassword: String) =
        booksRealTimeDataSource.signInUser(signInEmail, signInPassword)


    suspend fun signIn(newUser: User, password: String) =
        booksRealTimeDataSource.signIn(newUser, password)

}

