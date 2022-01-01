package com.example.gbook.data.firebase

import android.util.Log
import com.example.gbook.authentication.User
import com.example.gbook.firebase.BookRealTimeDBService
import com.example.gbook.ui.BookDetailsUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRealTimeDataSource(
//    private val bookRealTimeDBService: BookRealTimeDBService ,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

/*    suspend fun setUserToDB(user: User) {
//        withContext(ioDispatcher) {
//            bookRealTimeDBService.insertUser(user)
//        }
//    }
*/

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String


   fun getBooksToRead() : User {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    user = snapshot.getValue(User::class.java)!!

                   user = User(user.firstName , user.lastName,
                    user.day , user.month , user.year , user.email , user.toReadList ,
                    user.booksNumberInList)

                    Log.e("shelf" , "$user")


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
     return user
    }


//
//    suspend fun getBooks(): User =
//        withContext(ioDispatcher) {
//            bookRealTimeDBService.getBooks()
//        }
//
}