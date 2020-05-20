package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.denzcoskun.imageslider.models.SlideModel
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.RecyclerViewCategoryAdapter
import com.example.shoppingapp.adapters.RecyclerViewRecipeOfTheWeekAdapter
import com.example.shoppingapp.app.Endpoints
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.models.CategoryResponse
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class MainActivity : AppCompatActivity(), RecyclerViewRecipeOfTheWeekAdapter.MyCustomInterface {

    private lateinit var recyclerViewCategoryAdapter : RecyclerViewCategoryAdapter

    private var recipeOfTheWeek = ArrayList<Recipe>()
    private var doubleBackPressed : Boolean = false;
    private var isOnWebView : Boolean = false;

    private lateinit var auth : FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //checked if user is logged in
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        init()
    }

    private fun init(){

        //create toolbar
        setupToolbar()

        //create image slider
        var imagesDrawable : ArrayList<SlideModel> = ArrayList()
        imagesDrawable.add(SlideModel(R.drawable.chicken_thigh, "Poultry"))
        imagesDrawable.add(SlideModel(R.drawable.cheese_and_grapes, "Cheese and Grapes"))
        imagesDrawable.add(SlideModel(R.drawable.quinoa_salad, "Quinoa Salad"))
        imagesDrawable.add(SlideModel(R.drawable.fresh_vegetables, "Fresh Vegetables"))


        image_slider_banner.setImageList(imagesDrawable, true) //crop and scale the image to fit into the center


        //bind category data onto to the recycler view
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recyclerViewCategoryAdapter = RecyclerViewCategoryAdapter(this)
        recycler_view.adapter = recyclerViewCategoryAdapter

        //get category from network
        getCategory()

        //get the recipe of the week
        getRecipeOfTheWeek()




    }

    private fun getCategory(){

        var url = Endpoints.getCategory()
        var requestQueue = Volley.newRequestQueue(this) //create volley and request queue

        var stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener {
                //parse the json object before displaying it via adapter
                Log.i("data", it.toString())
                var gson = GsonBuilder().create()
                var categoryResponse = gson.fromJson(it.toString(), CategoryResponse::class.java)
                var categoryList : ArrayList<Category> = categoryResponse.data

                //create asynchronous call to notify the adapter to update the layout
                progress_bar.visibility = View.GONE
                recyclerViewCategoryAdapter.setData(categoryList)


            },
            Response.ErrorListener {
                Log.e("data", it.message)
            })

        requestQueue.add(stringRequest)


    }

    private fun getRecipeOfTheWeek(){
        var recyclerViewRecipeOfTheWeekAdapter  = RecyclerViewRecipeOfTheWeekAdapter(this)
        recycler_view_recipe_of_the_week.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_recipe_of_the_week.adapter = recyclerViewRecipeOfTheWeekAdapter
        recyclerViewRecipeOfTheWeekAdapter.setMyCustomInterface(this)

        //create recipe of the week (5 hard-coded recipes)-roast-chicken/
        recipeOfTheWeek.add(Recipe("https://assets.bonappetit.com/photos/57acd8a31b3340441497524d/16:9/w_940,c_limit/classic-potato-gratin.jpg", "Classic Potato Gratin", "60-75 min", "https://www.bonappetit.com/recipe/classic-potato-gratin"))
        recipeOfTheWeek.add(Recipe("https://www.eatwell101.com/wp-content/uploads/2019/06/Garlic-Butter-Salmon-with-Lemon-Asparagus-Skillet.jpg", "Garlic Butter Salmon with Lemon Asparagus", "30 min", "https://www.eatwell101.com/garlic-butter-salmon-lemon-asparagus-skillet-recipe"))
        recipeOfTheWeek.add(Recipe("https://assets.bonappetit.com/photos/57acf5bd1b334044149753ab/16:9/w_1880,c_limit/your-new-favorite-pork-chops.jpg", "Cast-Iron Skillet Pork Chops", "25 min", "https://www.bonappetit.com/recipe/new-favorite-pork-chops"))
        recipeOfTheWeek.add(Recipe("https://assets.bonappetit.com/photos/5e4c58a6cf2f580008eba058/16:9/w_940,c_limit/HLY_Vegan%20Alfredo_16x9.jpg", "Pantry Pasta With Vegan Cream Sauce", "35 min", "https://www.bonappetit.com/recipe/pantry-pasta-with-vegan-cream-sauce"))
        //notify the adapter
        recyclerViewRecipeOfTheWeekAdapter.setData(recipeOfTheWeek)
    }

    override fun onItemClicked(position: Int) {

        //make the web view visible
        web_view.visibility = View.VISIBLE

        //load the recipe url to the web view
        var recipe = recipeOfTheWeek.get(position)

        isOnWebView = true
        web_view.webViewClient = WebViewClient() //open within app
        web_view.loadUrl(recipe.recipeLink) //open in external browser if no previous line of code


    }

    override fun onBackPressed() {
        if(web_view.canGoBack()){
            web_view.goBack()
        }
        else{
            if(!isOnWebView){
                finish()
                super.onBackPressed()

            }
            else if(!doubleBackPressed && isOnWebView){
                doubleBackPressed = true
                isOnWebView = false
                finish()
                startActivity(intent)
            }
            else{
                finish()
                super.onBackPressed()
            }

        }

    }



    private fun setupToolbar(){
        var toolbar = myToolbar
        toolbar.title = "Category"
        //toolbar.subtitle ="Sub Category"
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                return true
            }
            R.id.menu_person -> {
                Toast.makeText(this, "Parking Clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Order Clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_logout -> {
                var auth = FirebaseAuth.getInstance()
                auth.signOut()

                //redirect the user to the login activity
                startActivity(Intent(this, LoginActivity::class.java))

                return true
            }

        }
        return true
    }

}
