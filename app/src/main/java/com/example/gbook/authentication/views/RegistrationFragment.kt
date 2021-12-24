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
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseUser
import com.example.gbook.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : Fragment() {

    lateinit var userEmail: String
    lateinit var userPassword: String
     var createAccountInputsArray: Array<EditText?> = arrayOf(null,null,null)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    var createAccountInputsArray: Array<EditText>  = arrayOf(etEmail!! , etPassword!!, etConfirmPassword!!)
//
//        btnCreateAccount.setOnClickListener {
//            signIn()
//        }
//
//        btnSignIn2.setOnClickListener {
//            var action = RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment()
//            btnSignIn2.findNavController().navigate(action)
//            Toast.makeText(this.context, "please sign into your account", Toast.LENGTH_SHORT).show()
////            startActivity(Intent(this, LogInFragment::class.java))
////            toast("please sign into your account")
////            finish()
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createAccountInputsArray   = arrayOf(etEmail, etPassword, etConfirmPassword)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegistrationBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.btnCreateAccount.setOnClickListener {
            signIn()
        }

        binding.btnSignIn2.setOnClickListener {
            var action = RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment()
            btnSignIn2.findNavController().navigate(action)
            Toast.makeText(this.context, "please sign into your account", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, LogInFragment::class.java))
//            toast("please sign into your account")
//            finish()
        }

        return binding.root
    }



    /* check if there's a signed-in user*/

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            var action = RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
            btnSignIn2.findNavController().navigate(action)
//            startActivity(Intent(this, HomeActivity::class.java))

            Toast.makeText(this.context, "welcome back", Toast.LENGTH_SHORT).show()

            /*
            ! from registration to homeAuthen
             */

//            toast("welcome back")
        }
    }

    private fun notEmpty(): Boolean = etEmail.text.toString().trim().isNotEmpty() &&
            etPassword.text.toString().trim().isNotEmpty() &&
            etConfirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            etPassword.text.toString().trim() == etConfirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input!!.text.toString().trim().isEmpty()) {
                    input!!.error = "${input.hint} is required"
                }
            }
        } else {
            Toast.makeText(this.context, "passwords are not matching !", Toast.LENGTH_SHORT).show()
        }
        return identical
    }

    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = etEmail.text.toString().trim()
            userPassword = etPassword.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this.context,
                            "created account successfully !",
                            Toast.LENGTH_SHORT
                        ).show()

                        sendEmailVerification()//                        toast("failed to Authenticate !")


                        var action =
                            RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
                        btnSignIn2.findNavController().navigate(action)
                        /*
                        ! from regis to home
                         */

//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
                    } else {
                        Toast.makeText(this.context, "failed to Authenticate !", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.context, "email sent to $userEmail", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }
}

