package com.example.gbook.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gbook.BooksApiStatus
import com.example.gbook.R
import com.example.gbook.ui.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    imgUrl.let {
        val imgUri = imgUrl?.toUri()?.buildUpon()?.build()
        Glide.with(imgView.context)
            .load(imgUri)
            .centerCrop() // scale image to fill the entire ImageView
            .error(R.drawable.ic_connection_error)
            .placeholder(R.drawable.ic_broken_image)
            .into(imgView)

    }
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
}



@BindingAdapter("categoryList")
fun bindCategoryList(
    recyclerView: RecyclerView,
    data: List<BooksDataUiState>?
) {
    val adapter = recyclerView.adapter as CategoryBooksAdapter
    adapter.submitList(data)

}


/*@BindingAdapter("booklist")
//fun bindBookList(
//    recyclerView: RecyclerView,
//    bookDetails: List<BookDetailsUiState>?
//) {
//    Log.e("data", "$bookDetails")
//
//    val adapter = recyclerView.adapter as BooksAdapter?
//    Log.e("data", "$adapter")
//
//    adapter?.submitList(bookDetails)
//
//}
*/

@BindingAdapter("shelfList")
fun bindShelfList(
    recyclerView: RecyclerView,
    data: List<BookDetailsUiState>?
){
    val adapter = recyclerView.adapter as BookShelfAdapter
    adapter.submitList(data)
}