package com.example.shopapp.UIelements

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object Constants {
    const val USERS : String = "users"
    const val SHOPAPP_PREFERCENCES : String = "ShopAppPrefs"
    const val LOGGED_IN_USERNAME : String = "logged_in_username"
    const val EXTRA_USER_DETAILS :String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MALE : String ="male"
    const val FEMALE: String = "female"
    const val MOBILE : String = "mobile"
    const val GENDER : String = "gender"
    const val USER_PROFILE_IMAGE :String = "User_Profile_Image"
    const val IMAGE : String = "image"
    const val COMPLETE_PROFILE :String = "profileCompleted"
    const val PRODUCT_IMAGE : String = "Product_Image"
    fun showImageChooser(fragment: Fragment){
        Log.i("in constants ", " i made it to here")
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fragment.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun showImageChooser(activity: AppCompatActivity){
        Log.i("in constants ", " i made it to here")
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(fragment : Fragment , uri: Uri?) : String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fragment.activity?.contentResolver?.getType(uri!!))
    }
    fun getFileExtension(activity : AppCompatActivity , uri: Uri?) : String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity?.contentResolver?.getType(uri!!))
    }
}