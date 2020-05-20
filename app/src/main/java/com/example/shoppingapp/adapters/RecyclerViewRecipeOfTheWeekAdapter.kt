package com.example.shoppingapp.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_layout_category.view.*
import kotlinx.android.synthetic.main.grid_layout_category.view.image_view
import kotlinx.android.synthetic.main.grid_layout_category.view.text_view_title
import kotlinx.android.synthetic.main.grid_layout_recipe_of_the_week.view.*

class RecyclerViewRecipeOfTheWeekAdapter(var mainActivity : Context) : RecyclerView.Adapter<RecyclerViewRecipeOfTheWeekAdapter.MyViewHolder>(){

    private var recipeOfTheWeekList : ArrayList<Recipe> = ArrayList()
    private var listener : MyCustomInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(mainActivity).inflate(R.layout.grid_layout_recipe_of_the_week, parent, false)
        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return recipeOfTheWeekList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(recipeOfTheWeekList.get(position))
    }

    fun setData(recipeOfTheWeekList : ArrayList<Recipe>){
        this.recipeOfTheWeekList = recipeOfTheWeekList
        notifyDataSetChanged()
    }

    interface MyCustomInterface{
        fun onItemClicked(position : Int)

    }

    fun setMyCustomInterface(listener : MyCustomInterface){
        this.listener = listener
    }

    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        fun bind(recipe : Recipe){

            view.text_view_title.text = recipe.title

            Picasso
                .get()
                .load(recipe.image)
                .placeholder(R.drawable.image_place_holder)
                .error(R.drawable.image_place_holder)
                .into(view.image_view)

            view.text_view_duration.text = recipe.duration

            view.setOnClickListener{

                var position = adapterPosition
                listener?.onItemClicked(position)

            }


        }


    }



}