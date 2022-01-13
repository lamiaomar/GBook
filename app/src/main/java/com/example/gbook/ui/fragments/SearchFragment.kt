package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.ui.adapter.SearchBooksGridAdapter
import com.example.gbook.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {


private val viewModel: BookViewmodel by activityViewModels {
    val bookApi = BooksApi.retrofitService

    val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
    val booksRealTimeDataSource = BooksRealTimeDataSource()

    val repo = BooksRepository(booksRemoteDataSource , booksRealTimeDataSource)
    BookViewModelFactory(repo)
}
    private var binding : FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentSearchBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding!!.lifecycleOwner = this

        // Giving the binding access to the ViewModel
        binding!!.viewModel = viewModel

        binding!!.photosGridSearch.adapter = SearchBooksGridAdapter()


        binding!!.searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding!!.searchView.clearFocus()
                viewModel.getSearchBook(query)

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.getSearchBook(query)

                return true
            }

        })


        return binding!!.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}