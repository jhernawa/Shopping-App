package com.example.shoppingapp.models

import java.io.Serializable

data class Address(
    var key : String? = null,
    var addressType: String? = null,
    var name: String? = null,
    var mobile : String? = null,
    var streetName : String? = null,
    var houseNumber : String? = null,
    var city : String? = null,
    var zipCode: String? = null
) : Serializable{

    companion object{
        const val KEY_ADDRESS = "address"
    }

}