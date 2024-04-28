package com.example.incomeexpenselogger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ActivityThree : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three)

        dbHelper = DBHelper(this)

        val activityNameEditText: EditText = findViewById(R.id.addActivityName)
        val activityAmountEditText: EditText = findViewById(R.id.addAmount)
        val activityDescrEditText: EditText = findViewById(R.id.addDescription)
        val activityTypeRadioGrp: RadioGroup = findViewById(R.id.activityTypeRadioBtn)

        // This Activity displays two view formats for ADD and EDIT
        //      one: when new item is to be added and (id is set to -1)
        //      two: when existing item to be deleted (id is set to the actual item id)
        var id: Int = -1
        var user = ""
        val btnType = intent.getStringExtra("btnType")

        // When user clicks ADD ITEM
        if(btnType.equals("add")){
            // username is received from the previous activity
            user = intent.getStringExtra("username").toString()
        }
        // When user clicks EDIT ITEM
        else if(btnType.equals("edit")){
            // get the id of the selected item
            id =  intent.getIntExtra("activityId", -1)
            // based on the item id, get item information from database
            val currentItem = getItemById(id)
            // update item values based on user modifications
            if(currentItem != null){
                activityNameEditText.setText(currentItem.activityName)
                activityAmountEditText.setText(currentItem.cost.toString())
                activityDescrEditText.setText(currentItem.description)
                // setting the type of activity as income or expense
                if(currentItem.expenseType == "income"){
                    activityTypeRadioGrp.check(R.id.incomeBtn)
                } else {
                    activityTypeRadioGrp.check(R.id.expenseBtn)
                }
            }
        }

        // When user clicks SAVE ITEM
        val saveBtn: Button = findViewById(R.id.saveItem)
        saveBtn.setOnClickListener{
            // The XML values are extracted
            val expenseTypeId = activityTypeRadioGrp.checkedRadioButtonId
            val activityTypeRadioBtn : RadioButton = findViewById(expenseTypeId)
            val activityName = activityNameEditText.text.toString()
            val cost = activityAmountEditText.text.toString().toDouble()
            val expenseType = activityTypeRadioBtn.text.toString()
            val description = activityDescrEditText.text.toString()
            // id would be -1 if user is saving a new item
            if(id < 0){
                // new item is added to the database
                addNewItem(user, activityName, cost, expenseType, description)
            }
            // id would be the item id if user is saving an edited item
            else {
                // get the current item id
                val currentItem : Expenses? = getItemById(id)
                // based on user modifications, update the database
                if (currentItem != null) {
                    currentItem.activityName = activityName
                    currentItem.cost = cost
                    currentItem.expenseType = expenseType
                    currentItem.description = description
                }
                editItem(id, currentItem)
            }
        }

        // id would be -1 if user is deleting a new item
        val deleteBtn: Button = findViewById(R.id.deleteItem)
        deleteBtn.setOnClickListener{
            var currentUser : String? = null
            // id would be the item id if user is deleting an existing item
            if(id >= 0){
                currentUser = getUserById(id)
            }
            // delete item based on id, pass current user to the next Activity
            deleteItem(id, currentUser)
        }

        // When CLOSE button is clicked the previous state of ActivityTwo is loaded
        val closeBtn: Button = findViewById(R.id.closePage)
        closeBtn.setOnClickListener{
            finish()
        }

    }
    // Method to get item details by id
    private fun getItemById(itemId: Int): Expenses?{
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TB_name} WHERE ${DBHelper.ID} = ?", arrayOf(itemId.toString())
        )

        var item: Expenses? = null

        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_ID))
            val activityName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ACTIVITY_NAME))
            val cost = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COST))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EXPENSE_TYPE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DESCRIPTION))

            item = Expenses(itemId, username, activityName, cost, type, description)
        }
        cursor.close()
        db.close()
        return item
    }

    // Method to get username by id
    private fun getUserById(itemId: Int): String?{
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TB_name} WHERE ${DBHelper.ID} = ?", arrayOf(itemId.toString())
        )

        var user: String? = null

        while (cursor.moveToNext()) {
            user = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USER_ID))
        }
        cursor.close()
        db.close()
        return user
    }

    // method that calls DBHelper to add an item
    private fun addNewItem(username: String, activityName: String, cost: Double, expenseType: String, description: String){
        dbHelper.addExpense(username, activityName, cost, expenseType, description)
        val intent = Intent(this, ActivityTwo::class.java)
        intent.putExtra("currentUser", username)
        startActivity(intent)
    }

    // method that calls DBHelper to edit an item
    private fun editItem(id: Int, currentItem: Expenses?){
        var user = ""
        if(currentItem != null){
            dbHelper.updateExpense(id, currentItem.activityName, currentItem.cost, currentItem.description, currentItem.expenseType)
            user = currentItem.username.toString()
        }
        val intent = Intent(this, ActivityTwo::class.java)
        intent.putExtra("currentUser", user)
        startActivity(intent)
    }

    // method that calls DBHelper to delete an item
    private fun deleteItem(id: Int, currentUser: String?){
        if(currentUser != null){
            dbHelper.deleteExpense(id)
            val intent = Intent(this, ActivityTwo::class.java)
            intent.putExtra("currentUser", currentUser)
            startActivity(intent)
        } else {
            finish()
        }

    }

}