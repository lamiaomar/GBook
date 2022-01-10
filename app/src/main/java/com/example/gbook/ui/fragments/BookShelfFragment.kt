package com.example.gbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentBookShelfBinding
import com.example.gbook.ui.BookDetailsUiState
import com.example.gbook.ui.adapter.BookShelfAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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


        binding.shelfRecycler.adapter =
            BookShelfAdapter({ viewModel.deleteBookFromList(book = it) },
                { shareBook(book = it) })





        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        if (uid.isNotEmpty()) {
            viewModel.getBooksToRead()

        } else {
            Toast.makeText(this.context, "uid is empty", Toast.LENGTH_SHORT).show()

        }
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

