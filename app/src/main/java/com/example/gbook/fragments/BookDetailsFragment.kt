package com.example.gbook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.gbook.BookViewmodel
import com.example.gbook.authentication.BookList
import com.example.gbook.authentication.User
import com.example.gbook.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val POSITION = "title"
private const val LISTNUM = "listNum"


class BookDetailsFragment : Fragment() {

    private val viewModel: BookViewmodel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String

    private var displayPosition: Int = 1
    private var numOfList: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        arguments?.let {
            displayPosition = it.getInt(POSITION)
            numOfList = it.getInt(LISTNUM)

            Log.e("positionD", "$displayPosition")
            Log.e("positionN", "$numOfList")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val binding = FragmentBookDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")


        binding.animationView.pauseAnimation()

        binding.animationView.setOnClickListener {
//            it.animation.cancel()

            binding.animationView.playAnimation()
            if (uid.isNotEmpty()) {
             viewModel.addBookToReadList(displayPosition, numOfList)
            } else {
                Toast.makeText(this.context, "uid is empty", Toast.LENGTH_SHORT).show()

            }

//            if (binding.animationView.isAnimating) binding.animationView.pauseAnimation()

        }

        return binding.root

    }

//    private fun addBookToReadList() {
//
//        val book = BookList(
//             "12",
//         "bookkkkkname",
//         "pic",
//         "description",
//         "4.5",
//        "100",
//         "1995"
//        )
//
//        try {
//            databaseReference.child(uid).child("userLists").push().setValue(book)
//        } catch (e: Exception) {
//            Toast.makeText(this.context, "uid1 is empty", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.displayBookDetails(displayPosition, numOfList)
                Log.e("positionD", "${viewModel.displayBookDetails(displayPosition, numOfList)}")

    }
}