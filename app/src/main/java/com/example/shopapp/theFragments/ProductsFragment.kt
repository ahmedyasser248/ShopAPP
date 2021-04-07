package com.example.shopapp.theFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.activities.AddProductActivity
import com.example.shopapp.adapters.MyProductListAdapter
import com.example.shopapp.models.Product
import kotlinx.android.synthetic.main.products_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductsFragment: BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_products_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.products_fragment, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id  = item.itemId
        when(id){
            R.id.add_menu_item ->{
                startActivity(Intent(this.activity,AddProductActivity::class.java ))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun successProductsListFromFireStore(productList: ArrayList<Product>){
        hideDialog()
        if(productList.size > 0){
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE
            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = MyProductListAdapter(requireActivity(),productList)
            rv_my_product_items.adapter = adapterProducts
        }else{
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {
        showProgressBar(resources.getString(R.string.please_wait))
        FirestoreClass().getProductList(this)
    }
}