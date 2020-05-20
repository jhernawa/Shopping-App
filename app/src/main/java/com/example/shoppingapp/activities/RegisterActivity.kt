package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.Helpers.SessionManager
import com.example.shoppingapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {


        button_register.setOnClickListener{


           /* //save user registration in Shared Preference

            var sessionManager = SessionManager(this)

            var name = edit_text_name.text.toString()
            var username = edit_text_username.text.toString()
            var password = edit_text_password.text.toString()

            sessionManager.register(name, username, password)



            //create Toast Message
            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()

            //Go back to Login Activity
            startActivity(Intent(this, LoginActivity::class.java))*/



            /////////////////////////////////////////////////////////////////////////////////

            //save user registration in Firebase
            var name = edit_text_name.text.toString()
            var username = edit_text_username.text.toString()
            var password = edit_text_password.text.toString()


            var auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(username, password) //listen to whether the task is completed or not
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "user registered successfully", Toast.LENGTH_SHORT).show()

                            //redirect user back to login activity
                            finish()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "user email exists already", Toast.LENGTH_SHORT).show()
                        }
                    }

                })





        }

        text_view_registered_user.setOnClickListener{
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }


}
