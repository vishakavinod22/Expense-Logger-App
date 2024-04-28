package com.example.incomeexpenselogger

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenseList: ArrayList<Expenses>) : RecyclerView.Adapter<ExpenseCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCardViewHolder {
        val expenseView = LayoutInflater.from(parent.context).inflate(R.layout.activity_income_expense_cardview, parent,false)
        return ExpenseCardViewHolder(expenseView)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseCardViewHolder, position: Int) {
        val currentExpense = expenseList[position]
        holder.activityId.text = currentExpense.id.toString()
        holder.activityName.text = currentExpense.activityName
        holder.cost.text = currentExpense.cost.toString()
        holder.type.text = currentExpense.expenseType.toString()

        // When the edit item button is clicked, the selected item details are sent to ActivityThree to be updated
        holder.editBtn.setOnClickListener{
            val intent = Intent(holder.itemView.context, ActivityThree::class.java)
            intent.putExtra("btnType", "edit")
            if(currentExpense.id != null) {
                intent.putExtra("activityId", currentExpense.id.toInt())
            }
            holder.itemView.context.startActivity(intent)
        }

    }
}