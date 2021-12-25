package com.example.gbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.adapter.SearchBooksGridAdapter
import com.example.gbook.databinding.FragmentBookListBinding
import com.example.gbook.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the ViewModel
        binding.viewModel = viewModel

        binding.photosGridSearch.adapter = SearchBooksGridAdapter()


        binding.searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                viewModel.getSearchBook(query)

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.getSearchBook(query)

                return true
            }

        })


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}