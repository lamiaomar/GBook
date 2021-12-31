package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewmodel
import com.example.gbook.databinding.FragmentBookListBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gbook.BookViewModelFactory
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.network.BooksApi
import com.example.gbook.ui.CategoryBooksAdapter
import kotlinx.coroutines.launch


class BookListFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels {
        val bookApi = BooksApi.retrofitService
        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val repo = BooksRepository(booksRemoteDataSource)
        BookViewModelFactory(repo)
    }
    lateinit var ay7aga: FragmentBookListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        val binding = FragmentBookListBinding.inflate(inflater)
        ay7aga = binding
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = viewLifecycleOwner

        // Giving the binding access to the ViewModel
        binding.viewModel = viewModel
        binding.categoryList.adapter = CategoryBooksAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.bookCategoryResultUi.collect {
                    val adapter = ay7aga.categoryList.adapter as CategoryBooksAdapter
                    adapter.submitList(it.categoryList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()


    }


}