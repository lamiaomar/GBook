package com.example.gbook.data

class BooksRepository (
    private val booksRemoteDataSource: BooksRemoteDataSource
) {

    suspend fun getBooks(category : String) : BooksData =
        booksRemoteDataSource.getBooks(category)

    suspend fun getCategoryBook(category : String) : List<BooksData> =
        booksRemoteDataSource.getCategoryBook(category)


}