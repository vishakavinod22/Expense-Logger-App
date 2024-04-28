package com.example.incomeexpenselogger

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

// Ref: Moh Week 7
class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_name, null, 1) {

    // Declaring DB constant values
    companion object {
        const val DB_name = "UserExpensesDatabase.db"
        const val TB_name = "UserExpenses"
        const val ID = "ID"
        const val USER_ID = "User_ID"
        const val ACTIVITY_NAME = "Name"
        const val COST = "Cost"
        const val EXPENSE_TYPE = "Expense_Type"
        const val DESCRIPTION = "Description"
    }

    // Performs the Create operation
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TB_name ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID TEXT, $ACTIVITY_NAME TEXT, $COST DOUBLE, $EXPENSE_TYPE TEXT, $DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TB_name")
        onCreate(db)
    }

    // Performs the Insert operation
    fun addExpense(username: String, activityName: String, cost: Double, expenseType: String, description: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(USER_ID, username)
            put(ACTIVITY_NAME, activityName)
            put(COST, cost)
            put(EXPENSE_TYPE, expenseType)
            put(DESCRIPTION, description)
        }
        db.insert(TB_name, null, values)
        db.close()
    }

    // Performs the Update operation
    fun updateExpense(itemId: Int, itemName: String?, itemAmt: Double?, itemDesc: String?, itemType: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ACTIVITY_NAME, itemName)
        contentValues.put(COST, itemAmt)
        contentValues.put(DESCRIPTION, itemDesc)
        contentValues.put(EXPENSE_TYPE, itemType)
        db.update(TB_name, contentValues, "$ID = ?", arrayOf(itemId.toString()))
        db.close()
    }

    // Performs the Delete operation
    fun deleteExpense(itemId: Int){
        val db = this.writableDatabase
        db.delete(TB_name, "$ID = ?", arrayOf(itemId.toString()))
        db.close()
    }

    // Deletes all the rows from the table
    fun deleteAllExpenses() {
        val db = this.writableDatabase
        db.delete(TB_name, null, null)
        db.close()
    }

}