package com.example.shopapp.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopapp.R

open class MainActivity : AppCompatActivity() {
   lateinit var mprogressDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mprogressDialog = Dialog(this)
    }


}