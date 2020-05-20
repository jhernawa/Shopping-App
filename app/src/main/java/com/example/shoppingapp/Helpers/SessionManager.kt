package com.example.shoppingapp.Helpers

import android.content.Context
import android.content.SharedPreferences

class SessionManager(var mainActivityContext: Context){

    var sharedPreferences: SharedPreferences = mainActivityContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object{
        private const val FILE_NAME = "registered_user"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NAME = "name"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    fun register(name : String, username : String, password: String){

        editor.putString(KEY_NAME, name)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.commit()

    }

    fun login(username : String, password: String) : Boolean{

        var success : Boolean = false
        if(sharedPreferences.getString(KEY_USERNAME, null) == username && sharedPreferences.getString(KEY_PASSWORD, null) == password){
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
            editor.commit()
            success = true
        }

        return success

    }

    fun checkLogin(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}