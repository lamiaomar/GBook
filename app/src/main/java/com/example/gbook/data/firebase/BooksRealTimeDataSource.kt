package com.example.gbook.data.firebase

import android.util.Log
import com.example.gbook.authentication.User
import com.example.gbook.ui.BookDetailsUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksRealTimeDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private var userData = User()
    private var uid: String = auth.currentUser?.uid.toString()


    suspend fun getBooksToRead(): User
    = withContext(ioDispatcher) {
        uid = auth.currentUser?.uid.toString()
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!

                    userData = User(
                        user.firstName, user.lastName,
                        user.day, user.month, user.year, user.email,
                        user.gender, user.toReadList,
                        user.booksNumberInList
                    )
                    Log.e("user", "${user.booksNumberInList}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
        userData
    }


    suspend fun addBookToReadList(book: BookDetailsUiState)
    = withContext(ioDispatcher) {
        uid = auth.currentUser?.uid.toString()
        databaseReference.child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot?.getValue(User::class.java)

                    val bookList = user?.toReadList

                    val isBookMarked=bookList?.find { it.title==book.title }
                    if(isBookMarked==null){
                        bookList?.add(book)
                        databaseReference.child(uid).child("toReadList")
                            .setValue(bookList)
                        databaseReference.child(uid).child("booksNumberInList")
                            .setValue(bookList?.size)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("add data base error", "$databaseError")
                }
            }
        )
    }


    suspend fun deleteBookFromList(book: BookDetailsUiState)
    = withContext(ioDispatcher) {
            uid = auth.currentUser?.uid.toString()
            databaseReference.child(uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)!!

                        val bookList = user.toReadList

                        bookList.remove(book)
                        databaseReference.child(uid).child("toReadList")
                            .setValue(bookList)
                        databaseReference.child(uid).child("booksNumberInList")
                            .setValue(bookList.size)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Error database" , "$error")
                    }

                })
        }


    suspend fun editUserProfile(userEdit : User)
    = withContext(ioDispatcher){
        uid = auth.currentUser?.uid.toString()
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                    databaseReference.child(uid).child("firstName")
                        .setValue(userEdit.firstName)
                    databaseReference.child(uid).child("lastName")
                        .setValue(userEdit.lastName)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error database" , "$error")
            }

        })
    }

}