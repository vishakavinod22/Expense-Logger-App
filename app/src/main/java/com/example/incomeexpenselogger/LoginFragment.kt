package com.example.incomeexpenselogger


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If user selects the sign up button, an intent is sent to ActivityOne to display the SignupFragment
        val signInBtn: Button = view.findViewById(R.id.userSignUpBtn)
        signInBtn.setOnClickListener {
            val intent = Intent(view.context, ActivityOne::class.java)
            intent.putExtra("signUpMsg", "Loading signup page...")
            startActivity(intent)
        }

        // If user selects the login button, an intent is sent to ActivityOne to display the LoginFragment
        val logBtn: Button = view.findViewById(R.id.logIn)
        logBtn.setOnClickListener {
            val username: EditText = view.findViewById(R.id.userName)
            val password: EditText = view.findViewById(R.id.userPassword)

            // Ensures that user does not leave any fields blank
            if(username.text.isNotEmpty() && password.text.isNotEmpty()){
                val intent = Intent(view.context, ActivityOne::class.java)
                intent.putExtra("logInMsg", "Logging in user...")
                intent.putExtra("logInUsername", username.text.toString())
                intent.putExtra("logInPassword", password.text.toString())
                startActivity(intent)
            }
            else {
                // Display toast message if user leaves any input fields empty
                Toast.makeText(
                    view.context,
                    "Please don't leave any fields empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}