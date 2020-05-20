package com.example.shoppingapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.RecyclerViewSelectAddressAdapter
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_select_address.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class SelectAddressActivity : AppCompatActivity() {

    lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)

        databaseReference = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DATABASE_NAME)

        init()
    }

    private fun init() {

        //set toolbar
        setupToolbar()

        button_add_address.setOnClickListener{
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

        //connect to recycleViewProductListAdapter to show Address
        var recyclerViewSelectAddressAdapter : RecyclerViewSelectAddressAdapter = RecyclerViewSelectAddressAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = recyclerViewSelectAddressAdapter

        //** get data from firebase **/
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            //dataSnapshot is the addresses node
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var addressList : ArrayList<Address> = ArrayList<Address>()
                for(child in dataSnapshot.children){
                    var address: Address? = child.getValue(Address::class.java)
                    address?.key = child.key
                    addressList.add(address!!)

                }

                recyclerViewSelectAddressAdapter.setData(addressList)



            }
        })




    }

    private fun setupToolbar(){
        var toolbar = myToolbar
        toolbar.title = "Select Address"
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
