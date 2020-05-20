package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.SubCategoryActivity
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_layout_category.view.*

class RecyclerViewCategoryAdapter(var mainActivity : Context) : RecyclerView.Adapter<RecyclerViewCategoryAdapter.MyViewHolder>(){

    private var categoryList : ArrayList<Category> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(mainActivity).inflate(R.layout.grid_layout_category, parent, false)
        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(categoryList.get(position))
    }

    fun setData(categoryList : ArrayList<Category>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        fun bind(category : Category){

            view.text_view_title.text = category.catName

            Picasso
                .get()
                .load(Config.IMAGE_URL +category.catImage)
                .placeholder(R.drawable.image_place_holder)
                .error(R.drawable.image_place_holder)
                .into(view.image_view)


            view.setOnClickListener{

                var subCategoryActivityIntent = Intent(mainActivity, SubCategoryActivity::class.java)

                Log.i("catId", category.catId.toString())
                subCategoryActivityIntent.putExtra("category_id", category.catId.toString())
                subCategoryActivityIntent.putExtra("category_name", category.catName)

                mainActivity.startActivity(subCategoryActivityIntent)
            }


        }


    }



}