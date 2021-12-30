package com.example.gbook.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gbook.BooksApiStatus
import com.example.gbook.R
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.BooksAdapter
import com.example.gbook.ui.BooksDataUiState
import com.example.gbook.ui.CategoryBooksAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.e("imgUrl", "$imgUrl")

    imgUrl.let {
        val imgUri = imgUrl?.toUri()?.buildUpon()?.build()
        Log.e("imgUri", "$imgUri")
        Glide.with(imgView.context)
            .load(imgUri)
            .centerCrop() // scale image to fill the entire ImageView
            .error(R.drawable.ic_connection_error)
            .placeholder(R.drawable.ic_broken_image)
            .into(imgView)

    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookDetailsUiState>?
) {
    val adapter = recyclerView.adapter as BookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("secListData")
fun bindSecRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookDetailsUiState>?
) {
    val adapter = recyclerView.adapter as SecondBookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("thirdListData")
fun bindThirdRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookDetailsUiState>?
) {
    val adapter = recyclerView.adapter as ThirdBookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("searchListData")
fun bindSearchRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookDetailsUiState>?
) {
    val adapter = recyclerView.adapter as SearchBooksGridAdapter
    adapter.submitList(data)
}


@BindingAdapter("booksApiStatus")
fun bindStatus(
    statusImageView: ImageView,
    status: BooksApiStatus?
) {

    when (status) {

        BooksApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        BooksApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        BooksApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }


@BindingAdapter("bookList")
fun bindBookList(
        recyclerView: RecyclerView,
        data: List<BooksDataUiState>?){

        val adapter = recyclerView.adapter as CategoryBooksAdapter
        adapter.submitList(data)

    }

@BindingAdapter("categoryList")
fun bindCategoryList(
        recyclerView: RecyclerView,
        data: List<BooksDataUiState>?){

        val adapter = recyclerView.adapter as CategoryBooksAdapter
        adapter.submitList(data)

    }
}