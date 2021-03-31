package com.example.shopapp.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.UIelements.Constants
import com.example.shopapp.UIelements.GlideLoader
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.user_profile_fragment.*
import java.io.IOException
import java.lang.Exception
import java.util.jar.Manifest

class AddProductActivity : AppCompatActivity() ,View.OnClickListener{
   private var mSelectedImageFileURI: Uri? = null
    private lateinit var mProgressDialog: Dialog
    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_update_product -> {
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission
                            .READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChooser(this@AddProductActivity)
                    }
                    else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )

                    }
                }
                R.id.btn_submit_add_product -> {
                    if(validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar =supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Constants.showImageChooser(this@AddProductActivity)
            } else{
                Toast.makeText(this , resources.getString(R.string.read_storage_permission_denied),
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
                   iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                    try{
                        mSelectedImageFileURI = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!,iv_product_image)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileURI == null -> {
                makeSnackBarMessage(resources.getString(R.string.err_msg_select_product_image))
                false
            }

            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) -> {
                makeSnackBarMessage(resources.getString(R.string.err_msg_enter_product_title))
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) -> {
                makeSnackBarMessage(resources.getString(R.string.err_msg_enter_product_price))
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) -> {
                makeSnackBarMessage(
                    resources.getString(R.string.err_msg_enter_product_description)
                )
                false
            }

            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it <= ' ' }) -> {
                makeSnackBarMessage(
                    resources.getString(R.string.err_msg_enter_product_quantity)
                )
                false
            }
            else -> {
                true
            }
        }
    }
    private fun makeSnackBarMessage(message : String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG).show()
    }
    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileURI, Constants.PRODUCT_IMAGE)
    }
    fun imageUploadSuccess(imageURL : String){
        hideDialog()
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
}