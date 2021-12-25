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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gbook.R
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.example.gbook.databinding.FragmentLogInBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
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


        binding.btnCreateAccount2.setOnClickListener {
            var action = LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
            btnCreateAccount2.findNavController().navigate(action)

        }

       binding.btnSignIn.setOnClickListener {
            signInUser()
        }

        return binding.root


    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = etSignInEmail.text.toString().trim()
        signInPassword = etSignInPassword.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {

                        var action = LogInFragmentDirections.actionLogInFragmentToHomeAuthenticationFragment()
                        findNavController().navigate(action)

                        Toast.makeText(this.context,"signed in successfully", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this.context,"sign in failed", Toast.LENGTH_SHORT).show()

                    }
                }
        } else {
            try {
                signInInputsArray.forEach { input ->
                    if (input!!.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"
                    }
                }
            }catch (e:Exception){
                Toast.makeText(this.context, "You have to fill all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }

}