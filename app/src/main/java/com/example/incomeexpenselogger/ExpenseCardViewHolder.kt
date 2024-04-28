package com.example.incomeexpenselogger

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ExpenseCardViewHolder(expenseView: View) : RecyclerView.ViewHolder(expenseView){
    val editBtn : Button = expenseView.findViewById(R.id.editButton)
    val activityId : TextView = expenseView.findViewById(R.id.itemID)
    val activityName: TextView = expenseView.findViewById(R.id.userActivity)
    val cost: TextView = expenseView.findViewById(R.id.activityAmount)
    val type: TextView = expenseView.findViewById(R.id.activityType)
}