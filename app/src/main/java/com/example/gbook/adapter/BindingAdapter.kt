package com.example.gbook.adapter

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gbook.R
import com.example.gbook.data.ItemsItem

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.e("imgUrl" , "$imgUrl")

    imgUrl.let {
        val imgUri = imgUrl?.toUri()?.buildUpon()?.build()
        Log.e("imgUri" , "$imgUri")
        Glide.with(imgView.context)
            .load(imgUri)
            .centerCrop() // scale image to fill the entire ImageView
            .error(R.drawable.ic_launcher_foreground)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgView)

    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<ItemsItem>?
) {
    Log.e("TAG", "la : ")
    val adapter = recyclerView.adapter as BookGridAdapter
    adapter.submitList(data)
    Log.e("TAG", "la : ${adapter.submitList(data)}")

}