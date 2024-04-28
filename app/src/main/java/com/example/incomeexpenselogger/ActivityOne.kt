package com.example.incomeexpenselogger

import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ActivityOne : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one)

        // Setting initial screen fragment to the user login page
        val loginFragment : Fragment = LoginFragment()
        val loginFragmentTransaction : FragmentTransaction =
            supportFragmentManager.beginTransaction()
        loginFragmentTransaction.replace(
            R.id.activityOneFrameLayout,
            loginFragment
        ).commit()

        // When the user enters their details for login, the values are sent from LoginFragment to ActivityOne via intents
        val loginUserMsg = intent?.getStringExtra("logInMsg")
        if (loginUserMsg != null) {
            val username = intent?.getStringExtra("logInUsername")
            val password = intent?.getStringExtra("logInPassword")

            // Validating the users login credentials
            var isValidated = false
            if(username !=null && password != null){
                isValidated = validateUserLogin(username, password)
            }
            // If validation is successful, go to ActivityTwo, else a toast message with the error is displayed
            if(isValidated && username != null){
                val intent = Intent(this, ActivityTwo::class.java)
                intent.putExtra("username", username)
                intent.putExtra("profile", getUserProfileName(username))
                startActivity(intent)
            }
        }

        // If the user clicks the sign up page button, the sign in page fragment is displayed
        val signUpBtnMsg = intent?.getStringExtra("signUpMsg")
        if (signUpBtnMsg != null) {
            val signupFragment : Fragment = SignupFragment()
            val signupFragmentTransaction : FragmentTransaction =
                supportFragmentManager.beginTransaction()
            signupFragmentTransaction.replace(
                R.id.activityOneFrameLayout,
                signupFragment
            ).commit()
        }

        // When the user enters their details for sign up, the values are sent from SignupFragment to ActivityOne via intents
        val signupUserMsg = intent?.getStringExtra("signInMsg")
        if (signupUserMsg != null) {
            val profileName = intent?.getStringExtra("signInProfileName")
            val username = intent?.getStringExtra("signInUsername")
            val password = intent?.getStringExtra("signInPassword")
            if(profileName != null && username !=null && password != null){
                signUpUser(profileName, username, password)
                Toast.makeText(this, signupUserMsg, Toast.LENGTH_SHORT).show()
            }
        }

    }

    // JSON : https://www.baeldung.com/kotlin/json-convert-data-class
    // Method to save user details into a shared preference file using JSONs
    private fun signUpUser(profileName: String, username: String, password: String) {

        val userInfo = Users(profileName, username, password)
        val sharedPreferences : SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val userInfoJson = Gson().toJson(userInfo)

        editor.putString(username, userInfoJson)
        editor.apply()
    }

    // Method to validate users login credentials
    private fun validateUserLogin(username: String, password: String): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)

        val userInfoJson = sharedPreferences.getString(username, null)

        if (userInfoJson != null) {
            val user: Users = Gson().fromJson(userInfoJson, Users::class.java)
            if (user.password == password) {
                return true
            } else {
                Toast.makeText(this, "Invalid password...", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid username...", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    // Method to get users profile names from their usernames
    private fun getUserProfileName(username: String): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)

        val userInfoJson = sharedPreferences.getString(username, null)

        if (userInfoJson != null) {
            val user: Users = Gson().fromJson(userInfoJson, Users::class.java)
            return user.profileName.toString()
        }
        return "null"
    }

}
