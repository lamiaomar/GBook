package com.example.gbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.data.ItemsItem
import com.example.gbook.databinding.SearchGridViewItemBinding
import com.example.gbook.fragments.BookListFragmentDirections
import com.example.gbook.fragments.SearchFragmentDirections

class SearchBooksGridAdapter : ListAdapter<ItemsItem,
        SearchBooksGridAdapter.SearchBookViewHolder>(DiffCallback) {


    class SearchBookViewHolder(
        private var binding:
        SearchGridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookItem: ItemsItem) {
            binding.result = bookItem
            binding.executePendingBindings()

        }

        val bookThumb: ImageView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return newItem.volumeInfo!!.title == oldItem.volumeInfo!!.title
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchBookViewHolder {
        return SearchBookViewHolder(
            SearchGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: SearchBookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        holder.bookThumb.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment(position, 4)
            holder.bookThumb.findNavController().navigate(action)

        }

    }

}

