package com.example.gbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.gbook.fragments.AuthenticationFragment
import com.example.gbook.fragments.BookListFragment
import com.example.gbook.fragments.BookListFragmentDirections
import com.example.gbook.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homePage = BookListFragment()
        val searchPage = SearchFragment()
        val profilePage = AuthenticationFragment()


//        bottom_navigation.setOnItemSelectedListener{ item ->
//
//            when (item.itemId) {
//                R.id.ic_home -> {
//                    val action = BookListFragmentDirections.actionBookListFragmentToSearchFragment()
//                    bottom_navigation.findNavController().navigate(action)
////                    makeCerrentFragment(homePage)
//
////                    homePage.onDestroy()
//                }
//                R.id.ic_search -> {
////                    val action = BookListFragmentDirections.actionBookListFragmentToSearchFragment()
////                    bottom_navigation.findNavController().navigate(action)
////                    makeCerrentFragment(searchPage)
//                }
//                R.id.ic_profile ->{
////                    makeCerrentFragment(profilePage)
//
//                }
//            }
//            true
//        }
//
//    }
//
////    private fun makeCerrentFragment(frament: Fragment) {
//////        val action = BookListFragmentDirections.actionBookListFragmentToSearchFragment()
//////        bottom_navigation.findNavController().navigate(action)
////
////
////        supportFragmentManager.beginTransaction().apply {
////            replace(R.id.nav_host_fragment, frament)
////            commit()
////        }
////
////    }
    }
}
//buttom app bar with navigation host