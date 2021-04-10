package com.example.shopapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.utils.GlideLoader
import com.example.shopapp.activities.ProductDetailsActivity
import com.example.shopapp.models.Product
import com.example.shopapp.theFragments.ProductsFragment
import com.example.shopapp.utils.Constants
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyProductListAdapter(private val context : Context,
                                private var list : ArrayList<Product>,
                                private val fragment: ProductsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_layout,
                parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text=model.title
            holder.itemView.tv_item_price.text="$${model.price}"
            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context,ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                context.startActivity(intent)
            }

        }
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}