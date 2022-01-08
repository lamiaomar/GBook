package com.example.gbook.data.firebase

import android.util.Log
import com.example.gbook.authentication.User
import com.example.gbook.ui.BookDetailsUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class BooksRealTimeDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private var userData = User()
    private var uid: String = auth.currentUser?.uid.toString()



    suspend fun getBooksToRead(): User =
        withContext(ioDispatcher) {
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
                    val user = dataSnapshot.getValue(User::class.java)!!
                    val booksNum = user.booksNumberInList

                    if (booksNum == 0) {
                        databaseReference.child(uid).child("toReadList")
                            .child("0").setValue(book)

                        databaseReference.child(uid).child("booksNumberInList")
                            .setValue(1)
                        return
                    } else {
                        val newNumberOfBook = booksNum + 1

                        databaseReference.child(uid).child("toReadList")
                            .child(booksNum.toString()).setValue(book)

                        databaseReference.child(uid).child("booksNumberInList")
                            .setValue(newNumberOfBook)
                        return
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("add data base error", "$databaseError")
                }
            }
        )
    }


    suspend fun deleteBookFromList(book: BookDetailsUiState) =
        withContext(ioDispatcher) {
            uid = auth.currentUser?.uid.toString()
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        val user = snapshot.getValue(User::class.java)!!
                        try {
                            val localList = user.toReadList

                            for (books in 0..user.booksNumberInList) {

                                if (book.title == user.toReadList[books].title) {
                                    localList.removeAt(books)
                                    databaseReference.child(uid).child("toReadList")
                                        .removeValue()

                                    for (it in 0..localList.size) {
                                        databaseReference.child(uid).child("toReadList")
                                            .child(it.toString()).setValue(
                                                localList[it]
                                            ).addOnSuccessListener {
                                                databaseReference.child(uid)
                                                    .child("booksNumberInList")
                                                    .setValue(user.toReadList.size)
                                            }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("error delete book", "Exception is : $e")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel delete book", "DatabaseError is : $error")
                }
            })
        }


    suspend fun isBookMarked(book: BookDetailsUiState): Boolean
    = withContext(ioDispatcher) {
        var bookMark = false
        uid = auth.currentUser?.uid.toString()
        databaseReference.child(uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    try {
                        val localList = user.toReadList

                        for (books in 0..localList.size) {

                            if (book.title == localList[books].title) {
                                Log.e("bookMarkinin", "$bookMark")
                                bookMark = true
                               return
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("isBookMarked", "Excrption : $e")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("isBookMarked", "error : $error")
                }
            })
        Log.e("bookMarklll", "$bookMark")
         bookMark
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
            }

        })
    }


    //region adding book by better way
/*   suspend fun addBookToReadList(book: BookDetailsUiState, bookNum: Int) =
   withContext(ioDispatcher) {
       uid = auth.currentUser?.uid.toString()
       databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
               for (item in snapshot.children) {
                   val user = snapshot.getValue(User::class.java)!!
                   try {
                       val localList = user.toReadList
                       localList.add(book)
                       for (item in 0..user.booksNumberInList) {
                           databaseReference.child(uid).child("toReadList").removeValue()
                           for (it in 0..localList.size) {
                               databaseReference.child(uid).child("toReadList")
                                   .child(it.toString()).setValue(
                                       localList[it]
                                   ).addOnSuccessListener {
                                       databaseReference.child(uid).child("booksNumberInList")
                                           .setValue(localList.size)
                                   }
                           }
                       }
                   } catch (e: Exception) {

                   }
               }


           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

       })
   }
*/

/*  suspend fun getNumOfBookList(): Int = withContext(ioDispatcher) {
  uid = auth.currentUser?.uid.toString()
  databaseReference.child(uid).addValueEventListener(
      object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
              // Get Post object and use the values to update the UI
              val user = dataSnapshot.getValue(User::class.java)!!
              initalValue = user.booksNumberInList
              Log.e("addingBook8DS" , "${user.booksNumberInList}")


          }

          override fun onCancelled(databaseError: DatabaseError) {
              // Getting Post failed, log a message
          }
      }
  )



//        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
  initalValue
  Log.e("addingBook9DS" , "${initalValue}")

}
*/
//endregion
}