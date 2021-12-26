package com.example.gbook.authentication

data class User(
    var firstName : String? = "",
    var lastName : String? = "",
    var day : String? = "",
    var month : String? = "",
    var year : String? = "",
    var email : String? = "" ,
    var userLists : List<String>? = listOf()
){}

data class UserList(
    var link : String = "nono"
){}
