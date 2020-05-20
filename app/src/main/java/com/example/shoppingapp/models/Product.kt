package com.example.shoppingapp.models

import java.io.Serializable

data class Product(
    var _id : String,
    var image : String,
    var productName: String,
    var description: String,
    var price: Double,
    var mrp: Double,
    var quantity : Int
) : Serializable