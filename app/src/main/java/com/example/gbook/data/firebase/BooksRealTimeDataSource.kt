package com.example.gbook.data.firebase

import android.util.Log
import com.example.gbook.authentication.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRealTimeDataSource(
//    private val bookRealTimeDBService: BookRealTimeDBService ,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private var currentUser = User()
    private var uid: String = auth.currentUser?.uid.toString()


    suspend fun getBooksToRead(): User = withContext(ioDispatcher) {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    var user = snapshot.getValue(User::class.java)!!

                    currentUser = User(
                        user.firstName, user.lastName,
                        user.day, user.month, user.year, user.email,
                        user.gender, user.toReadList,
                        user.booksNumberInList
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
        currentUser
    }


//
//    suspend fun getBooks(): User =
//        withContext(ioDispatcher) {
//            bookRealTimeDBService.getBooks()
//        }
//
}