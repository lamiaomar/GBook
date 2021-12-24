package com.example.gbook.authentication.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.gbook.R
import com.example.gbook.authentication.utils.FirebaseUtils.firebaseAuth
import com.example.gbook.databinding.FragmentHomeAuthenticationBinding
import kotlinx.android.synthetic.main.fragment_home_authentication.*


class HomeAuthenticationFragment : Fragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_home)
//// sign out a user
//
//        btnSignOut.setOnClickListener {
//            firebaseAuth.signOut()
//            val action = HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToRegistrationFragment()
//            btnSignOut.findNavController().navigate(action)
//
//            Toast.makeText(this.context, "signed out", Toast.LENGTH_SHORT).show()
////            startActivity(Intent(this, CreateAccountActivity::class.java))
////            toast("signed out")
////            finish()
//
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeAuthenticationBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val action = HomeAuthenticationFragmentDirections.actionHomeAuthenticationFragmentToRegistrationFragment()
            btnSignOut.findNavController().navigate(action)

            Toast.makeText(this.context, "signed out", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, CreateAccountActivity::class.java))
//            toast("signed out")
//            finish()

        }

        return binding.root
    }


}