package com.example.gbook.data

import com.example.gbook.authentication.User
import com.example.gbook.data.firebase.BooksRealTimeDataSource

class BooksRepository (

    private val booksRemoteDataSource: BooksRemoteDataSource ,
    private val booksRealTimeDataSource: BooksRealTimeDataSource
    ) {

    suspend fun getBooks(category : String) : BooksData =
        booksRemoteDataSource.getBooks(category)

//    suspend fun getCategoryBook(category : String) : List<BooksData> =
//        booksRemoteDataSource.getCategoryBook(category)


//    suspend fun getBooks(): User =
//        booksRealTimeDataSource.getBooks()

    suspend fun getBooksToRead() : User =
        booksRealTimeDataSource.getBooksToRead()

}