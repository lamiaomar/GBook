package com.example.gbook.authentication.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_authentication.*


class HomeAuthenticationFragment : Fragment() {

   private lateinit var auth : FirebaseAuth
   private lateinit var databaseReference: DatabaseReference
   private lateinit var user: User
   private lateinit var uid : String
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

        if (uid.isNotEmpty()){
            getUserData()
        }else{
            Toast.makeText(this.context , "uid empty" , Toast.LENGTH_SHORT).show()

        }

//        val user = Firebase.auth.currentUser
//        if (user != null) {
//            // User is signed in
//            user?.let {
//                // Name, email address, and profile photo Url
//                val name = user.displayName
//                val email = user.email
//                val photoUrl = user.photoUrl
//
//                // Check if user's email is verified
//                val emailVerified = user.isEmailVerified
//
//                // The user's ID, unique to the Firebase project. Do NOT use this value to
//                // authenticate with your backend server, if you have one. Use
//                // FirebaseUser.getToken() instead.
//                val uid = user.uid
//            }
//
//        } else {
//            // No user is signed in
//        }

        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val action = HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToRegistrationFragment()
            btnSignOut.findNavController().navigate(action)

            Toast.makeText(this.context, "signed out", Toast.LENGTH_SHORT).show()


        }

          return binding.root
    }

    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(User::class.java)!!
                binding.userName.setText(user.firstName + "\n  " + user.lastName)
                binding.userEmail.setText(user.email)
                binding.userDate.setText(user.day + "/" + user.month + "/" + user.year)



            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , "Faild to retrive" , Toast.LENGTH_SHORT).show()

            }


        })

    }


}