package com.example.gbook.authentication

import com.example.gbook.ui.BookDetailsUiState
import okhttp3.Challenge

data class User(
    val firstName: String? = "",
    val lastName: String? = "",
    val day: String? = "",
    val month: String? = "",
    val year: String? = "",
    val email: String? = "",
    val gender: String? = "",
    val toReadList: MutableList<BookDetailsUiState> = mutableListOf(),
    val booksNumberInList: Int = 0 ,
    val booksChallenge: String? = "0"
) {}


data class BookList(
    val title: String = "",
    val bookCover: String = "",
    val description: String = "",
    val averageRating: String = "",
    val pageCount: String = "",
    val publishedDate: String = "",
    val bookmarked: Boolean = false
)

