package com.example.shopapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.utils.GlideLoader
import com.example.shopapp.models.Product
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

class DashboardItemListAdapter (private val context : Context, private var list : ArrayList<Product>)
     : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyProductListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_price.text=model.title
            holder.itemView.tv_dashboard_item_price.text="$${model.price}"

        }
    }
    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

}