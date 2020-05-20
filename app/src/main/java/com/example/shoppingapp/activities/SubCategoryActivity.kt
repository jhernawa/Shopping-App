package com.example.shoppingapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.SubCategoryAdapter
import com.example.shoppingapp.app.Endpoints
import com.example.shoppingapp.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class SubCategoryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        init()
    }

    private fun init(){

        //displaying the subcategory
        var categoryClicked : String? = intent.extras?.getString("category_id").toString()
        var categoryName : String? = intent.extras?.getString("category_name").toString()
        Log.i("categoryClicked", categoryClicked.toString())

        //set up toolbar
        setupToolbar(categoryName)


        //get subcategory from network
        getSubcategory(categoryClicked)
    }

    private fun getSubcategory(categoryClicked : String?){


        var url = Endpoints.getSubCategory(categoryClicked)
        //var url = "https://apolis-grocery.herokuapp.com/api/subcategory/$categoryClicked"
        Log.i("first url", url.toString())
        var requestQueue = Volley.newRequestQueue(this) //create volley and request queue

        var stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener {
                //parse the json object before displaying it via adapter
                Log.i("dataSubcategory", it.toString())
                var gson = GsonBuilder().create()
                var subCategoryResponse = gson.fromJson(it.toString(), SubCategoryResponse::class.java)
                var subCategoryList : ArrayList<SubCategory> = subCategoryResponse.data

                //set subCategoryList onto the adapter so that title could be displayed
                var subCategoryAdapter = SubCategoryAdapter(supportFragmentManager);
                subCategoryAdapter.setSubCategory(subCategoryList)

                //add product fragment to show from each subcategory
                for(i in 0..subCategoryList.size-1){

                    Log.i("subId", subCategoryList.get(i).subId.toString())
                    subCategoryAdapter.addFragment(subCategoryList.get(i).subId)


                }

                progress_bar.visibility = View.GONE

                //bind category data onto the tab layout and view pager
                view_pager_layout.adapter = subCategoryAdapter
                tab_layout.setupWithViewPager(view_pager_layout)

            },
            Response.ErrorListener {
                Log.e("data", it.message)
            })

        requestQueue.add(stringRequest)

    }

    private fun setupToolbar(categoryClicked: String?){

        var toolbar = myToolbar
        toolbar.title = categoryClicked
        //toolbar.subtitle ="Sub Category"
        setSupportActionBar(toolbar)

        //create the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            //handle the back button
            android.R.id.home -> {
                finish()
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
