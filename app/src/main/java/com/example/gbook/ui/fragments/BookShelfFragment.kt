package com.example.gbook.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gbook.BookShelfSplashFragmentDirections
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.MainActivity
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentBookShelfBinding
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.adapter.BookShelfAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookShelfFragment : Fragment() {


    var auth = FirebaseAuth.getInstance()
   var uid = auth.currentUser?.uid.toString()

    private val viewModel: BookViewmodel by activityViewModels {
        val bookApi = BooksApi.retrofitService

        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()

        val repo = BooksRepository(booksRemoteDataSource, booksRealTimeDataSource)
        BookViewModelFactory(repo)
    }

    private lateinit var binding: FragmentBookShelfBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentBookShelfBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        viewModel.title.observe(viewLifecycleOwner, Observer { user ->
            if ( uid.isEmpty()) {
                val action = BookShelfFragmentDirections.actionBookShelfFragmentToBookShelfSplashFragment()
                findNavController().navigate(action)
            }
        })


        binding.shelfRecycler.adapter =
            BookShelfAdapter({ viewModel.deleteBookFromList(book = it) },
                { shareBook(book = it) })



        if (uid.isNotEmpty()) {
            viewModel.getBooksToRead()

        } else {
            Toast.makeText(this.context, "uid is empty", Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private fun shareBook(book : BookDetailsUiState) {
        val intent = Intent(Intent.ACTION_SEND).putExtra(
            Intent.EXTRA_TEXT ,
            "I'm reading ${book.title} ,great book" +
                    ",it is about.. ${book.description}"
        ).setType("text/plain")
        val shareIntent = Intent.createChooser(intent, "GBook")
        context?.startActivity(shareIntent)

    }


}

