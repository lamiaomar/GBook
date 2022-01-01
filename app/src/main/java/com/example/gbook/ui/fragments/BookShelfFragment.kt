package com.example.gbook.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.UserViewModel
import com.example.gbook.authentication.User
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentBookShelfBinding
import com.example.gbook.ui.adapter.BookShelfAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookShelfFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String

    private val viewModel: BookViewmodel by activityViewModels {
        val bookApi = BooksApi.retrofitService

        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()

        val repo = BooksRepository(booksRemoteDataSource , booksRealTimeDataSource)
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

        binding.shelfRecycler.adapter = BookShelfAdapter()

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")


        if (uid.isNotEmpty()) {
//            viewModel.getBooksToRead()

        } else {
            Toast.makeText(this.context, "uid is empty", Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }

}

