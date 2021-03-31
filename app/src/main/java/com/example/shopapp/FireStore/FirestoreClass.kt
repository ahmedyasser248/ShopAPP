package com.example.shopapp.FireStore

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopapp.activities.SettingsActivity
import com.example.shopapp.UIelements.Constants
import com.example.shopapp.activities.AddProductActivity
import com.example.shopapp.models.User
import com.example.shopapp.theFragments.LoginFragment
import com.example.shopapp.theFragments.RegisterFragment
import com.example.shopapp.theFragments.UserProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()
        fun registerUser(fragmentActivity: RegisterFragment,userInfo : User){
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                fragmentActivity.userRegistrationSuccess()

            }.addOnFailureListener {

                fragmentActivity.hideDialog()
                Log.i("error in registeration",it.message.toString())
            }
            Log.i("3rdMessage","anything happen")
    }
    fun getCurrentUserID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID=""
        if(currentUser!=null){
            currentUserID=currentUser.uid
        }
        return currentUserID
    }
    fun getUserDetails(fragment : Fragment){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i("4th message","got here")
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =  fragment.activity?.getSharedPreferences(
                    Constants.SHOPAPP_PREFERCENCES,
                    Context.MODE_PRIVATE
                )
                val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME,"${user.firstName} ${user.lastName}")
                editor.apply()

                when(fragment){
                   is LoginFragment-> {
                        fragment.userLoggedInSuccess(user!!)
                    }
                }
            }.addOnFailureListener {e ->
                Log.i("couldn't login",e.toString())

            }
    }
    fun updateUserProfileData(fragment: Fragment,userHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(fragment){
                    is UserProfileFragment -> {
                        fragment.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {
                e ->
                when(fragment){
                    is UserProfileFragment ->{
                        fragment.hideDialog()
                    }
                }
                Log.e("Error while updating the user details",e.toString())
            }
    }
    fun uploadImageToCloudStorage(fragment :Fragment ,imageFileURI : Uri? ,imageType:String){

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(Constants.USER_PROFILE_IMAGE
                + System.currentTimeMillis() +"."+
            Constants.getFileExtension(fragment, imageFileURI)
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener {
            taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri -> Log.e("Downloadable image url", uri.toString())
                when(fragment){
                    is UserProfileFragment -> {
                        fragment.imageUploadSuccess(uri.toString())
                    }

                }

            }
        }
            .addOnFailureListener {exception ->
                when(fragment){
                    is UserProfileFragment -> {
                        fragment.hideDialog()
                    }
                }
                Log.e("error in uploadImageToCloud",exception.message,exception)
            }
    }
    fun uploadImageToCloudStorage(activity :AppCompatActivity ,imageFileURI : Uri? ,imageType:String){

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(Constants.USER_PROFILE_IMAGE
                + System.currentTimeMillis() +"."+
                Constants.getFileExtension(activity, imageFileURI)
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener {
                taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri -> Log.e("Downloadable image url", uri.toString())
                when(activity){
                    is AddProductActivity ->{
                        activity.imageUploadSuccess(uri!!.toString())
                    }
                }

            }
        }
            .addOnFailureListener {exception ->
                when(activity){
                    is AddProductActivity -> {
                        activity.hideDialog()
                    }
                }
                Log.e("error in uploadImageToCloud",exception.message,exception)
            }
    }
    fun getUserDetails(activity: AppCompatActivity ){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i("4th message","got here")
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =  activity.getSharedPreferences(
                    Constants.SHOPAPP_PREFERCENCES,
                    Context.MODE_PRIVATE
                )
                val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME,"${user.firstName} ${user.lastName}")
                editor.apply()

                when(activity){
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }.addOnFailureListener {e ->
                when(activity){
                    is SettingsActivity ->{
                        activity.hideDialog()
                    }
                }
                Log.i("couldn't login",e.toString())

            }
    }
}


