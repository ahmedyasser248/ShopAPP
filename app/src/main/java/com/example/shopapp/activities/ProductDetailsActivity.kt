package com.example.shopapp.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.models.Product
import com.example.shopapp.utils.Constants
import com.example.shopapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.dialog_progress.*

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var mProgressDialog : Dialog
    private var mProductId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getProductDetails()
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    fun productDetailsSuccess(product : Product){
        hideDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_stock_quantity.text=product.stock_quantity
        tv_product_details_price.text = "$${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_title.text=product.title

    }
    private fun getProductDetails(){
        showProgressBar(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this,mProductId)
    }

    fun showProgressBar(text : String){

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