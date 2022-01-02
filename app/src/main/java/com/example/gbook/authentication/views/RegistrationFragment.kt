package com.example.gbook.authentication.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.gbook.R
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseUser
import com.example.gbook.databinding.FragmentRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : Fragment() {

    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var userFirstName: String
    lateinit var userLastName: String
    lateinit var userDay: String
    lateinit var userMonth: String
    lateinit var userYear: String


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    var createAccountInputsArray: Array<TextInputEditText?> = arrayOf(null, null, null)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        createAccountInputsArray = arrayOf(email, password, re_password)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRegistrationBinding.inflate(inflater)

        binding.lifecycleOwner = this


        binding.btnCreateAccount.setOnClickListener {
            signIn()
        }

        binding.btnSignIn2.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment()
            btnSignIn2.findNavController().navigate(action)
            Toast.makeText(this.context, "please sign into your account", Toast.LENGTH_SHORT).show()
        }


        val items = resources.getStringArray(R.array.months)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.month.setAdapter(adapter)

        return binding.root
    }


    /* check if there's a signed-in user*/

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            val action =
                RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
            btnSignIn2.findNavController().navigate(action)

            Toast.makeText(this.context, "welcome back", Toast.LENGTH_SHORT).show()
        }
    }

    private fun notEmpty(): Boolean = email.text.toString().trim().isNotEmpty() &&
            password.text.toString().trim().isNotEmpty() &&
            re_password.text.toString().trim().isNotEmpty() &&
            first_name.text.toString().trim().isNotEmpty() &&
            lastname.text.toString().trim().isNotEmpty() &&
            day.text.toString().trim().isNotEmpty() &&
            month.text.toString().trim().isNotEmpty() &&
            year.text.toString().trim().isNotEmpty() &&
            email.text.toString().trim().isNotEmpty()


    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            password.text.toString().trim() == re_password.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input?.text.toString().trim().isEmpty()) {
                    input?.error = "${input?.hint} is required"
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
            userEmail = email.text.toString().trim()
            userPassword = password.text.toString().trim()


            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this.context,
                            "created account successfully !",
                            Toast.LENGTH_SHORT
                        ).show()

                        addUserDataToDB()

                        sendEmailVerification()


                        val action =
                            RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
                        btnSignIn2.findNavController().navigate(action)

                    } else {
                        Toast.makeText(this.context, "failed to Authenticate !", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        }
    }


    private fun addUserDataToDB() {
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        userFirstName = first_name.text.toString().trim()
        userLastName = lastname.text.toString().trim()
        userDay = day.text.toString().trim()
        userMonth = month.text.toString().trim()
        userYear = year.text.toString().trim()
        userEmail = email.text.toString().trim()


        val user = User(
            userFirstName,
            userLastName,
            userDay,
            userMonth,
            userYear,
            userEmail,
            listOf(),
            -1
        )
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this.context, "add to database succ", Toast.LENGTH_SHORT).show()
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

