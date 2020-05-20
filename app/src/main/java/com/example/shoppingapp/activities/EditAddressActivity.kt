package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Address
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_city
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_house_or_building_number
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_mobile_number
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_name
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_street_name
import kotlinx.android.synthetic.main.activity_edit_address.edit_text_zipcode
import kotlinx.android.synthetic.main.activity_edit_address.radio_button_home
import kotlinx.android.synthetic.main.activity_edit_address.radio_button_office
import kotlinx.android.synthetic.main.activity_edit_address.radio_button_other
import kotlinx.android.synthetic.main.activity_edit_address.*

class EditAddressActivity : AppCompatActivity() {

    lateinit var databaseReference: DatabaseReference

    lateinit var address : Address
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)

        databaseReference = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DATABASE_NAME)

        /*show the current address detail*/
        address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address

        //address type
        if(address.addressType == "Home"){
            radio_button_home.setChecked(true)
        }
        else if(address.addressType == "Office"){
            radio_button_office.setChecked(true)
        }
        else{
            radio_button_other.setChecked(true)
        }

        //name
        edit_text_name.setText(address?.name)

        //mobile
        edit_text_mobile_number.setText(address?.mobile)

        //address
        edit_text_street_name.setText(address?.streetName)
        edit_text_house_or_building_number.setText(address?.houseNumber)
        edit_text_city.setText(address?.city)

        //zip code
        edit_text_zipcode.setText(address?.zipCode)

        init()
    }

    private fun init() {

        button_edit_address.setOnClickListener{

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

            var updatedAddress = Address(address.key, addressType, name, mobile, streetName, houseNumber, city, zipCode)

            databaseReference.child(address?.key!!).setValue(updatedAddress)

            startActivity(Intent(this, SelectAddressActivity::class.java))

        }

    }
}
