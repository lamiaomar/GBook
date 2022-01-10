package com.example.gbook

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gbook.authentication.User
import com.example.gbook.authentication.utils.FirebaseUtils
import com.example.gbook.authentication.views.LogInFragmentDirections
import com.example.gbook.authentication.views.RegistrationFragmentDirections
import com.example.gbook.data.BooksRepository
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.coroutines.launch


class UserViewModel(
    private val usersRepository: BooksRepository
) : ViewModel() {


    var operationState = false


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    var currentUser = User()

    fun signIn(newUser: User, password: String) {
        // identicalPassword() returns true only  when inputs are not empty and passwords are identical
        val userEmail = newUser.email

        /*create a user*/
        if (userEmail != null) {
            FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        addUserDataToDB(newUser)

                        operationState = true

                        sendEmailVerification()
                    }
                }
        }

    }


    fun addUserDataToDB(newUser: User) {
        viewModelScope.launch {
            usersRepository.addUserToDB(newUser)
        }
    }


    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        FirebaseUtils.firebaseUser?.sendEmailVerification()
    }

    fun signInUser(signInEmail: String, signInPassword: String) {
        viewModelScope.launch {
            usersRepository.signInUser(signInEmail ,signInPassword)
        }
    }


    fun onSuccesses(): Boolean {
        return operationState
    }

    fun getUserData(): User {
        var user = User()
        viewModelScope.launch {
         user =  usersRepository.getBooksToRead()
        }
        return user
    }

//    auth = FirebaseAuth.getInstance()
//
//    databaseReference = FirebaseDatabase.getInstance().getReference("users")
//    var uid = auth.currentUser?.uid.toString()
//
//    databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
//
//        override fun onDataChange(snapshot: DataSnapshot) {
//
//
//            for (item in snapshot.children) {
//
//                val user = snapshot.getValue(User::class.java)!!
//
//                currentUser = User(
//                    user.firstName, user.lastName,
//                    user.day, user.month, user.year, user.email,
//                    user.gender, user.toReadList, user.booksNumberInList
//                )
//
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.e("database error", "$error")
//        }
//
//
//    })
//    return currentUser
}




