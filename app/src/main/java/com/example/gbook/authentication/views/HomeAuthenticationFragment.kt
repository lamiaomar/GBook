package com.example.gbook.authentication.views


import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController

import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.view.DataEvent
import kotlinx.android.synthetic.main.fragment_home_authentication.*
import kotlinx.android.synthetic.main.fragment_home_authentication.view.*


class HomeAuthenticationFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels {
        val bookApi = BooksApi.retrofitService

        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()

        val repo = BooksRepository(booksRemoteDataSource, booksRealTimeDataSource)
        BookViewModelFactory(repo)
    }

    private lateinit var binding: FragmentHomeAuthenticationBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeAuthenticationBinding.inflate(inflater)

        binding.lifecycleOwner = this


        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()


        binding.viewModel = viewModel

        if (uid.isNotEmpty()) {
          viewModel.getUserData()
//            binding.numBooks.setText(viewModel.getUserData())

        } else {
            Toast.makeText(this.context, "uid empty", Toast.LENGTH_SHORT).show()

        }


        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val action =
                HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToRegistrationFragment()
            btnSignOut.findNavController().navigate(action)

        }

        binding.calender.setOnClickListener {
            val action =
                HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToCalenderFragment()
            calender.findNavController().navigate(action)
        }

        binding.edit.setOnClickListener {
            val action =
                HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToEditProfileFragment()
            edit.findNavController().navigate(action)
        }

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        viewModel.getUserData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.max = 1000
        val currentProgress = viewModel.userResultUi.value.booksChallenge?.toInt()

        ObjectAnimator.ofInt(progressBar, "progress" , currentProgress!!)
            .setDuration(2000).start()

    }
    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }

}