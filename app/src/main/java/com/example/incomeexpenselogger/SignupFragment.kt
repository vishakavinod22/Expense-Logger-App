package com.example.incomeexpenselogger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_signup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // When user enters registration information and clicks SIGNUP the data is sent to ActivityOne
        // to be stored as a shared preference
        val signUpBtn: Button = view.findViewById(R.id.signUp)
        signUpBtn.setOnClickListener {
            val profileName: EditText = view.findViewById(R.id.profileName)
            val username: EditText = view.findViewById(R.id.userName)
            val password: EditText = view.findViewById(R.id.userPassword)

            // Ensures that user does not leave any fields blank
            if(profileName.text.isNotEmpty() && username.text.isNotEmpty() && password.text.isNotEmpty()){
                val intent = Intent(view.context, ActivityOne::class.java)
                intent.putExtra("signInMsg", "Signing in user...")
                intent.putExtra("signInProfileName", profileName.text.toString())
                intent.putExtra("signInUsername", username.text.toString())
                intent.putExtra("signInPassword", password.text.toString())
                startActivity(intent)
            } else{
                // Display toast message if user leaves any input fields empty
                Toast.makeText(view.context, "Please don't leave any fields empty", Toast.LENGTH_SHORT).show()
            }

        }
    }
}