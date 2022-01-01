package com.example.gbook.firebase

import com.example.gbook.authentication.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.cache.DiskLruCache


interface BookRealTimeDBService {

//    suspend fun insertUser(user: User)

    suspend fun getUsers () : User
}



object userApi{
   val  auth = FirebaseAuth.getInstance()
}