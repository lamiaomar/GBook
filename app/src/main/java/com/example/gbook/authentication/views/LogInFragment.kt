package com.example.gbook.authentication.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.example.gbook.databinding.FragmentLogInBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels {
        val bookApi = BooksApi.retrofitService

        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()

        val repo = BooksRepository(booksRemoteDataSource, booksRealTimeDataSource)
        BookViewModelFactory(repo)
    }


    lateinit var signInInputsArray: Array<TextInputEditText?>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        signInInputsArray = arrayOf(etSignInEmail, etSignInPassword)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLogInBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.btnCreateAccount2.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
            btnCreateAccount2.findNavController().navigate(action)

        }


        binding.btnSignIn.setOnClickListener {
            if (notEmpty()) {
                viewModel.signInUser(
                    etSignInEmail.text.toString().trim(), etSignInPassword.text.toString().trim()
                )
                val action =
                    LogInFragmentDirections.actionLogInFragmentToHomeAuthenticationFragment()
                findNavController().navigate(action)
            } else {
                try {
                    signInInputsArray.forEach { input ->
                        if (input!!.text.toString().trim().isEmpty()) {
                            input.error = "${input.hint} is required"
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this.context, "You have to fill all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return binding.root


    }

    private fun notEmpty(): Boolean =
        etSignInEmail.text.toString().trim().isNotEmpty() &&
        etSignInPassword.text.toString().trim().isNotEmpty()

}

