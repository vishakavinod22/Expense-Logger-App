package com.example.incomeexpenselogger

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ActivityTwo : AppCompatActivity() {
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var dbHelper: DBHelper
    private lateinit var expenseAdapter: ExpenseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        dbHelper = DBHelper(this)

        // If database is empty, then initializes sample data
        initializeData()

        // Gets username and profileName values from ActivityOne
        val username = intent.getStringExtra("username")
        val profileName = intent.getStringExtra("profile")

        val userProfileName = findViewById<TextView>(R.id.userProfileName)
        var displayGreeting = "Hello \n${profileName}".uppercase()
        userProfileName.text = displayGreeting

        expensesRecyclerView = findViewById(R.id.incomeExpenseRecyclerView)
        // Set the layout manager for the expenses RecyclerView
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        // Set the size of the RecyclerView to be fixed.
        expensesRecyclerView.setHasFixedSize(true)

        // Calls the loadData method to load SQLite database and display data in RecyclerView
        loadData(username.toString())

        // When ActivityTwo is reloaded after adding, editing, or deleting data,
        // the current username is passed as an intent
        val currentUser = intent.getStringExtra("currentUser")
        if(currentUser!= null){
            // Resets the username and profile name in ActivityTwo
            val cUser = currentUser.toString()
            val profile = getUserProfileName(currentUser)
            displayGreeting = "Hello $profile".uppercase()
            userProfileName.text = displayGreeting
            loadData(cUser)
        }

        // When user clicks LOGOUT, the activity gets finished and ActivityOne is called
        val logoutBtn : Button = findViewById(R.id.logOutBtn)
        logoutBtn.setOnClickListener{
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, ActivityOne::class.java)
            startActivity(intent)

        }

        // When user clicks ADD ITEM, ActivityThree is called
        val addItemBtn : Button = findViewById(R.id.addNewItem)
        addItemBtn.setOnClickListener{
            val intent = Intent(this, ActivityThree::class.java)
            intent.putExtra("btnType", "add")
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    // Method to load data from SQLite database
    // Ref: Mohesen week 7
    private fun loadData(username: String){
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TB_name} WHERE ${DBHelper.USER_ID} = ?", arrayOf(username))
        val expenseList = ArrayList<Expenses>()

        while (cursor.moveToNext()) {
            val activityId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID))
            val activityName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ACTIVITY_NAME))
            val cost = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COST))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EXPENSE_TYPE))
            val typeSymbol : String = if(type.lowercase() == "income"){
                "+"
            } else if(type.lowercase() == "expense"){
                "-"
            } else {
                ""
            }
            expenseList.add(Expenses(activityId, username, activityName, cost, typeSymbol, ""))
        }
        cursor.close()
        expenseAdapter = ExpenseAdapter(expenseList)
        expensesRecyclerView.adapter = expenseAdapter
        db.close()
    }

    // Method to initialize data
    private fun initializeData() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DBHelper.TB_name}", null)
        if (cursor != null && cursor.moveToFirst()) {
            val count = cursor.getInt(0)
            if (count == 0) {
                dbHelper.addExpense("vish", "Salary", 15.0, "income", "Monthly Income")
                dbHelper.addExpense("siya@gmail.com", "Coffee", 2.50, "expense", "Tim Horton")
                dbHelper.addExpense("vish", "Cake", 4.75, "expense", "Red Velvet")
            }
            cursor.close()
        }
        db.close()
    }

    // Method to get user profile name via username
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