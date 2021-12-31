package com.example.gbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.BooksItemBinding
import com.example.gbook.ui.fragments.BookDetailsFragment
import com.example.gbook.ui.fragments.BookListFragmentDirections


class BooksAdapter : ListAdapter<BookDetailsUiState,
        BooksAdapter.BookViewHolder>(DiffCallback) {

    class BookViewHolder(
        private var binding:
        BooksItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetails: BookDetailsUiState) {
            binding.result = bookDetails
            binding.executePendingBindings()

        }

        var bookThumb: ImageView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookDetailsUiState>() {
        override fun areItemsTheSame(
            oldDetails: BookDetailsUiState,
            newDetails: BookDetailsUiState
        ): Boolean {
            return newDetails.title == oldDetails.title
        }

        override fun areContentsTheSame(
            oldDetails: BookDetailsUiState,
            newDetails: BookDetailsUiState
        ): Boolean {
            return oldDetails.title == newDetails.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        return BookViewHolder(
            BooksItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        Log.e("positionnnnn22222", "$position")
        Log.e("positionnnnn22222","${bookPhoto.title}")
        holder.bookThumb.setOnClickListener {
            val action =
                BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(title = position ,bookTitle = bookPhoto.title)
            holder.bookThumb.findNavController().navigate(action)

        }


    }

}



