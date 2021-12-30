package com.example.gbook.ui

import kotlinx.coroutines.flow.MutableStateFlow


data class BookCategoryUiState (
    val isSignedIn: Boolean = false,
    val categoryList : List<BooksDataUiState> = listOf()
        )


data class BooksDataUiState(
    val category : String = "" ,
//    val isPremium: Boolean = false,
    val books: List<BookDetailsUiState> = listOf()

)


data class BookDetailsUiState(
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String = "",
    val pageCount: String = "",
    val publishedDate: String = ""
)

/*
! BookCategoryUiState( BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) )
 */
