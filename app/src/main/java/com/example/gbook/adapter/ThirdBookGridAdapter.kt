package com.example.gbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.data.ItemsItem
import com.example.gbook.databinding.ThirdGridViewItemBinding
import com.example.gbook.fragments.BookListFragmentDirections

class ThirdBookGridAdapter : ListAdapter<ItemsItem,
        ThirdBookGridAdapter.ThirdBookViewHolder>(DiffCallback) {


    class ThirdBookViewHolder(
        private var binding:
        ThirdGridViewItemBinding
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
    ): ThirdBookViewHolder {
        return ThirdBookViewHolder(
            ThirdGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: ThirdBookViewHolder, position: Int) {
        val bookPhoto = getItem(position)
        holder.bind(bookPhoto)

        holder.bookThumb.setOnClickListener {
            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position,3)
            holder.bookThumb.findNavController().navigate(action)

        }

    }

}