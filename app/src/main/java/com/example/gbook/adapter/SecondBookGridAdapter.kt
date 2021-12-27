package com.example.gbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.data.ItemsItem
import com.example.gbook.databinding.SecGridViewItemBinding
import com.example.gbook.fragments.BookListFragmentDirections
import com.example.gbook.ui.BookItemUiState

class SecondBookGridAdapter : ListAdapter<BookItemUiState,
        SecondBookGridAdapter.SearchBookViewHolder>(DiffCallback) {


    class SearchBookViewHolder(
        private var binding:
        SecGridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookItem: BookItemUiState) {
            binding.result = bookItem
            binding.executePendingBindings()

        }

        val bookThumb: ImageView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookItemUiState>() {
        override fun areItemsTheSame(oldItem: BookItemUiState, newItem: BookItemUiState): Boolean {
            return newItem.title == oldItem.title
        }

        override fun areContentsTheSame(oldItem: BookItemUiState, newItem: BookItemUiState): Boolean {
            return oldItem.title == newItem.title
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchBookViewHolder {
        return SearchBookViewHolder(
            SecGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: SearchBookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        holder.bookThumb.setOnClickListener {
            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position,2)
            holder.bookThumb.findNavController().navigate(action)
        }

    }

}

//splash screen