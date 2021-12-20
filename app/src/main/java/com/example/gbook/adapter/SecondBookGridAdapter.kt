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

class SecondBookGridAdapter : ListAdapter<ItemsItem,
        SecondBookGridAdapter.SecondBookViewHolder>(DiffCallback) {


    class SecondBookViewHolder(
        private var binding:
        SecGridViewItemBinding
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
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SecondBookViewHolder {
        return SecondBookViewHolder(
            SecGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: SecondBookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        holder.bookThumb.setOnClickListener {

            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position,2)
            holder.bookThumb.findNavController().navigate(action)

        }

    }

}