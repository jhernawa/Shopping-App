package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.RecyclerViewCartAdapter
import com.example.shoppingapp.database.DBHelper
import com.example.shoppingapp.models.Product
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.time.temporal.TemporalAmount

class CartActivity : AppCompatActivity(), RecyclerViewCartAdapter.MyCustomInterface {

    lateinit var recyclerViewCartAdapter : RecyclerViewCartAdapter
    lateinit var productList : ArrayList<Product>
    private var orderAmount: Double = 0.0 //order amount
    private var totalMrp : Double = 0.0 //total mrp
    private var totalDiscount : Double = 0.0 //total discount
    private var delivery : Double = 0.0 //total delivery


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        init()
    }

    private fun init(){


        //set up toolbar
        setupToolbar()

        //read data from database
        var dbHelper = DBHelper(this)
        productList = dbHelper.readProduct()
        if(productList.size == 0){
            showEmptyCartMessage()
        }

        //connect to the recycler view adapter to show the employee list
        recycler_view.layoutManager = LinearLayoutManager(this)
        recyclerViewCartAdapter = RecyclerViewCartAdapter(this)
        recyclerViewCartAdapter.setMyCustomInterface(this)
        recyclerViewCartAdapter.setData(productList)

        recycler_view.adapter = recyclerViewCartAdapter

        //display order summary
        displayOrderSummary(productList)

        //proceed checkout button listener
        button_checkout.setOnClickListener{
            //to make sure the user selects one of the deliveries method
            if(delivery == 0.0){
                Toast.makeText(this, "Please select delivery method", Toast.LENGTH_SHORT).show()
            }
            else{
                startActivity(Intent(this, SelectAddressActivity::class.java))
            }

        }

        //delivery radio group button listener
        radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{group, checkedId ->
            if(checkedId == R.id.radio_button_2_hour_delivery){
                if(delivery == 0.0){
                    delivery = 5.0
                    orderAmount += delivery
                }
                else{
                    orderAmount -= 3.0
                    delivery = 5.0
                    orderAmount += delivery
                }

                text_view_order_amount_value.text = "$ ${"%.2f".format(orderAmount).toString()}"
            }
            else{
                if(delivery == 0.0){
                    delivery = 3.0
                    orderAmount += delivery
                }
                else{
                    orderAmount -= 5.0
                    delivery = 3.0
                    orderAmount += delivery
                }

                text_view_order_amount_value.text = "$ ${"%.2f".format(orderAmount).toString()}"
            }
        })
    }

    private fun setupToolbar(){
        var toolbar = myToolbar
        toolbar.title = "Shopping Cart"
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

    override fun onItemClickedIncrement(position: Int) {
        updateDisplayOrderSummaryFromDatabase(productList.get(position), true, 0)
    }

    override fun onItemClickedDecrement(position: Int, qtyDec : Int) {
        Log.i("data position", "${position}")
        Log.i("data size", "${productList.size}")
        updateDisplayOrderSummaryFromDatabase(productList.get(position), false, qtyDec)

    }

    override fun showEmptyCart() {
        showEmptyCartMessage()

    }

    override fun resetProductList(productList: ArrayList<Product>) {
        this.productList = productList
    }

    private fun showEmptyCartMessage(){
        //show the empty cart message
        layout_product_order_amount.visibility = View.GONE
        text_view_empty_cart.visibility = View.VISIBLE

    }




    private fun updateDisplayOrderSummaryFromDatabase(product : Product, isIncrement : Boolean, qty : Int){

        if(isIncrement){
            orderAmount += product.price
            totalMrp += product.mrp
            totalDiscount += (product.mrp - product.price)
        }
        else{
            orderAmount -= (product.price * qty)
            totalMrp -= (product.mrp * qty)
            totalDiscount -= ((product.mrp - product.price) * qty)


        }



        text_view_order_amount_value.text = "$ ${"%.2f".format(orderAmount).toString()}"
        text_view_total_amount.text = "$ ${"%.2f".format(totalMrp).toString()}"
        text_view_discount_amount.text = "-$ ${"%.2f".format(totalDiscount).toString()}"


    }
    private fun displayOrderSummary(productList : ArrayList<Product>){

        for(elem in productList){
            orderAmount += (elem.price * elem.quantity)
            totalMrp += (elem.mrp * elem.quantity)

        }
        totalDiscount = totalMrp - orderAmount + delivery


        text_view_order_amount_value.text = "$ ${"%.2f".format(orderAmount).toString()}"
        text_view_total_amount.text = "$ ${"%.2f".format(totalMrp).toString()}"
        text_view_discount_amount.text = "-$ ${"%.2f".format(totalDiscount).toString()}"



    }







}
