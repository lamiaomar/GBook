package com.example.gbook.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gbook.authentication.User
import com.example.gbook.databinding.FragmentBookShelfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookShelfFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    //private lateinit var bookShelfArray : Array<String?>
    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var binding: FragmentBookShelfBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookShelfBinding.inflate(inflater)

        binding.lifecycleOwner = this

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")


        if (uid.isNotEmpty()) {
            getUserData()
        } else {
            Toast.makeText(this.context, "uid is empty", Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }

    private fun getUserData() {
        try {
//            databaseReference.addChildEventListener({
//
//            })
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (post in snapshot.children) {
                        var x = 0
                        user = snapshot.getValue(User::class.java)!!
                        for (item in user.userLists!! ){
                        Log.e("list", "${user.userLists?.get(x)}")
                        ++x
                        }
                        //                        binding.userName.setText(user.userLists?.[item.toInt()])
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this.context, "uid1 is empty", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }


}

