package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.shoppingapp.R
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class AddAddressActivity : AppCompatActivity() {

    lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        databaseReference = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DATABASE_NAME)

        init()
    }

    private fun init() {

        //set up toolbar
        setupToolbar()

        button_add_address.setOnClickListener{

            //address type
            var addressType = ""
            if(radio_button_home.isChecked()) addressType = "Home"
            else if(radio_button_office.isChecked()) addressType = "Office"
            else if(radio_button_other.isChecked()) addressType = "Other"

            //name
            var name = edit_text_name.text.toString()

            //mobile
            var mobile = edit_text_mobile_number.text.toString()

            //address
            var streetName = edit_text_street_name.text.toString()
            var houseNumber = edit_text_house_or_building_number.text.toString()
            var city = edit_text_city.text.toString()

            //zip code
            var zipCode = edit_text_zipcode.text.toString()

            /*save to firebase*/
            var address = Address("", addressType, name, mobile, streetName, houseNumber, city, zipCode)
            var addressId =  databaseReference.push().key

            databaseReference.child(addressId!!).setValue(address)


            if(addressType == ""){
                Toast.makeText(this, "Please select address type", Toast.LENGTH_SHORT).show()
            }else{
                startActivity(Intent(this, SelectAddressActivity::class.java))
            }



        }
    }

    private fun setupToolbar(){
        var toolbar = myToolbar
        toolbar.title = "Add Address"
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
