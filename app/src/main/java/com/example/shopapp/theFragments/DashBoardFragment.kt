package com.example.shopapp.theFragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.shopapp.R
import com.example.shopapp.activities.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashBoardFragment : Fragment(R.layout.dashboard_fragment) {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(doubleBackToExitPressedOnce){
                    activity?.finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(requireContext(),resources.getString(R.string.please_click_back_again),
                    Toast.LENGTH_SHORT)
                    .show()
                CoroutineScope(Dispatchers.IO).launch {
                    delay(2000)
                    doubleBackToExitPressedOnce =false

                }
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_settings -> {
                startActivity(Intent(this.activity,
                    SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}