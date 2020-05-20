package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        init()
    }

    private fun init() {


        button_reset_password.setOnClickListener {
            var username = edit_text_username.text.toString()

            var auth = FirebaseAuth.getInstance()

            if (username.isEmpty()) {
                Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show()
            } else {

                auth.sendPasswordResetEmail(username)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Reset password link was sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            var errorMessage = task.exception?.message
                            Toast.makeText(
                                this,
                                "No registered user is associated with this username",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


            }

        }

        text_view_remember_password.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
