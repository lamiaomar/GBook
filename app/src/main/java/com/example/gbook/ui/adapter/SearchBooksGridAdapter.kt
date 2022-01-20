package com.example.gbook.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.databinding.SearchGridViewItemBinding
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.fragments.SearchFragmentDirections


class SearchBooksGridAdapter : ListAdapter<BookDetailsUiState,
        SearchBooksGridAdapter.SearchBookViewHolder>(DiffCallback) {


    class SearchBookViewHolder(
        private var binding:
        SearchGridViewItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetails: BookDetailsUiState) {
            binding.result = bookDetails
            binding.executePendingBindings()

        }

        val bookThumb: ImageView = binding.bookThumb

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookDetailsUiState>() {
        override fun areItemsTheSame(oldDetails: BookDetailsUiState,
                                     newDetails: BookDetailsUiState): Boolean {
            return newDetails.title == oldDetails.title
        }

        override fun areContentsTheSame(oldDetails: BookDetailsUiState,
                                        newDetails: BookDetailsUiState): Boolean {
            return oldDetails.title == newDetails.title
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
                SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment(position,"",1)
            holder.bookThumb.findNavController().navigate(action)

        }

    }

}


//class SearchLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter< SearchBooksGridAdapter.SearchBookViewHolder>() {
//    override fun onBindViewHolder(holder:  SearchBooksGridAdapter.SearchBookViewHolder, loadState: LoadState) {
//        holder.bind(loadState)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState):  SearchBooksGridAdapter.SearchBookViewHolder {
//        return  SearchBooksGridAdapter.SearchBookViewHolder.create(parent, retry)
//    }
//}