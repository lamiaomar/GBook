package com.example.gbook.firebase

import com.example.gbook.authentication.User


interface BookRealTimeDBService {

    suspend fun insertUser(user : User)

}

