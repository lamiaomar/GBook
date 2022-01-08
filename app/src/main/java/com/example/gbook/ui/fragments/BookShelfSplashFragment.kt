package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gbook.BookShelfSplashFragmentDirections
import com.example.gbook.BookViewmodel
import com.example.gbook.databinding.FragmentBookShelfSplashBinding
import com.google.firebase.auth.FirebaseAuth


class BookShelfSplashFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()
    var uid = auth.currentUser?.uid.toString()

    private val viewModel: BookViewmodel by activityViewModels ()


    private lateinit var binding: FragmentBookShelfSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookShelfSplashBinding.inflate(inflater)

        binding.lifecycleOwner = this


        viewModel.title.observe(viewLifecycleOwner, Observer { user ->

            if (uid.isNotEmpty()) {
                val action = BookShelfSplashFragmentDirections.actionBookShelfSplashFragmentToBookShelfFragment()
                findNavController().navigate(action)
            }
        })
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}