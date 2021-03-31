package com.example.shopapp.theFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopapp.R
import com.example.shopapp.UIelements.Constants
import com.example.shopapp.models.User
import kotlinx.android.synthetic.main.after_login_fragment.*
import kotlinx.android.synthetic.main.user_profile_fragment.*

class AfterLoginFragment : Fragment(R.layout.after_login_fragment) {
    lateinit var userName : String
    lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        val sharedPreferences = this.activity?.getSharedPreferences(Constants.SHOPAPP_PREFERCENCES,Context.MODE_PRIVATE)
        userName = sharedPreferences?.getString(Constants.LOGGED_IN_USERNAME,"")!!
        val bundle : Bundle? = this.arguments
        if(bundle!= null){
//            user= bundle.getParcelable("userInfo")!!
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}