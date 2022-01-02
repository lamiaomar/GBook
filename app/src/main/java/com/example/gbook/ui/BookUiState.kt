package com.example.gbook.ui



data class BookCategoryUiState (
    val isSignedIn: Boolean = false,
    val categoryList : List<BooksDataUiState> = listOf()
        )


data class BooksDataUiState(
    val category : String = "" ,
//    val isPremium: Boolean = false,
    val books: List<BookDetailsUiState> = listOf(),
    val numOfBooks : String = ""

)


data class BookDetailsUiState(
//    val id : IndustryIdentifiersItem  ,
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String = "",
    val pageCount: String = "",
    val publishedDate: String = ""
)

//data class IndustryIdentifiersItem(
//    val identifier : String = ""
//)



/*
! BookCategoryUiState( BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) ,
!                      BooksDataUiState( category , BookDetailsUiState) )
 */
