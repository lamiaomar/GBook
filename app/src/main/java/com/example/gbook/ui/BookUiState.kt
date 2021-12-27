package com.example.gbook.ui



data class BooksDataUiState(
    val isSignedIn: Boolean = false,
//    val isPremium: Boolean = false,
    val volume: List<BookItemUiState> = listOf()
)

//data class BooksVolumeUiState(
//    val item: List<BookItemUiState> = listOf()
//)


data class BookItemUiState(
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String ="",
    val pageCount: String ="",
    val publishedDate: String = ""
)




