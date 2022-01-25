package com.example.gbook.authentication.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.ServiceLocator
import com.example.gbook.authentication.User
import com.example.gbook.authentication.extensions.Extensions.isValid
import com.example.gbook.authentication.extensions.TextTypes
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.gender
import kotlinx.coroutines.delay


class RegistrationFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels {
        BookViewModelFactory(ServiceLocator.provideBooksRepository())
    }

    var createAccountInputsArray: Array<TextInputEditText?> = arrayOf(null, null, null)

    lateinit var binding: FragmentRegistrationBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createAccountInputsArray = arrayOf(email, password, re_password)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegistrationBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        binding.btnCreateAccount.setOnClickListener {
//            if (!validationCheck()) {
            val user = User(
                first_name.text.toString().trim(),
                lastname.text.toString().trim(),
                day.text.toString().trim(),
                month.text.toString().trim(),
                year.text.toString().trim(),
                email.text.toString().trim(),
                gender.text.toString().trim(),
                mutableListOf(),
                0)

            if (identicalPassword()) {
                viewModel.signIn(user , password.text.toString())
                if (viewModel.onSuccesses()){
                    val action =
                        RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
                    btnSignIn2.findNavController().navigate(action)

                }
//                else{
//                    Toast.makeText(context, "Failed registration", Toast.LENGTH_SHORT).show()
//                }

            }
//            }
        }

        binding.btnSignIn2.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment()
            btnSignIn2.findNavController().navigate(action)
            Toast.makeText(this.context, "please sign into your account", Toast.LENGTH_SHORT).show()
        }


        val items = resources.getStringArray(R.array.months)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.month.setAdapter(adapter)


        val item = resources.getStringArray(R.array.gender)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.dropdown_item, item)
        binding.gender.setAdapter(adapter2)


        return binding.root
    }

    private fun validationCheck(): Boolean {
        var isValid = true

        if (binding.EmailId.isValid(binding.email, TextTypes.EMAIL))
            isValid = false


        if (binding.passlId.isValid(binding.password, TextTypes.PASSWORD))
            isValid = false


        if (binding.repasslId.isValid(binding.rePassword, TextTypes.RE_PASSWORD))
            isValid = false


        if (binding.nameId.isValid(binding.firstName, TextTypes.FIRSTNAME))
            isValid = false


        if (binding.lastNameTextinput.isValid(binding.lastname, TextTypes.LASTNAME))
            isValid = false

        if (binding.dayBirth.isValid(binding.day, TextTypes.DAY))
            isValid = false

        if (binding.yearBirth.isValid(binding.year, TextTypes.YEAR))
            isValid = false

        return isValid
    }


    /* check if there's a signed-in user*/

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            val action =
                RegistrationFragmentDirections.actionRegistrationFragmentToHomeAuthenticationFragment()
            btnSignIn2.findNavController().navigate(action)
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
        if (notEmpty()&&
            password.text.toString().trim() == re_password.text.toString().trim()
        ) {
            identical = true
        } else if (notEmpty()){
            createAccountInputsArray.forEach { input ->
                if (input?.text.toString().trim().isEmpty()) {
                    input?.error = "${input?.hint} is required"
                }
            }
        }
        return identical
    }

}

