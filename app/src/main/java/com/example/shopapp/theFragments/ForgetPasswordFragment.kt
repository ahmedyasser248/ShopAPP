package com.example.shopapp.theFragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.forget_password_fragment.*

class ForgetPasswordFragment : Fragment(R.layout.forget_password_fragment) {
   lateinit var mProgressDialog : Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            val email = et_email.text.toString().trim{ it <= ' ' }
            if(email.isEmpty()){
                makeMessageSnackBar(R.string.forget_the_email)
            }else{
                showProgressBar(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                        task ->
                    hideDialog()
                    if(task.isSuccessful){
                        makeMessageSnackBar(R.string.email_sent_success)
                        findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                    }else{
                        errorOccurred(task.exception!!.message.toString())
                    }
                }
            }
        }
    }

    private fun makeMessageSnackBar(message: Int){
        val snackBar = Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }
    private fun errorOccurred(message : String){
        val snackBar = Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG).show()

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
}