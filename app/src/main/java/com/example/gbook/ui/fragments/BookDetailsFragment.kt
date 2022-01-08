package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.gbook.BookViewmodel
import com.example.gbook.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val POSITION = "title"
private const val LISTNUM = "bookTitle"
private const val SEARCH = "search"

class BookDetailsFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    private var displayPosition: Int = 0
    private var bookTitle: String = ""
    private var numSearch: Int = 0


    lateinit var binding: FragmentBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        arguments?.let {
            displayPosition = it.getInt(POSITION)
            bookTitle = it.getString(LISTNUM).toString()
            numSearch = it.getInt(SEARCH)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        binding = FragmentBookDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        binding.animationView.setOnClickListener {
            binding.animationView.playAnimation()
            if (uid.isNotEmpty()) {
                lifecycleScope.launch {
                    if (!viewModel.isBookMarked()){
                        if (numSearch == 1) {
                            viewModel.addBookToReadList(1)
                        } else {
                            viewModel.addBookToReadList()
                        }
                    }else{
                        Toast.makeText(context, "book already in the list ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.displayBookDetails(displayPosition, bookTitle, numSearch)

    }

    override fun onDestroy() {
        super.onDestroy()
        bookTitle = ""
        displayPosition = 0
        binding.descriptionText.text = null
        binding.title.title = null

    }
}