package com.example.shopapp.theFragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.activities.UserActivity
import com.example.shopapp.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment  : Fragment(R.layout.login_fragment) ,View.OnClickListener{
    lateinit var mProgressDialog : Dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v != null)
            when(v.id){
                R.id.tv_forgot_password ->{
                    findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
                }
                R.id.btn_login ->{
                    loginRegisterUser()

                }
                R.id.tv_register ->{
                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                }
            }
    }
    fun showProgressBar(text : String){

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text =text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

    }
    private fun hideDialog(){
        mProgressDialog.dismiss()
    }
    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim{ it <= ' '}) ->{
                makeMessageSnackBar(R.string.forget_the_email)
                false
            }
                TextUtils.isEmpty(et_password.text.toString().trim{ it <= ' '}) -> {
                    makeMessageSnackBar(R.string.forget_the_password)
                    false
                }
            else -> {
                true
            }
        }
    }
    private fun makeMessageSnackBar(message: Int){
        val snackBar = Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }
    private fun loginRegisterUser(){
        if(validateLoginDetails()){
            showProgressBar(resources.getString(R.string.please_wait))

            val email = et_email.text.toString().trim{ it<= ' '}
            val password = et_password.text.toString().trim{ it<= ' '}
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener{
                task ->

                if(task.isSuccessful){
                        FirestoreClass().getUserDetails(this@LoginFragment)
                }else{
                    hideDialog()
                   errorOccurred(task.exception!!.message.toString())
                }
            }


        }
    }
    private fun errorOccurred(message : String){
        val snackBar = Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG).show()
    }
    fun userLoggedInSuccess(user:User){
        hideDialog()
        val bundle = Bundle().apply {
            putParcelable("userInfo",user)
        }
        if(user.profileCompleted == 0){
            findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment,bundle)
        }else {
            activity?.startActivity(Intent(requireActivity(),
                UserActivity::class.java))
            activity?.finish()
        }
    }

}