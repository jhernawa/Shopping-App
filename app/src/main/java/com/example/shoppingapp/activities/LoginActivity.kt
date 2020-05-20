package com.example.shoppingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.R
import com.example.shoppingapp.Helpers.SessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {

        /*//save user login session using shared preference

        //direct users directly to the main activity if the user has logged in before
        var sessionManager = SessionManager(this)

        *//*if(sessionManager.checkLogin()){
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }*//*


        //button login set on click listener
        button_login.setOnClickListener{

            //check if entered username and password have been registered before
            var sessionManager = SessionManager(this)

            var username = edit_text_username.text.toString()
            var password = edit_text_password.text.toString()

            if(sessionManager.login(username, password)){
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
            }



        }
        text_view_new_user.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }*/





        button_login.setOnClickListener{

            var username = edit_text_username.text.toString()
            var password = edit_text_password.text.toString()

            /*  Sign the user in by saving the email and password in the database.
                And save the session by using shared preference (locally).
                This shared preference is done automatically by firebase
             */
            var auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task : Task<AuthResult>){
                        if(task.isSuccessful){
                            finish()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                        else{
                            Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                        }

                    }
                })
        }

        text_view_new_user.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        text_view_forgot_password.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }


}
