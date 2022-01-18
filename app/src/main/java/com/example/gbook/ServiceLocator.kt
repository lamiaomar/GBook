package com.example.gbook

import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BookApiService
import com.example.gbook.data.network.BooksApi

object ServiceLocator {

    private fun provideBookApi() : BookApiService = BooksApi.retrofitService


    private fun provideBooksRemoteDataSource() : BooksRemoteDataSource = BooksRemoteDataSource(
        provideBookApi())

    private fun provideBooksRealTimeDataSource() : BooksRealTimeDataSource = BooksRealTimeDataSource()


    fun provideBooksRepository() : BooksRepository = BooksRepository(
        provideBooksRemoteDataSource() , provideBooksRealTimeDataSource())

}

/*
        val bookApi = BooksApi.retrofitService
        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()
        val repo = BooksRepository(booksRemoteDataSource, booksRealTimeDataSource)
        BookViewModelFactory(repo)
 */