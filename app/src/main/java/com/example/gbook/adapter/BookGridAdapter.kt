package com.example.gbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.GridViewItemBinding
import com.example.gbook.fragments.BookListFragmentDirections
import com.example.gbook.ui.BookDetailsUiState


class BookGridAdapter : ListAdapter<BookDetailsUiState,
        BookGridAdapter.BookViewHolder>(DiffCallback) {

    class BookViewHolder(
        private var binding:
        GridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetails: BookDetailsUiState) {
            binding.result = bookDetails
            binding.executePendingBindings()

        }

                val bookThumb : ImageView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookDetailsUiState>() {
        override fun areItemsTheSame(oldDetails: BookDetailsUiState, newDetails: BookDetailsUiState): Boolean {
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
            GridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)
        Log.e("position","$position")

        holder.bookThumb.setOnClickListener {

            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position, 1)
            holder.bookThumb.findNavController().navigate(action)

        }

    }

}

