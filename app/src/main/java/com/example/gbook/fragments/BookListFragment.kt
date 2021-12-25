package com.example.gbook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.gbook.BookViewmodel
import com.example.gbook.adapter.BookGridAdapter
import com.example.gbook.adapter.SearchBooksGridAdapter
import com.example.gbook.adapter.SecondBookGridAdapter
import com.example.gbook.adapter.ThirdBookGridAdapter
import com.example.gbook.databinding.FragmentBookListBinding
import kotlinx.android.synthetic.main.fragment_book_list.*
import androidx.appcompat.app.AppCompatActivity




class BookListFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        val binding = FragmentBookListBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the ViewModel
        binding.viewModel = viewModel

        binding.photosGrid.adapter = BookGridAdapter()
        binding.photosGrid2.adapter = SecondBookGridAdapter()
        binding.photosGrid3.adapter = ThirdBookGridAdapter()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()



    }


}