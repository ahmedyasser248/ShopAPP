package com.example.shopapp.theFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopapp.FireStore.FirestoreClass
import com.example.shopapp.R
import com.example.shopapp.activities.SettingsActivity
import com.example.shopapp.adapters.DashboardItemListAdapter
import com.example.shopapp.models.Product
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashBoardFragment : BaseFragment() {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(doubleBackToExitPressedOnce){
                    activity?.finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(requireContext(),resources.getString(R.string.please_click_back_again),
                    Toast.LENGTH_SHORT)
                    .show()
                CoroutineScope(Dispatchers.IO).launch {
                    delay(2000)
                    doubleBackToExitPressedOnce =false

                }
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_settings -> {
                startActivity(Intent(this.activity,
                    SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun successDashboardItemList(dashItemList:ArrayList<Product>){
        hideDialog()
        if(dashItemList.size>0){
            rv_my_dashboard_items.visibility=View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE
            rv_my_dashboard_items.layoutManager = GridLayoutManager(activity,2)
            rv_my_dashboard_items.setHasFixedSize(true)
            val adapter = DashboardItemListAdapter(requireActivity(),dashItemList)
            rv_my_dashboard_items.adapter = adapter
        }
        else {

                rv_my_dashboard_items.visibility = View.GONE
                tv_no_dashboard_items_found.visibility=View.VISIBLE

        }
    }
    private fun getDashboardItemList(){
        showProgressBar(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemList(this@DashBoardFragment)

    }
}