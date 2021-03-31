package com.example.shopapp.theFragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.UIelements.Constants
import com.example.shopapp.UIelements.GlideLoader
import com.example.shopapp.models.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.after_login_fragment.*
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.user_profile_fragment.*
import java.io.IOException
import java.util.jar.Manifest

class UserProfileFragment:Fragment(R.layout.user_profile_fragment),View.OnClickListener {
    lateinit var mProgressDialog : Dialog
    lateinit var user : User
    private var mSelectedImageFileUri : Uri? =null
    private var mUserProfileImageURL : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        if(bundle!= null){
            user= bundle.getParcelable("userInfo")!!
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onClick(p0: View?) {
        if(p0!=null){
            when(p0.id){
                R.id.iv_user_photo ->{
                  if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)
                  == PackageManager.PERMISSION_GRANTED)  {
                      Constants.showImageChooser(this@UserProfileFragment)
                  }
                    else{
                      ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                      Constants.READ_STORAGE_PERMISSION_CODE)
                  }
                }
                R.id.btn_save -> {


                    if(validateUserProfileDetails()){
                        showProgressBar(resources.getString(R.string.please_wait))
                        if (mSelectedImageFileUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE)
                        }else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_first_name.isEnabled = false
        et_first_name.setText(user.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(user.lastName)

        et_email.isEnabled = false
        et_email.setText(user.email)
        iv_user_photo.setOnClickListener(this)
        btn_save.setOnClickListener(this)
    }
    private fun makeSnackBar(message : String){
        val snackBar = Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG)
        snackBar.show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeSnackBar("i reached here")
                Constants.showImageChooser(this@UserProfileFragment)
            } else{
                Toast.makeText(requireContext() , resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("got here","didn't get here")
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if(data != null){
                    try{
                        makeSnackBar("MAKE IT")
                        mSelectedImageFileUri = data.data!!
                       GlideLoader(requireContext()).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
                    }catch (e : IOException){
                        e.printStackTrace()
                        Toast.makeText(requireContext(),resources.getString(R.string.image_selection_failed),Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }else{
                makeSnackBar("error in request")
            }
        }else{
            makeSnackBar("error in result code")
        }
    }
    private fun validateUserProfileDetails(): Boolean{
        return when{
            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                makeSnackBar("please enter mobile number")
                false
            }
            else ->{
                true
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
    fun hideDialog(){
        mProgressDialog.dismiss()
    }
    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String,Any>()
        val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }
        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if(mUserProfileImageURL!!.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL!!
        }
        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        FirestoreClass().updateUserProfileData(this, userHashMap)

    }
    fun userProfileUpdateSuccess(){
        hideDialog()
        val bundle=Bundle().apply {
            putParcelable("userInfo",user)
        }
        findNavController().navigate(R.id.action_userProfileFragment_to_afterLoginFragment,bundle)
    }
    fun imageUploadSuccess(imageUrl : String){
        mUserProfileImageURL = imageUrl
        updateUserProfileDetails()
    }
}