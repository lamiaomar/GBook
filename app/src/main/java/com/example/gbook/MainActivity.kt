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
import com.example.gbook.fragments.BookListFragment
import com.example.gbook.fragments.BookListFragmentDirections
import com.example.gbook.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val sharedViewModel: BookViewmodel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)


        val homePage = BookListFragment()
        val searchPage = SearchFragment()

//        makeCerrentFragment(homePage)
//        holder.bookThumb.setOnClickListener {
//
//            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailsFragment(position,4)
//            holder.bookThumb.findNavController().navigate(action)
//
//        }
//        bottom_navigation.findNavController().navigate()
//
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {makeCerrentFragment(homePage)
                homePage.onDestroy()
                }
                R.id.ic_search -> makeCerrentFragment(searchPage)

            }
            true
        }

    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        menuInflater.inflate(R.menu.nav_menu, menu)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        val id = item.itemId
//        when (id) {
//            R.id.ic_home -> {
//            }
//
//        }
//
//        return super.onOptionsItemSelected(item)
//
//    }

    private fun makeCerrentFragment(frament: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_warper, frament)
            commit()
        }
}