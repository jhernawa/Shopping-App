package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.ProductDetailActivity
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.database.DBHelper
import com.example.shoppingapp.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.view.*
import kotlinx.android.synthetic.main.grid_layout_cart.view.*
import kotlinx.android.synthetic.main.grid_layout_cart.view.button_minus
import kotlinx.android.synthetic.main.grid_layout_cart.view.button_plus
import kotlinx.android.synthetic.main.grid_layout_cart.view.text_view_mrp
import kotlinx.android.synthetic.main.grid_layout_cart.view.text_view_quantity
import kotlinx.android.synthetic.main.grid_layout_cart.view.text_view_save
import kotlinx.android.synthetic.main.grid_layout_product.view.*
import kotlinx.android.synthetic.main.grid_layout_product.view.image_view_picture
import kotlinx.android.synthetic.main.grid_layout_product.view.text_view_price
import kotlinx.android.synthetic.main.grid_layout_product.view.text_view_title

class RecyclerViewCartAdapter(var cartActivity : Context) : RecyclerView.Adapter<RecyclerViewCartAdapter.MyViewHolder>(){

    private var productList : ArrayList<Product> = ArrayList<Product>()
    private var listener : MyCustomInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewCartAdapter.MyViewHolder {

        var view = LayoutInflater.from(cartActivity).inflate(R.layout.grid_layout_cart, parent, false)
        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return productList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerViewCartAdapter.MyViewHolder, position: Int) {

        holder.bind(productList!!.get(position))

    }

    fun setData(productList : ArrayList<Product>){
        this.productList = productList;
        notifyDataSetChanged()
    }

    fun deleteItem(holder: RecyclerViewCartAdapter.MyViewHolder, position: Int) {
        Log.i("data position rv", "${position}")
        Log.i("data size rv", "${productList.size}")
        productList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, productList.size)
        holder.itemView.visibility = View.GONE
    }

    interface MyCustomInterface{
        fun onItemClickedIncrement(position : Int)
        fun onItemClickedDecrement(position : Int, qtyDec : Int)
        fun resetProductList(productList : ArrayList<Product>)
        fun showEmptyCart()

    }

    fun setMyCustomInterface(listener : MyCustomInterface){
        this.listener = listener
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
            view.text_view_mrp.text = "$ ${product.mrp.toString()}"

            view.text_view_quantity.text = product.quantity.toString()
            view.text_view_save.text = "Save $ ${"%.2f".format(product.mrp - product.price).toString()}"


            var dbHelper : DBHelper = DBHelper(cartActivity)
            var currQty = product.quantity
            view.button_plus.setOnClickListener{
                currQty++
                view.text_view_quantity.text = currQty.toString()

                //update the database
                dbHelper.updateProduct(product , true)

                //inform the cart activity to update the order amount
                var position = adapterPosition
                listener?.onItemClickedIncrement(position)
            }

            view.button_minus.setOnClickListener{

                if(currQty > 0 ){
                    currQty--
                    view.text_view_quantity.text = currQty.toString()

                    //update the database
                    dbHelper.updateProduct(product, false)

                    //inform the cart activity to update the order amount
                    var position = adapterPosition

                    //if item is removed from database, we need to tell the cart activity to remove
                    //this product from its array list


                    listener?.onItemClickedDecrement(position, 1)
                }
                if (currQty == 0){
                    var position = adapterPosition

                    //remove item from the recycler view

                    deleteItem(this, position)
                    listener?.resetProductList(productList)

                    //remove product from the database
                    dbHelper.deleteProduct(product)

                }

                //if no more product to show, show empty cart
                if(productList.size == 0){
                    listener?.showEmptyCart()
                }


            }


            view.image_view_remove.setOnClickListener{


                var position = adapterPosition


                var qty = dbHelper.getQuantity(product)


                //inform the cart activity to update the order amount
                listener?.onItemClickedDecrement(position, qty)

                listener?.resetProductList(productList)


                //remove item from the recycler view
                deleteItem(this, position)


                //remove product from the database
                dbHelper.deleteProduct(product)

                //if no more product to show, show empty cart
                if(productList.size == 0){
                    listener?.showEmptyCart()
                }

            }

        }


    }



}