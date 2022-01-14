package com.example.gbook.ui


data class BookCategoryUiState(
    val isSignedIn: Boolean = false,
    val categoryList: List<BooksDataUiState> = listOf()
)


data class BooksDataUiState(
    val category: String = "",
    val books: List<BookDetailsUiState> = listOf(),
    val numOfBooks: String = ""

)


data class BookDetailsUiState(
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String = "",
    val pageCount: String = "",
    val publishedDate: String = "" ,
    val bookmarked : Boolean = false
)

data class UserUiState(
    val firstName: String? = "",
    val lastName: String? = "",
    val day: String? = "",
    val month: String? = "",
    val year: String? = "",
    val email: String? = "",
    val gender : String? = "",
    val toReadList: MutableList<BookDetailsUiState> = mutableListOf(),
    val booksNumberInList : Int = 0,
    val booksChallenge: String? = "0"

)


/*
! BookCategoryUiState( BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) )
 */
