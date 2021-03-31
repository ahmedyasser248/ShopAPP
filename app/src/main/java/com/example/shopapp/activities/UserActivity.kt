package com.example.shopapp.activities

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.shopapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this@UserActivity,
            R.drawable.app_gradient_color_background
        ))
        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)

    }


    private fun showBottomNav() {
        nav_view.visibility = View.VISIBLE

    }
    private fun hideActionBar(){
       supportActionBar?.hide()
    }
    private  fun showActionBar(){
        supportActionBar?.show()
    }

    private fun hideBottomNav() {
        nav_view.visibility = View.GONE

    }
}