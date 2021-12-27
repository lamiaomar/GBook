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
import com.example.gbook.data.ItemsItem
import com.example.gbook.ui.BookItemUiState

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
    data: List<BookItemUiState>?
) {
    val adapter = recyclerView.adapter as BookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("secListData")
fun bindSecRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookItemUiState>?
) {
    val adapter = recyclerView.adapter as SecondBookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("thirdListData")
fun bindThirdRecyclerView(
    recyclerView: RecyclerView,
    data: List<BookItemUiState>?
) {
    val adapter = recyclerView.adapter as ThirdBookGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("searchListData")
fun bindSearchRecyclerView(
    recyclerView: RecyclerView,
    data: List<ItemsItem>?
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

}