package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gbook.BookViewmodel
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentBookShelfIsSignBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class BookShelfIsSignFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()
    var uid = auth.currentUser?.uid.toString()

    private val viewModel: BookViewmodel by activityViewModels ()

    private lateinit var binding: FragmentBookShelfIsSignBinding


    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            val action = BookShelfIsSignFragmentDirections.actionBookShelfIsSignFragmentToBookShelfFragment()
                findNavController().navigate(action)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookShelfIsSignBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.btnSignUp.setOnClickListener {
            val action = BookShelfIsSignFragmentDirections.actionBookShelfIsSignFragmentToRegistrationFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }



}