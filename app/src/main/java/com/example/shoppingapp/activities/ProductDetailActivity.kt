package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.RecyclerViewCartAdapter
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.database.DBHelper
import com.example.shoppingapp.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.grid_layout_product.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var product : Product
    private var quantity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        init()
    }

    private fun init(){

        //set up toolbar
        setupToolbar()

        product = intent.extras?.getSerializable("product") as Product



        Picasso
            .get()
            .load(Config.IMAGE_URL + product.image)
            .placeholder(R.drawable.image_place_holder)
            .error(R.drawable.image_place_holder)
            .into(image_view_picture)

        text_view_title.text = product.productName
        text_view_description.text = product.description
        text_view_price.text = "$ ${"%.2f".format(product.price).toString()}"
        text_view_mrp.text = "$ ${"%.2f".format(product.mrp).toString()}"
        text_view_save.text = "Save $ ${"%.2f".format(product.mrp - product.price).toString()}"
        text_view_percentage.text = "${ "%.0f".format(((product.mrp - product.price).toDouble() / product.mrp.toDouble())*100).toString() }%"
        /* Check if the database has already got the product.
           Show the plus minus button if it exists in database.
           If database does not have the product, you directly add the product to the cart */
        var dbHelper = DBHelper(this)
        if(dbHelper.isIdExist(product)){
            /* make the add to cart button invisible */
            button_add_to_cart.visibility = View.GONE

            /*make the layout_plus_minus visible*/
            layout_increment_decrement.visibility = View.VISIBLE
            //update the quantity accordingly based on the plus and minus button (text view is independent)
            quantity = dbHelper.getQuantity(product)
            text_view_quantity.text = quantity.toString()
            button_plus.setOnClickListener{
                //update text view
                quantity++
                text_view_quantity.text = quantity.toString()

                //update the database
                dbHelper.updateProduct(product, true)
            }
            button_minus.setOnClickListener{
                //update text view
                if(quantity > 0){
                    //update text view
                    quantity--
                    text_view_quantity.text = quantity.toString()

                    //update the database
                    dbHelper.updateProduct(product, false)
                }

                //hides the layout_increment_decrement
                if(quantity == 0){
                    //delete product from database
                    dbHelper.deleteProduct(product)

                    //update visibility of button
                    layout_increment_decrement.visibility = View.GONE
                    button_add_to_cart.visibility = View.VISIBLE


                }

            }

        }
        button_add_to_cart.setOnClickListener{
            //hide the add to cart button
            button_add_to_cart.visibility = View.GONE

            //display the plus minus button
            layout_increment_decrement.visibility = View.VISIBLE

            //create new entry into the table
            dbHelper.createProduct(product)

            quantity = dbHelper.getQuantity(product)
            text_view_quantity.text = quantity.toString()
            button_plus.setOnClickListener{
                //update the text view
                quantity++
                text_view_quantity.text = quantity.toString()

                //update the database
                dbHelper.updateProduct(product, true)
            }

            button_minus.setOnClickListener{
                //update text view
                if(quantity > 0){
                    //update text view
                    quantity--
                    text_view_quantity.text = quantity.toString()

                    //update the database
                    dbHelper.updateProduct(product, false)
                }

                //hides the layout_increment_decrement
                if(quantity == 0){
                    //delete product from database
                    dbHelper.deleteProduct(product)

                    //update visibility of button
                    layout_increment_decrement.visibility = View.GONE
                    button_add_to_cart.visibility = View.VISIBLE
                }
            }





        }



    }

    override fun onRestart() {
        super.onRestart()

        var dbHelper = DBHelper(this)
        quantity = dbHelper.getQuantity(product)
        text_view_quantity.text = quantity.toString()

        if(quantity == 0){
            //hide the add to cart button
            button_add_to_cart.visibility = View.VISIBLE

            //display the plus minus button
            layout_increment_decrement.visibility = View.GONE
        }

    }



    private fun setupToolbar(){
        var toolbar = myToolbar
        toolbar.title = "Product Detail"
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
