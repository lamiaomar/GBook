package com.example.gbook.ui.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.ShelfItemBinding
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.fragments.BookShelfFragmentDirections
import kotlin.concurrent.fixedRateTimer

class BookShelfAdapter(val delete : (item : BookDetailsUiState) -> Unit)
    : ListAdapter<BookDetailsUiState,
        BookShelfAdapter.BookShelfViewHolder>(DiffCallback) {


    class BookShelfViewHolder(
        private var binding:
        ShelfItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetails: BookDetailsUiState) {
            binding.result = bookDetails
            binding.executePendingBindings()
        }

        val bookThumb: ImageView = binding.bookThumb
        val deleteBook : ImageView = binding.deleteBook


    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookDetailsUiState>() {
        override fun areItemsTheSame(
            oldItem: BookDetailsUiState,
            newItem: BookDetailsUiState
        ): Boolean {
            return oldItem.title == newItem.title
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
        holder.bind(bookPhoto)


        holder.bookThumb.setOnClickListener {
            val action = BookShelfFragmentDirections.actionBookShelfFragmentToDetailsUserBookFragment(position)
            holder.bookThumb.findNavController().navigate(action)
        }

        holder.deleteBook.setOnClickListener {
            delete(bookPhoto)
            notifyItemRemoved(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookShelfViewHolder {
        return BookShelfViewHolder(
            ShelfItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    }

}