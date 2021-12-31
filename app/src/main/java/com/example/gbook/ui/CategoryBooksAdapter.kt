package com.example.gbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.CategoryItemBinding
import com.example.gbook.ui.fragments.BookDetailsFragment
import com.example.gbook.ui.fragments.BookListFragmentDirections

class CategoryBooksAdapter : ListAdapter<BooksDataUiState,
        CategoryBooksAdapter.BookViewHolder>(DiffCallback) {

    class BookViewHolder(
        private var binding:
        CategoryItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookDetails: BooksDataUiState) {

            binding.booksCtegory.text = bookDetails.category

            val adapter = BooksAdapter()
            binding.bookList.adapter = adapter
            adapter.submitList(bookDetails.books)

        }

        var bookCategory: TextView = binding.booksCtegory

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BooksDataUiState>() {
        override fun areItemsTheSame(
            oldDetails: BooksDataUiState,
            newDetails: BooksDataUiState
        ): Boolean {
            return newDetails.category == oldDetails.category
        }

        override fun areContentsTheSame(
            oldDetails: BooksDataUiState,
            newDetails: BooksDataUiState
        ): Boolean {
            return oldDetails.category == newDetails.category
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        return BookViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        val fragment = BookDetailsFragment()
        var bundle = Bundle()
        bundle.putInt("key", position)
        fragment.setArguments(bundle)
    }
}




