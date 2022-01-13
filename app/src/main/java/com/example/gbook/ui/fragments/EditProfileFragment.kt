package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.gbook.BookViewmodel
import com.example.gbook.R
import com.example.gbook.authentication.User
import com.example.gbook.databinding.FragmentEditProfileBinding
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class EditProfileFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    private lateinit var binding: FragmentEditProfileBinding


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
            if (nameId != null) {
                viewModel.editUserProfile(User(firstName = first_name?.text.toString().trim()))
            }
            if (last_name_textinput != null) {
                viewModel.editUserProfile(User(lastName = lastname?.text.toString().trim()))
            }
            if (dayBirth != null) {
                viewModel.editUserProfile(User(day = day?.text.toString().trim()))
            }

            if (monthBirth != null) {
                viewModel.editUserProfile(User(month = month?.text.toString().trim()))
            }

            if (yearBirth != null) {
                viewModel.editUserProfile(User(year = year?.text.toString().trim()))
            }

            if (genders != null) {
                viewModel.editUserProfile(User(gender = gender?.text.toString().trim()))
            }

            val action =
                EditProfileFragmentDirections.actionEditProfileFragmentToHomeAuthenticationFragment()
            done.findNavController().navigate(action)
        }



        val items = resources.getStringArray(R.array.months)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.month.setAdapter(adapter)


        val item = resources.getStringArray(R.array.gender)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.dropdown_item, item)
        binding.gender.setAdapter(adapter2)


        return binding.root


    }

}
