package com.example.gbook.data

import com.example.gbook.authentication.User
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.ui.BookDetailsUiState

class BooksRepository (

    private val booksRemoteDataSource: BooksRemoteDataSource ,
    private val booksRealTimeDataSource: BooksRealTimeDataSource
    ) {

    suspend fun getBooks(category : String) : BooksData =
        booksRemoteDataSource.getBooks(category)


    suspend fun getBooksToRead() : User =
        booksRealTimeDataSource.getBooksToRead()


    suspend fun deleteBookFromList(book : BookDetailsUiState) =
        booksRealTimeDataSource.deleteBookFromList(book)


    suspend fun addBookToReadList(book: BookDetailsUiState) =
        booksRealTimeDataSource.addBookToReadList(book)


    suspend fun isBookMarked(book : BookDetailsUiState): Boolean =
        booksRealTimeDataSource.isBookMarked(book)


    suspend fun editUserProfile(userEdit : User) =
        booksRealTimeDataSource.editUserProfile(userEdit)
}

//    suspend fun getNumOfBookList() : Int =
//        booksRealTimeDataSource.getNumOfBookList()