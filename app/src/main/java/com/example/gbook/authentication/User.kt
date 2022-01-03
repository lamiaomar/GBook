package com.example.gbook.authentication

import com.example.gbook.ui.BookDetailsUiState

data class User(
    var firstName: String? = "",
    var lastName: String? = "",
    var day: String? = "",
    var month: String? = "",
    var year: String? = "",
    var email: String? = "",
    var toReadList: MutableList<BookDetailsUiState> = mutableListOf(),
    var booksNumberInList : Int = -1
) {}


data class BookList(
    val id: String = "",
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String = "",
    val pageCount: String = "",
    val publishedDate: String = ""
)

