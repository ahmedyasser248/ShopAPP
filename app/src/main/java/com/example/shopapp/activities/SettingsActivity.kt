package com.example.shopapp.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.UIelements.GlideLoader
import com.example.shopapp.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.dialog_progress.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var mUserDetails : User
    override fun onClick(p0: View?) {
        if(p0 !=null){
            when(p0.id){
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit ->{

                }
            }
        }
    }

    lateinit var mProgressDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
        }
        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }





    fun showProgressDialog(text : String){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text =text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

    }
     fun hideDialog(){
        mProgressDialog.dismiss()
    }
    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }
    fun userDetailsSuccess(user : User){
        mUserDetails = user
        hideDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}