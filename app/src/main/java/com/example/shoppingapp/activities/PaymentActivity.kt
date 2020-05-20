package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import com.example.shoppingapp.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()
    }

    private fun init(){

        var paymentType : String = ""
        radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            if(checkedId == R.id.radio_button_cash_on_delivery){
                paymentType = "cash on delivery"
            }
            else{
                paymentType = "online"
            }
        })

        button_complete_purchase.setOnClickListener{
            if(paymentType == ""){
                Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show()
            }
            else{
                startActivity(Intent(this, OrderConfirmationActivity::class.java))
            }
        }

    }
}
