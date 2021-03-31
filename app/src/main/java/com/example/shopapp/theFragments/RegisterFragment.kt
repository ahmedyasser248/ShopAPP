package com.example.shopapp.theFragments

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.regiser_fragment.*

class RegisterFragment : Fragment(R.layout.regiser_fragment) {
    lateinit var mProgressDialog : Dialog
    lateinit var fireStoreClass:FirestoreClass
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_register.setOnClickListener {
          registerUser()
        }
        tv_login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        fireStoreClass = FirestoreClass()

    }
    private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_first_name.text.toString().trim{it <=' '}) -> {
                makeMessageSnackBar(R.string.forget_the_first_name)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim{ it <= ' '})->{
                makeMessageSnackBar(R.string.forget_the_last_name)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim{ it <=' '})->{
                makeMessageSnackBar(R.string.forget_the_email)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim{ it <=' '})->{
                makeMessageSnackBar(R.string.forget_the_password)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim{ it<=' ' })->{
                makeMessageSnackBar(R.string.forget_the_conf_message)
                false
            }
            et_password.text.toString().trim{ it <= ' '} != et_confirm_password.text.toString().trim{ it <= ' '}->
            {
                makeMessageSnackBar(R.string.password_conf_not_match)
                false
            }
            !cb_terms_and_condition.isChecked ->{
                makeMessageSnackBar(R.string.forget_check_conditions_and_terms)
                false
            }
            else ->{

                true
            }
        }

    }
    private fun makeMessageSnackBar(message: Int){

        val snackBar = Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG)
        snackBar.show()
    }
    private fun registerUser(){
        if(validateRegisterDetails()){
           showProgressBar(resources.getString(R.string.please_wait))
            val email : String = et_email.text.toString().trim {  it <= ' ' }
            val password: String =et_password.text.toString().trim(){ it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->

                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val user = User(firebaseUser.uid,et_first_name.text.toString().trim{ it<= ' '}
                        ,et_last_name.text.toString().trim{ it<= ' '},et_email.text.toString().trim{ it<= ' '})
                            Log.i("2ndmessage","I came here")
                           fireStoreClass.registerUser(this,user)

                           findNavController().navigate(R.id.action_registerFragment_to_loginFragment)


                    } else{
                        hideDialog()
                        errorOccurred(task.exception!!.message.toString())
                    }
                })


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
    fun hideDialog(){
        mProgressDialog.dismiss()
    }
    private fun errorOccurred(message : String){
        val snackBar = Snackbar.make(requireView(),message,Snackbar.LENGTH_LONG).show()

    }
    fun userRegistrationSuccess(){
        hideDialog()
        Toast.makeText(requireContext(),resources.getString(R.string.register_success),Toast.LENGTH_SHORT).show()
    }

}