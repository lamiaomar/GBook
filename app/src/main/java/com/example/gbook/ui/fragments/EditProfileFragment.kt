package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.gbook.BookViewmodel
import com.example.gbook.authentication.User
import com.example.gbook.databinding.FragmentEditProfileBinding
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class EditProfileFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    private lateinit var binding : FragmentEditProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(inflater)

        binding.lifecycleOwner = this


//        day = day?.text.toString().tr ,
//        month = month?.text.toString(),
//        year = year?.text.toString(),
//        gender = gender?.text.toString()


        binding.done.setOnClickListener {
            viewModel.editUserProfile(User(firstName = first_name?.text.toString().trim(),
                lastName = lastname?.text.toString().trim()))
          val action = EditProfileFragmentDirections.actionEditProfileFragmentToHomeAuthenticationFragment()
            done.findNavController().navigate(action)
        }


        return binding.root


    }
}
