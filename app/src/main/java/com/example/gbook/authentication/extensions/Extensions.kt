package com.example.gbook.authentication.extensions

import android.app.Activity
import android.widget.Toast


/*
*To avoid repetition use Extension function
 */
object Extensions {
    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}