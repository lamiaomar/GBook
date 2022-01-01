package com.example.gbook.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.ShelfItemBinding
import com.example.gbook.ui.BookDetailsUiState

class BookShelfAdapter : ListAdapter<BookDetailsUiState,
        BookShelfAdapter.BookShelfViewHolder>(DiffCallback) {


    class BookShelfViewHolder(
        private var binding :
            ShelfItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetails : BookDetailsUiState){
            binding.result = bookDetails
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookDetailsUiState>(){
        override fun areItemsTheSame(
            oldItem: BookDetailsUiState,
            newItem: BookDetailsUiState
        ): Boolean {
         return   oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: BookDetailsUiState,
            newItem: BookDetailsUiState
        ): Boolean {

            return oldItem.title == newItem.title
        }

    }

    override fun onBindViewHolder(holder: BookShelfViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookShelfViewHolder {
        return BookShelfViewHolder(
            ShelfItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    }

}