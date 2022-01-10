package com.example.gbook.authentication.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_authentication.*
import java.util.*


class HomeAuthenticationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var binding: FragmentHomeAuthenticationBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeAuthenticationBinding.inflate(inflater)

        binding.lifecycleOwner = this

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        if (uid.isNotEmpty()) {
            getUserData()
        } else {
            Toast.makeText(this.context, "uid empty", Toast.LENGTH_SHORT).show()

        }

        //region Picker
        //Date picker builder
       val builder = MaterialDatePicker.Builder.datePicker()

        //Date Picker button on click listener
//        binding.calender.setOnClickListener {
//            val picker = builder.build()
//            picker.show(requireFragmentManager(),"Select data")
//
//            picker.addOnPositiveButtonClickListener {
//                binding.date.setText(picker.headerText)
//            }
//        }
        //endregion

        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val action =
                HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToRegistrationFragment()
            btnSignOut.findNavController().navigate(action)

        }

        binding.calender.setOnClickListener {
            val action = HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToCalenderFragment()
           calender.findNavController().navigate(action)
        }

        binding.edit.setOnClickListener {
            val action = HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToEditProfileFragment()
            edit.findNavController().navigate(action)
        }

        return binding.root
    }

    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                for (item in snapshot.children) {

                    user = snapshot.getValue(User::class.java)!!

                    binding.userName.setText(user.firstName + " " + user.lastName)
                    binding.userEmail.setText(user.email)
                    binding.userDate.setText(user.day + "/" + user.month + "/" + user.year)
                    binding.booksNumber.setText(user.booksNumberInList.toString())
                    binding.gender.setText(user.gender)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to retrive", Toast.LENGTH_SHORT).show()

            }


        })

    }


}