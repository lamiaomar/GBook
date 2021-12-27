package com.example.gbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.GridViewItemBinding
import com.example.gbook.ui.BookItemUiState


class BookGridAdapter : ListAdapter<BookItemUiState,
        BookGridAdapter.BookViewHolder>(DiffCallback) {

    class BookViewHolder(
        private var binding:
        GridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookItem: BookItemUiState) {
            binding.result = bookItem
            binding.executePendingBindings()

        }

        //        val bookThumb : ImageView = binding.bookThumb
//        val bookThumb: TextView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookItemUiState>() {
        override fun areItemsTheSame(oldItem: BookItemUiState, newItem: BookItemUiState): Boolean {
            return newItem.title == oldItem.title
        }

        override fun areContentsTheSame(
            oldItem: BookItemUiState,
            newItem: BookItemUiState
        ): Boolean {
            return oldItem.title == newItem.title
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

//        holder.bookThumb.setOnClickListener {
//
//            val action =
//                BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position, 1)
//            holder.bookThumb.findNavController().navigate(action)
//
//        }

    }

}

