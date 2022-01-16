package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentDetailsUserBookBinding

private const val POSITION = "title"

class DetailsUserBookFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    private var displayPosition: Int = 0

    lateinit var binding: FragmentDetailsUserBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            displayPosition = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsUserBookBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.displayBookDetailsFromList(displayPosition)
    }

}