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
import com.example.gbook.R
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.example.gbook.databinding.FragmentLogInBinding
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText?>

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

//            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

       binding.btnSignIn.setOnClickListener {
            signInUser()
        }

        return binding.root


    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_sign_in)
//
//        signInInputsArray = arrayOf(etSignInEmail, etSignInPassword)
//
//        btnCreateAccount2.setOnClickListener {
//            var action = LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
//            btnCreateAccount2.findNavController().navigate(action)
//
////            startActivity(Intent(this, CreateAccountActivity::class.java))
//        }
//
//        btnSignIn.setOnClickListener {
//            signInUser()
//        }
//    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = etSignInEmail.text.toString().trim()
        signInPassword = etSignInPassword.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {

                        var action = LogInFragmentDirections.actionLogInFragmentToHomeAuthenticationFragment()
                        Toast.makeText(this.context,"signed in successfully", Toast.LENGTH_SHORT).show()

//                        startActivity(Intent(this, HomeActivity::class.java))
//                        toast("signed in successfully")
                    } else {
                        Toast.makeText(this.context,"sign in failed", Toast.LENGTH_SHORT).show()

//                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input!!.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

}