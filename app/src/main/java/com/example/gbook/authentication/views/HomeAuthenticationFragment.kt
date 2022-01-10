package com.example.gbook.authentication.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import com.example.gbook.BookViewModelFactory
import com.example.gbook.UserViewModel
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_authentication.*
import java.util.*
import kotlin.collections.ArrayList


class HomeAuthenticationFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels {
        val bookApi = BooksApi.retrofitService

        val booksRemoteDataSource = BooksRemoteDataSource(bookApi)
        val booksRealTimeDataSource = BooksRealTimeDataSource()

        val repo = BooksRepository(booksRemoteDataSource, booksRealTimeDataSource)
        BookViewModelFactory(repo)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
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

        binding.viewModel = viewModel

        if (uid.isNotEmpty()) {
         val user =  viewModel.getUserData()
            binding.userName.setText(user.firstName + " " + user.lastName)
                    binding.userEmail.setText(user.email)
                    binding.userDate.setText(user.day + "/" + user.month + "/" + user.year)
                    binding.booksNumber.setText(user.booksNumberInList.toString())
                    binding.gender.setText(user.gender)
        } else {
            Toast.makeText(this.context, "uid empty", Toast.LENGTH_SHORT).show()

        }


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


//    fun userChallenge(){
//        val arrayList = ArrayList<Int>()
//
//        arrayList?.add(Integer.valueOf(num_of_books?.text.toString()))
//
//        val s = Segment("Books",arrayList?.get(0))
//
//        val sf = SegmentFormatter(Color.BLUE)
//        pie_chart.addSegment(s,sf)
//
//    }

}