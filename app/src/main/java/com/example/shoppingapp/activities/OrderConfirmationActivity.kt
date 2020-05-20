package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import kotlinx.android.synthetic.main.activity_order_confirmation.*

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        init()
    }

    private fun init() {

        button_home.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
