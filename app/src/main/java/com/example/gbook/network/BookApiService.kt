package com.example.gbook.network

import com.example.gbook.data.BooksData
import com.example.gbook.ui.BooksDataUiState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com"
private const val ENDPOINT = "/books/v1/volumes"
private const val DEFAULT = "search+terms"
private const val API_KEY = "AIzaSyBlkY49AJ-dhEZCULByKv5Gh1C62WPicCE"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookApiService{
    @GET(BASE_URL+ENDPOINT)
    suspend fun getBook(@Query("q") q : String = DEFAULT , @Query("key") key: String = API_KEY) : BooksData

    @GET(BASE_URL+ENDPOINT)
    suspend fun getCategoryBook(@Query("q") q : String = DEFAULT , @Query("key") key: String = API_KEY) : List<BooksData>

}

object BooksApi {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }

}