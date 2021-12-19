package com.example.gbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.data.ItemsItem
import com.example.gbook.databinding.GridViewItemBinding

class BookGridAdapter : ListAdapter<ItemsItem,
        BookGridAdapter.BookViewHolder>(DiffCallback) {


    class BookViewHolder(
        private var binding:
        GridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookItem: ItemsItem) {
            binding.result = bookItem
            binding.executePendingBindings()
        }


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
    ): BookViewHolder {
        return BookViewHolder(
            GridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val marsPhoto = getItem(position)
        holder.bind(marsPhoto)

//            holder.button.setOnClickListener {
////            view -> view.findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment)
//                val action =
//                    MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(position)
//                holder.button.findNavController().navigate(action)
//
//            }


    }

}

