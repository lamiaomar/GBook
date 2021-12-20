package com.example.gbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.databinding.FragmentBookDetailsBinding
import com.example.gbook.overview.BookDetailsViewModel

private const val POSITION = "title"
private const val LISTNUM = "listNum"


class BookDetailsFragment : Fragment() {

    private val viewModel : BookDetailsViewModel by activityViewModels()

    private var displayPosition: Int = 0
    private var numOfList: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            displayPosition = it.getInt(POSITION)
            numOfList = it.getInt(LISTNUM)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBookDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.displayBookDetails(displayPosition,numOfList)
    }
}