package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.ProductDetailActivity
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_layout_category.view.*
import kotlinx.android.synthetic.main.grid_layout_product.view.*
import kotlinx.android.synthetic.main.grid_layout_product.view.text_view_title

class RecycleViewProductListAdapter(var productListActivity : Context) : RecyclerView.Adapter<RecycleViewProductListAdapter.MyViewHolder>(){
    private var productList : ArrayList<Product> = ArrayList<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewProductListAdapter.MyViewHolder {

        var view = LayoutInflater.from(productListActivity).inflate(R.layout.grid_layout_product, parent, false)
        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return productList!!.size
    }

    override fun onBindViewHolder(holder: RecycleViewProductListAdapter.MyViewHolder, position: Int) {

        holder.bind(productList!!.get(position))
    }

    fun setData(productList : ArrayList<Product>){
        this.productList = productList;
        notifyDataSetChanged()
    }


    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        fun bind(product : Product){

            view.text_view_title.text = product.productName
            Picasso
                .get()
                .load(Config.IMAGE_URL + product.image)
                .placeholder(R.drawable.image_place_holder)
                .error(R.drawable.image_place_holder)
                .into(view.image_view_picture)
            view.text_view_price.text = "$ ${product.price.toString()}"

            view.setOnClickListener{
                var productDetailActivityIntent = Intent(view.context, ProductDetailActivity::class.java)

                productDetailActivityIntent.putExtra("product", product)

                view.context.startActivity(productDetailActivityIntent)
            }

        }




    }



}